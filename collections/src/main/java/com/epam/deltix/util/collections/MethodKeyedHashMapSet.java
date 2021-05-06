package com.epam.deltix.util.collections;

import java.lang.reflect.*;
import java.util.*;

/**
 *  Reflection-based HashMapSet, using a key getter method to extract
 *  keys from objects.
 */
public class MethodKeyedHashMapSet <K, V> extends HashMapSet <K, V> {
    private final Method                method;

    public MethodKeyedHashMapSet (Method method, Collection <V> copy) {
        super (copy);
        this.method = method;
        method.setAccessible (true);
    }

    public MethodKeyedHashMapSet (Method method, int capacity) {
        super (capacity);
        this.method = method;
        method.setAccessible (true);
    }

    public MethodKeyedHashMapSet (Method method) {
        this.method = method;
        method.setAccessible (true);
    }
       
    @Override @SuppressWarnings ("unchecked")
    public K                            getKey (V value) {
        try {
            return ((K) method.invoke (value));
        } catch (IllegalAccessException ex) {
            throw new RuntimeException (ex);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException (ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException (ex);
        }        
    }    
}
