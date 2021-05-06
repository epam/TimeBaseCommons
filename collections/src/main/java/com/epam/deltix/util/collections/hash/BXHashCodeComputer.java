package com.epam.deltix.util.collections.hash;

public class BXHashCodeComputer extends HashCodeComputer {
    public static final HashCodeComputer     INSTANCE = new BXHashCodeComputer ();

    private static final long serialVersionUID = 1L;

    protected BXHashCodeComputer () { }
    
    @Override
    public int              modHashCode (int key, int mod) {
        if ((mod >>> 16) == 0) {
            key = key ^ (key >> 16);
        
            if ((mod >>> 24) == 0)
                key = key ^ (key >> 8);
        }
        
        return (super.modHashCode (key, mod));        
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
