package com.epam.deltix.timebase.messages;

/**
 *
 */
public interface TimeStampedMessage {

    public final long    TIMESTAMP_UNKNOWN = Long.MIN_VALUE;
    public final long    INT64_NULL = Long.MIN_VALUE;

    /**
     * Gets message time in milliseconds that passed since January 1, 1970, 00:00:00 GMT
     * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this message.
     */
    public long             getTimeStampMs();

    /**
     * Gets message time in nanoseconds that passed since January 1, 1970, 00:00:00 GMT
     * @return the number of nanoseconds since January 1, 1970, 00:00:00 GMT represented by this message.
     */
    public long             getNanoTime();
}

