package deltix.data.stream;

/**
 *
 */
public interface TimeStampedMessage extends Message {
    public static final long    TIMESTAMP_UNKNOWN = Long.MIN_VALUE;
    
    public long             getTimeStampMs ();
}
