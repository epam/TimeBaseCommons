package com.epam.deltix.util.concurrent;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Thread-safe version of Map. Suitable for scenarios when number of reads vastly outnumbers number of writes. Reads are lock-free.
 * Writes can synchronize on instance of this class to implement atomic operations. For example to escalate frequent locks.
 */
public final class VolatileCopyMap<K,V> {
    private final Map<K,V> writableMap = new HashMap<K,V>();
    private volatile Map<K,V> readOnlySnapshot = Collections.emptyMap();
    
    public V get (K key) {
        return readOnlySnapshot.get(key);
    }
    
    /**
     * Method does not allow overriding already associated value (first put() wins).
     * @return value associated with given key at the end of this method
     */
    public synchronized V put (K key, V value) {
        V collision = writableMap.put(key, value); // optimistic assumption: collisions will be rare
        if (collision != null) {
            writableMap.put(key, collision); // restore original value
            value = collision;
        }
        readOnlySnapshot = new HashMap<K,V>(writableMap);  // writes are super rare compared to number of reads
        return value;
    }

    /** Caller is not allowed to modify result */
    public Collection<K> keySet() {
        return readOnlySnapshot.keySet();
    }

    /** Caller is not allowed to modify result */
    public Collection<V> values() {
        return readOnlySnapshot.values();
    }
    
}
