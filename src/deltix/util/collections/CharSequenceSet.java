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

    public boolean              addCharSequence (CharSequence e) {
        if (containsCharSequence (e))
            return (false);
        
        return (add (e.toString ()));
    }
    
    public boolean              removeCharSequence (CharSequence key) {
        mBuffer.set (key);
        return (removeCharSequence (mBuffer));
    }
    
    public final boolean        containsCharSequence (CharSequence key) {
        mBuffer.set (key);   
        return (contains (mBuffer));
    }

    public final boolean        containsCharSequence (CharSequence key, int start, int end) {
        mBuffer.set (key, start, end);   
        return (contains (mBuffer));
    }           
}
