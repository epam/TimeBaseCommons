package com.epam.deltix.util.oauth.utils;

public interface RetryStrategy {

    void refreshRetryDelay();

    long nextRetryDelay();

    int retriesMade();

}
