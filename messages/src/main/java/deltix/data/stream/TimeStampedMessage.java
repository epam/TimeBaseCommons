package deltix.data.stream;

/**
 *
 */
public interface TimeStampedMessage {
    public static final long    TIMESTAMP_UNKNOWN = Long.MIN_VALUE;
    public static final long    INT64_NULL = Long.MIN_VALUE;

    /** @return Message timestamp. Time is measured in milliseconds that passed since January 1, 1970 UTC. */
    public long             getTimeStampMs();

    /**
     *  @return Message timestamp in nanoseconds.
     *  Time is measured in nanosecond that passed since January 1, 1970 UTC.
     */
    public long             getNanoTime();
}
