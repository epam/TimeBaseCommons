/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.epam.deltix.util.oauth.service;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;
import com.epam.deltix.util.oauth.Oauth2Client;
import com.epam.deltix.util.oauth.Oauth2ClientConfig;
import com.epam.deltix.util.oauth.utils.IncreasingDelayRetryStrategy;
import com.epam.deltix.util.oauth.utils.RetryStrategy;
import com.epam.deltix.util.oauth.utils.TimerTokenScheduler;
import com.epam.deltix.util.time.TimeKeeper;
import com.epam.deltix.util.oauth.AuthResult;
import com.epam.deltix.util.oauth.RefreshTokenListener;
import com.epam.deltix.util.oauth.utils.RefreshTokenScheduler;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Oauth2ClientImpl implements Oauth2Client {

    public static final Log LOGGER = LogFactory.getLog(Oauth2ClientImpl.class.getName());

    public static final String GRANT_TYPE_PARAM = "grant_type";
    public static final String CLIENT_ID_PARAM = "client_id";
    public static final String CLIENT_SECRET_PARAM = "client_secret";

    public static final String CLIENT_CREDENTIALS_GRANT_TYPE = "client_credentials";

    private final Oauth2ClientConfig config;
    private final RestClient restClient;
    private final TokenQuery tokenQuery;

    private final TokenResponseParser parser;
    private final String clientId;
    private final Map<String, String> parameters = new HashMap<>();

    private final RefreshTokenListener listener;
    private final RefreshTokenScheduler refreshScheduler;

    private final RetryStrategy retryStrategy = new IncreasingDelayRetryStrategy();

    private volatile AuthResult authResult;
    private volatile long expirationTimestampMs;

    private volatile boolean closed;

    private final ReentrantLock lock = new ReentrantLock();

    public static Oauth2ClientImpl of(Oauth2ClientConfig config) {
        RestClient restClient = HttpConnectionRestClient.create(
            config.getUrl(), config.getConnectTimeoutMs(), config.getReadTimeoutMs()
        );
        return new Oauth2ClientImpl(config, restClient);
    }

    private Oauth2ClientImpl(Oauth2ClientConfig config, RestClient restClient) {
        this.config = config;
        this.restClient = restClient;
        this.tokenQuery = createTokenQuery(config);
        this.parser = new TokenResponseParser();
        this.parameters.putAll(config.getParameters());
        this.clientId = parameters.get(CLIENT_ID_PARAM);
        this.listener = config.getListener();
        this.refreshScheduler = config.getTimer() != null
            ? new TimerTokenScheduler(config.getTimer()) : null;

        // initial token request
        try {
            requestToken();
        } catch (Exception t) {
            LOGGER.error().append("Failed to request token").append(t).commit();
        }
    }

    private static TokenQuery createTokenQuery(Oauth2ClientConfig config) {
        if (CLIENT_CREDENTIALS_GRANT_TYPE.equals(config.getParameters().get(GRANT_TYPE_PARAM))) {
            String clientId = config.getParameters().get(CLIENT_ID_PARAM);
            if (clientId == null) {
                throw new RuntimeException(CLIENT_ID_PARAM + " is not specified");
            }

            if (config.getParameters().get(CLIENT_SECRET_PARAM) == null) {
                if (config.getKeystoreConfig() != null) {
                    return new CertificateTokenQuery(config.getUrl(), clientId,
                        config.getKeystoreConfig(), config.getParameters()
                    );
                }

                throw new RuntimeException("Invalid credentials: specify `" + CLIENT_SECRET_PARAM + "` parameter or keystore config.");
            }
        }

        return new ParametersTokenQuery(config.getParameters());
    }

    @Override
    public AuthResult login() {
        return getOrRequestToken();
    }

    public long expirationTimestampMs() {
        return expirationTimestampMs;
    }

    private AuthResult getOrRequestToken() {
        if (authResult == null) {
            requestTokenIfNeed();
        } else if (refreshScheduler == null) {
            // configured without refresh task
            // perform refresh token in current thread if needed
            if (refreshRequired()) {
                refreshTokenIfNeed();
            }
        }

        return authResult;
    }

    private void requestTokenIfNeed() {
        boolean requestTimeout = !tryUnderLock(() -> {
            // double check under lock
            if (authResult == null) {
                requestToken();
            }
        });
        if (requestTimeout) {
            throw new RuntimeException("Request token timeout");
        }
    }

    private void refreshTokenIfNeed() {
        tryUnderLock(() -> {
            // double check under lock
            if (refreshRequired()) {
                requestToken();
            }
        });
    }

    //returns false in case of timeout
    private boolean tryUnderLock(Runnable logic) {
        boolean locked;
        try {
            locked = lock.tryLock(config.getTimeoutMs(), TimeUnit.MILLISECONDS);
            if (locked) {
                try {
                    logic.run();
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }

        return locked;
    }

    private boolean refreshRequired() {
        return currentTime() >= expirationTimestampMs;
    }

    private long currentTime() {
        return TimeKeeper.currentTime;
    }

    private void requestToken() {
        if (closed) {
            return;
        }

        long requestTime = currentTime();
        authResult = parser.parse(clientId, sendTokenRequest());
        if (authResult.expiresInSec() == 0) {
            LOGGER.warn().append("Token updated (grant type: ").append(parameters.get(GRANT_TYPE_PARAM))
                .append("); expiration timestamp unknown, refresh task is not scheduled.").commit();
        } else {
            // update expiration time
            long expirationDelayMs = authResult.expiresInSec() * (long) (config.getExpirationMultiplier() * 1000.0d);
            expirationTimestampMs = requestTime + expirationDelayMs;

            LOGGER.info().append("Token updated (grant type: ").append(parameters.get(GRANT_TYPE_PARAM))
                .append("); expiration timestamp: ")
                .append(Instant.ofEpochMilli(expirationTimestampMs)).commit();

            scheduleRefresh(expirationDelayMs);
        }

        notifyRefreshed();
    }

    private String sendTokenRequest() {
        try {
            return restClient.postForm(tokenQuery);
        } catch (IOException e) {
            throw new RuntimeException("Failed to perform REST query", e);
        } catch (Exception t) {
            LOGGER.warn().append("Failed to request token").append(t).commit();
            throw t;
        }
    }

    private void notifyRefreshed() {
        if (listener != null) {
            listener.refreshed(authResult);
        }
    }

    private void scheduleRefresh(long delayMs) {
        if (refreshScheduler != null && !closed) {
            refreshScheduler.schedule(delayMs, this::refreshTokenTask);
            LOGGER.info().append("Refresh token task scheduled in ").append(delayMs / 1000).append(" seconds.").commit();
        }
    }

    private void refreshTokenTask() {
        try {
            lock.lock();
            try {
                requestToken();
                retryStrategy.refreshRetryDelay();
            } finally {
                lock.unlock();
            }
        } catch (Exception t) {
            LOGGER.warn().append("Failed to execute task").append(t).commit();
            scheduleRefresh(retryStrategy.nextRetryDelay());
        }
    }

    @Override
    public void close() {
        closed = true;
        if (refreshScheduler != null) {
            refreshScheduler.close();
        }
    }

}
