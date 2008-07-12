package deltix.util.lang;

/**
 *  Abstract filter.
 */
public interface Filter <T> {
    public boolean          accept (T value);
}
