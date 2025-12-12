package com.epam.deltix.util.oauth.utils;

public class IncreasingDelayRetryStrategy implements RetryStrategy {

    private static final long DEFAULT_RETRY_DELAY_MS = 5 * 1000;
    private static final long MAX_RETRY_DELAY_MS = 5 * 60 * 1000; // 5 min

    private long retryDelay = DEFAULT_RETRY_DELAY_MS;

    private int retriesMade;

    @Override
    public synchronized void refreshRetryDelay() {
        retryDelay = DEFAULT_RETRY_DELAY_MS;
        retriesMade = 0;
    }

    @Override
    public synchronized long nextRetryDelay() {
        ++retriesMade;

        long currentRetryDelay = retryDelay;
        retryDelay *= 2;
        if (retryDelay > MAX_RETRY_DELAY_MS) {
            retryDelay = MAX_RETRY_DELAY_MS;
        }

        return currentRetryDelay;
    }

    @Override
    public int retriesMade() {
        return retriesMade;
    }

}
