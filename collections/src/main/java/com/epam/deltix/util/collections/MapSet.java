package com.epam.deltix.util.collections;

import java.util.*;

/**
 *
 */
public interface MapSet <K, V> extends Set <V> {
    public V                    get (K key);
    
    public V                    removeKey (K key);
    
    public boolean              containsKey (K key);
    
    public boolean              containsKeyOf (V value);
    
    public Set <K>              keySet ();
    
    public void                 keyChanged (V value);
    
    public <T extends K> T []   keysToArray (T [] a);
}
