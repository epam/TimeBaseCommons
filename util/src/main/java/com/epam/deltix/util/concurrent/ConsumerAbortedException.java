package com.epam.deltix.util.concurrent;

/**
 * This exception indicates that attempted operation can't be performed because consumer in the "closed" state.
 * In general, this means that consumer can't be used anymore.
 */
public class ConsumerAbortedException extends RuntimeException {
    public ConsumerAbortedException(String message) {
        super(message);
    }

    public ConsumerAbortedException(String message, Throwable cause) {
        super(message, cause);
    }
}
