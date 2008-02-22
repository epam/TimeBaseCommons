package deltix.util;

/**
 *  Abstract filter.
 */
public interface Filter <T> {
    public boolean          accept (T value);
}
