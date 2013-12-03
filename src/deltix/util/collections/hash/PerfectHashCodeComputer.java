package deltix.util.collections.hash;

/**
 *  Computer the best hash code, but much slower.
 */
public class PerfectHashCodeComputer extends HashCodeComputer {
    public static final HashCodeComputer     INSTANCE = new PerfectHashCodeComputer ();
    
    protected PerfectHashCodeComputer () { }
    
    @Override
    public int          modHashCode (int key, int mod) {
        int     hc = 0;
            
        while (key != 0) {
            hc += key;
            key = key / mod;
        }
                
        return (super.modHashCode (hc, mod));
    }   
}
