package com.epam.deltix.util.lang;

/**
 * Abstract transformer.
 */
@FunctionalInterface
public interface Transformer<R, V> {
    R transform(V value);
}
