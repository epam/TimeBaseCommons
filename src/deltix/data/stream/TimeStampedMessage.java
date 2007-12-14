package deltix.data.stream;

/**
 *
 */
public interface TimeStampedMessage extends Message {
    public long             getTimeStampMs ();
}
