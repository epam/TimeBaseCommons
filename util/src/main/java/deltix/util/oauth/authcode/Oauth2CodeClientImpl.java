package deltix.util.oauth.authcode;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;
import deltix.util.oauth.*;
import deltix.util.oauth.utils.*;
import deltix.util.time.TimeKeeper;

import java.time.Instant;

public class Oauth2CodeClientImpl implements Oauth2CodeClient {

    public static final Log LOGGER = LogFactory.getLog(AuthCodeClient.class.getName());

    private final Oauth2CodeClientConfig config;
    private final AuthCodeClient client;

    private final RefreshTokenScheduler refreshScheduler;

    private final RefreshTokenListener listener;

    private final RetryStrategy retryStrategy = new IncreasingDelayRetryStrategy();

    private volatile AuthResult authResult;
    private volatile long expirationTimestampMs;

    private volatile boolean closed;

    public static Oauth2CodeClientImpl of(Oauth2CodeClientConfig config) {
        return new Oauth2CodeClientImpl(config, null);
    }

    public static Oauth2CodeClientImpl of(Oauth2CodeClientConfig config, RefreshTokenListener listener) {
        return new Oauth2CodeClientImpl(config, listener);
    }

    private Oauth2CodeClientImpl(Oauth2CodeClientConfig config, RefreshTokenListener listener) {
        this.config = config;
        this.client = new AuthCodeClient(config);

        if (config.getTimer() != null) {
            this.refreshScheduler = new TimerTokenScheduler(config.getTimer());
        } else if (config.getExecutor() != null) {
            this.refreshScheduler = new ExecutorRefreshScheduler(config.getExecutor());
        } else {
            this.refreshScheduler = null;
        }

        this.listener = listener;
    }

    @Override
    public synchronized AuthResult login() {
        if (authResult != null) {
            return authResult;
        }

        long requestTime = currentTime();
        authResult = client.requestToken();
        long delayMs = updateExpirationTimeAndGetDelay(requestTime);
        LOGGER.info().append("Token requested (grant type: authorization_code); expiration timestamp: ")
            .append(Instant.ofEpochMilli(expirationTimestampMs)).commit();

        scheduleRefresh(delayMs);
        return authResult;
    }

    private long currentTime() {
        return TimeKeeper.currentTime;
    }

    private void scheduleRefresh(long delayMs) {
        if (refreshScheduler != null && !closed) {
            if (authResult.refreshToken() != null) {
                refreshScheduler.schedule(delayMs, this::refreshTokenTask);
                LOGGER.info().append("Refresh token task scheduled in ").append(delayMs / 1000).append(" seconds.").commit();
            } else {
                LOGGER.warn().append("Refresh token is empty. Refresh task can't be scheduled.").commit();
            }
        }
    }

    private void refreshTokenTask() {
        try {
            if (closed) {
                return;
            }

            long requestTime = currentTime();
            authResult = refreshToken();
            long delayMs = updateExpirationTimeAndGetDelay(requestTime);

            LOGGER.info().append("Token refreshed (grant type: authorization_code); expiration timestamp: ")
                .append(Instant.ofEpochMilli(expirationTimestampMs)).commit();

            notifyRefreshed();
            retryStrategy.refreshRetryDelay();

            scheduleRefresh(delayMs);
        } catch (Throwable t) {
            LOGGER.warn().append("Failed to refresh token.").append(t).commit();

            if (retryStrategy.retriesMade() > config.getRefreshRetriesCount()) {
                notifyRefreshFailed(t);
            } else {
                scheduleRefresh(retryStrategy.nextRetryDelay());
            }
        }
    }

    private long updateExpirationTimeAndGetDelay(long requestTime) {
        long expirationDelayMs = authResult.expiresInSec() * (long) (config.getExpirationMultiplier() * 1000);
        expirationTimestampMs = requestTime + expirationDelayMs;
        return expirationDelayMs;
    }

    private AuthResult refreshToken() {
        return client.refreshToken(authResult.refreshToken());
    }

    private void notifyRefreshed() {
        if (listener != null) {
            listener.refreshed(authResult);
        }
    }

    private void notifyRefreshFailed(Throwable t) {
        if (listener != null) {
            listener.refreshFailed(t);
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
