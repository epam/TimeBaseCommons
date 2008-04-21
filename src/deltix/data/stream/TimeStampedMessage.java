package deltix.data.stream;

/**
 *
 */
public interface TimeStampedMessage {
    public static final long    TIMESTAMP_UNKNOWN = Long.MIN_VALUE;
    
    public long             getTimeStampMs ();
}
