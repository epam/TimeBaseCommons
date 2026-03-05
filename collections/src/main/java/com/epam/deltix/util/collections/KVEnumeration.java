package com.epam.deltix.util.collections;

/**
 * Enumeration of key-value pairs. Used by maps to provide both key and value
 * from enumeration.
 * <p>
 * Important detail:
 * KeyEntry.getKey() is have to be called before call to ElementsEnumeration.nextElement() if you want to get correct key,
 * as {@link #nextElement()} will move enumeration to the next element and key.
 * <p>
 * Example use:
 * <pre>{@code
 *     while (kVEnumeration.hasMoreElements()) {
 *         String key = keyEnum.key();
 *         String value = enumeration.nextElement();
 *         // do something with key and value
 *     }}</pre>
 *
 */
public interface KVEnumeration<K, V> extends KeyEntry<K>, ElementsEnumeration<V> {
}
