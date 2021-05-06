package com.epam.deltix.util.collections.hash;

import java.io.Serializable;
import java.util.*;

public abstract class HashCodeComputer implements Serializable {
    protected HashCodeComputer () { }
    
    public int              modHashCode (Object key, int mod) {
        return (modHashCode (Objects.hashCode (key), mod));
    }

    public int              modHashCode (float key, int mod) {
        return (modHashCode (Float.floatToIntBits (key), mod));
    }

    public int              modHashCode (double key, int mod) {
        return (modHashCode (Double.doubleToLongBits (key), mod));
    }

    public int              modHashCode (long key, int mod) {
        return (modHashCode ((int) (key ^ (key >>> 32)), mod));
    }

    public int              modHashCode (int key, int mod) {
        return (computeModHashCode (key, mod));
    }
    
    public static int       computeModHashCode (int key, int mod) {
        if (key == Integer.MIN_VALUE)
            return (1);
        
        if (key < 0)
            key = -key;
        
        return (key % mod);        
    }
}
