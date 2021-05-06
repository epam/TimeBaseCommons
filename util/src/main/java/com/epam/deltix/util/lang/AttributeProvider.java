package com.epam.deltix.util.lang;

import java.util.Set;

/**
 * Attribute key/value holder.
 */
public interface AttributeProvider<T> {
    /**
     * Returns set of attribute keys, never <code>null</code>.
     * @return attribute keys.
     */
    Set<String> getAttributeKeys();

    /**
     * Returns target attribute by key or <code>null</code> if key does not exist.
     * @param key attribute key.
     * @return target attribute by key or <code>null</code> if key does not exist.
     */
    T getAttribute(String key);
}
