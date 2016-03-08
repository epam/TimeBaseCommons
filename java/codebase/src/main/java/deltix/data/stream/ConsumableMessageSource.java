package deltix.data.stream;

public interface ConsumableMessageSource<T> extends MessageSource<T> {

    /**
     * @return Floating point number in range [0..1] that indicate how much data has been consumed (0 means none, 1 means all).
     */
    public double       getProgress();
}
