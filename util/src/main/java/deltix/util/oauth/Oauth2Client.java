package deltix.util.oauth;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;
import deltix.util.time.TimeKeeper;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Oauth2Client implements AutoCloseable {

    public static final Log LOGGER = LogFactory.getLog(Oauth2Client.class.getName());

    private static final long DEFAULT_RETRY_DELAY_MS = 5 * 1000;
    private static final long MAX_RETRY_DELAY_MS = 5 * 60 * 1000; // 5 min

    private final RestClient restClient;

    private final TokenResponseParser parser;
    private final String clientId;
    private final Map<String, String> parameters = new HashMap<>();

    private final TokenListener listener;
    private final RefreshTokenScheduler refreshScheduler;

    private final long expirationMultiplier;

    private long retryDelay = DEFAULT_RETRY_DELAY_MS;

    private volatile TokenInfo tokenInfo;
    private volatile long expirationTimestampMs;

    private volatile boolean closed;

    private final long timeoutMs;
    private final ReentrantLock lock = new ReentrantLock();

    public static Builder builder() {
        return new Builder();
    }

    private Oauth2Client(RestClient restClient, Map<String, String> parameters,
                         RefreshTokenScheduler refreshScheduler, TokenListener listener,
                         long expirationMultiplier, long timeoutMs) {
        this.restClient = restClient;
        this.parser = new GreenJellyTokenResponseParser();
        this.clientId = parameters.get("client_id");
        this.parameters.putAll(parameters);
        this.listener = listener;
        this.refreshScheduler = refreshScheduler;
        this.expirationMultiplier = expirationMultiplier;
        this.timeoutMs = timeoutMs;

        // initial token request
        try {
            requestToken();
        } catch (Throwable t) {
            LOGGER.error().append("Failed to request token").append(t).commit();
        }
    }

    public String clientId() {
        return clientId;
    }

    public String token() {
        return getOrRequestToken().accessToken();
    }

    public long expirationTimestampMs() {
        return expirationTimestampMs;
    }

    private TokenInfo getOrRequestToken() {
        if (tokenInfo == null) {
            requestTokenIfNeed();
        } else if (refreshScheduler == null) {
            // configured without refresh task
            // perform refresh token in current thread if needed
            if (refreshRequired()) {
                refreshTokenIfNeed();
            }
        }

        return tokenInfo;
    }

    private void requestTokenIfNeed() {
        boolean requestTimeout = !tryUnderLock(() -> {
            // double check under lock
            if (tokenInfo == null) {
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
            locked = lock.tryLock(timeoutMs, TimeUnit.MILLISECONDS);
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
        tokenInfo = parser.parse(sendTokenRequest(parameters));

        // update expiration time
        long expirationDelayMs = tokenInfo.expiresInSec() * expirationMultiplier;
        expirationTimestampMs = requestTime + expirationDelayMs;

        LOGGER.info().append("Token updated (grant type: ").append(parameters.get("grant_type"))
            .append("); expiration timestamp: ")
            .append(Instant.ofEpochMilli(expirationTimestampMs)).commit();

        scheduleRefresh(expirationDelayMs);
        notifyRefreshed();
    }

    private String sendTokenRequest(Map<String, String> parameters) {
        try {
            return restClient.postForm(parameters);
        } catch (IOException e) {
            throw new RuntimeException("Failed to perform REST query", e);
        }
    }

    private void notifyRefreshed() {
        if (listener != null) {
            listener.refreshed(tokenInfo.accessToken(), tokenInfo.expiresInSec());
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
                refreshRetryDelay();
            } finally {
                lock.unlock();
            }
        } catch (Throwable t) {
            LOGGER.warn().append("Failed to execute task").append(t).commit();
            scheduleRefresh(getRetryDelay());
        }
    }

    private void refreshRetryDelay() {
        retryDelay = DEFAULT_RETRY_DELAY_MS;
    }

    private long getRetryDelay() {
        long currentRetryDelay = retryDelay;
        retryDelay *= 2;
        if (retryDelay > MAX_RETRY_DELAY_MS) {
            retryDelay = MAX_RETRY_DELAY_MS;
        }

        return currentRetryDelay;
    }

    @Override
    public void close() {
        closed = true;
        if (refreshScheduler != null) {
            refreshScheduler.close();
        }
    }

    public static class Builder {

        private String url;
        private final Map<String, String> parameters = new HashMap<>();

        private RefreshTokenScheduler refreshScheduler;

        private TokenListener listener;

        private long timeoutMs = 5000;
        private int connectTimeoutMs = 5000;
        private int readTimeoutMs = 5000;

        private long expirationMultiplier = 700;

        private Builder() {
        }

        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder withClientCredentials(String clientId, String clientSecret) {
            parameters.put("grant_type", "client_credentials");
            parameters.put("client_id", clientId);
            parameters.put("client_secret", clientSecret);
            return this;
        }

        public Builder withParameter(String name, String value) {
            this.parameters.put(name, value);
            return this;
        }

        public Builder withRefreshScheduler(RefreshTokenScheduler refreshScheduler) {
            this.refreshScheduler = refreshScheduler;
            return this;
        }

        public Builder withTimer(Timer timer) {
            this.refreshScheduler = new TimerTokenScheduler(timer);
            return this;
        }

        public Builder withListener(TokenListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder withTimeout(long timeoutMs) {
            this.timeoutMs = timeoutMs;
            return this;
        }

        public Builder withConnectTimeout(int connectTimeoutMs) {
            this.connectTimeoutMs = connectTimeoutMs;
            return this;
        }

        public Builder withReadTimeout(int readTimeoutMs) {
            this.readTimeoutMs = readTimeoutMs;
            return this;
        }

        public Builder withExpirationMultiplier(double multiplier) {
            this.expirationMultiplier = (long) (multiplier * (double) 1000);
            return this;
        }

        public Oauth2Client build() {
            if (parameters.get("client_id") == null) {
                throw new RuntimeException("client id is not specified");
            }
            if (parameters.get("client_secret") == null) {
                throw new RuntimeException("client secret is not specified");
            }

            RestClient restClient = HttpConnectionRestClient.create(url, connectTimeoutMs, readTimeoutMs);
            return new Oauth2Client(restClient, parameters, refreshScheduler, listener, expirationMultiplier, timeoutMs);
        }

    }

}
