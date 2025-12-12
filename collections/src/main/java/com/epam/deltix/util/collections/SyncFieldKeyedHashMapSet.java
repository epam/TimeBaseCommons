package com.epam.deltix.util.collections;

import java.lang.reflect.*;
import java.util.*;

/**
 *  Reflection-based HashMapSet, accessing a key field directly by synchronizing
 *  on the object.
 */
public class SyncFieldKeyedHashMapSet <K, V> extends HashMapSet <K, V> {
    private final Field                 field;
    
    public SyncFieldKeyedHashMapSet (Field field, boolean mustSync, Collection <V> copy) {
        super (copy);
        this.field = field;
        field.setAccessible (true);
    }

    public SyncFieldKeyedHashMapSet (Field field, boolean mustSync, int capacity) {
        super (capacity);
        this.field = field;
        field.setAccessible (true);
    }

    public SyncFieldKeyedHashMapSet (Field field, boolean mustSync) {
        this.field = field;
        field.setAccessible (true);
    }
       
    @Override @SuppressWarnings ("unchecked")
    public K                            getKey (V value) {
        try {
            synchronized (value) {
                return ((K) field.get (value));
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException (ex);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException (ex);
        }        
    }    
}
