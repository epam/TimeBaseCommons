package deltix.util.collections.hash;

import deltix.util.lang.*;

/**
 *
 */
public class StringHashCodeComputer extends HashCodeComputer {
    public static final StringHashCodeComputer     INSTANCE = new StringHashCodeComputer ();
    
    private StringHashCodeComputer () {        
    }
    
    @Override
    public int      modHashCode (Object key, int mod) {
        return computeModHashCode (Util.hashCode ((CharSequence) key), mod);
    }

}
