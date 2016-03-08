package deltix.util.lang;

/**
 * Abstract transformer.
 */
public interface Transformer<R, V> {
    R transform(V value);
}
