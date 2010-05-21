package deltix.util.lang;

/**
 * Description: deltix.util.lang.Transformer
 * Date: May 20, 2010
 *
 * @author Nickolay Dul
 */
public interface Transformer<R, V> {
    R transform(V value);
}
