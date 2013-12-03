package deltix.util.collections.hash;

/**
 *
 */
public class BXHashCodeComputer extends HashCodeComputer {
    public static final HashCodeComputer     INSTANCE = new BXHashCodeComputer ();
    
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
}
