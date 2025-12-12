package com.epam.deltix.util.lang;

/**
 * Generic interface that allows to unwrap nested implementations.
 *
 * The following code shows how this interface can be used to 'un-wrap' chain of wrappers:
 * <pre>
 * TradingService service = ...;
 * while (service instanceof Wrapper&lt;TradingService&gt;) {
 *      service = ((Wrapper&lt;TradingService&gt;)service).getNestedInstance();
 * }
 *</pre>
 * You can use {@link Util#unwrap(Object)}.
 */
public interface Wrapper<T> {
    /** @return 'wrapped' instance, never null */
    T getNestedInstance();
}
