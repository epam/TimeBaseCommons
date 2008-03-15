package deltix.util.collections;

import java.util.*;

/**
 *  
 */
public class CharSequenceSet extends HashSet <String> {
    private CharSubSequence     mBuffer = new CharSubSequence ();
    
    public CharSequenceSet (int initialCapacity, float loadFactor) {
        super (initialCapacity, loadFactor);
    }
    
    public CharSequenceSet (int initialCapacity) {
        super (initialCapacity);
    }
    
    public CharSequenceSet () {
        super ();
    }
    
    public final boolean        contains (CharSequence key) {
        mBuffer.set (key);   
        return (contains (mBuffer));
    }

    public final boolean        contains (CharSequence key, int start, int end) {
        mBuffer.set (key, start, end);   
        return (contains (mBuffer));
    }           
}
