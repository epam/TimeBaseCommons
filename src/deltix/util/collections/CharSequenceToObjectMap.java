package deltix.util.collections;

import java.util.*;

/**
 *  
 */
public class CharSequenceToObjectMap <T> extends HashMap <String, T> {
    private CharSubSequence     mBuffer = new CharSubSequence ();
    
    public CharSequenceToObjectMap (int initialCapacity, float loadFactor) {
        super (initialCapacity, loadFactor);
    }
    
    public CharSequenceToObjectMap (int initialCapacity) {
        super (initialCapacity);
    }
    
    public CharSequenceToObjectMap () {
        super ();
    }
    
    @Override
    public final T              get (Object key) {
        return (get ((CharSequence) mBuffer));
    }

    public final T              get (CharSequence key) {
        mBuffer.set (key);        
        return (super.get (mBuffer));
    }
    
    public final T              get (CharSequence key, int start, int end) {
        mBuffer.set (key, start, end);        
        return (super.get (mBuffer));
    }
    
    public T                    put (CharSequence key, T value) {
        return super.put (key.toString (), value);
    }        
}
