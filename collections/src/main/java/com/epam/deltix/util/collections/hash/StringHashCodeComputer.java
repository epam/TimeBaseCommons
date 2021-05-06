package com.epam.deltix.util.collections.hash;

import com.epam.deltix.util.lang.*;

public class StringHashCodeComputer extends HashCodeComputer {
    public static final StringHashCodeComputer     INSTANCE = new StringHashCodeComputer ();

    private static final long serialVersionUID = 1L;

    private StringHashCodeComputer () {        
    }
    
    @Override
    public int      modHashCode (Object key, int mod) {
        return computeModHashCode (Util.hashCode ((CharSequence) key), mod);
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
