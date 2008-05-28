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

    public CharSequenceSet (Set <String> ... unionMembers) {
        for (Set <String> s : unionMembers)
            addAll (s);
    }
    
    public boolean              addCharSequence (CharSequence e) {
        if (containsCharSequence (e))
            return (false);
        
        return (add (e.toString ()));
    }
    
    public boolean              addCharSequence (CharSequence key, int start, int end) {
        mBuffer.set (key, start, end);   
        
        if (contains (mBuffer))
            return (false);
        
        return (add (mBuffer.toString ()));
    }
    
    public boolean              removeCharSequence (CharSequence key) {
        mBuffer.set (key);
        return (removeCharSequence (mBuffer));
    }
    
    public boolean              removeCharSequence (CharSequence key, int start, int end) {
        mBuffer.set (key, start, end);
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
