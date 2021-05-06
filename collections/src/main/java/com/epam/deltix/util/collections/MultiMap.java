package com.epam.deltix.util.collections;

import java.util.*;

/**
 *  Our version of MultiMap. Thanks to Sun, everyone has their own.
 */
public interface MultiMap <K, V> {
    public void                         clear ();

    public Collection <V>               remove (K key);

    public Collection <V>               put (K key, V value);
    
    public Collection <V>               get (K key);

    public boolean                      isEmpty();

    public int                          size();

    public Set<K>                       keySet();

    public Collection<Collection<V>>    values();
}
