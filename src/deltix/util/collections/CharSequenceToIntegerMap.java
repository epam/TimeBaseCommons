package deltix.util.collections;

import deltix.util.collections.generated.ObjectHashMapBase.KeyNotFoundException;
import deltix.util.collections.generated.ObjectToIntegerHashMap;

/**
 *  
 */
public class CharSequenceToIntegerMap extends ObjectToIntegerHashMap <CharSequence> {
    private CharSubSequence     mBuffer = new CharSubSequence ();
    
    public CharSequenceToIntegerMap (int initialCapacity) {
        super (initialCapacity);
    }
    
    public CharSequenceToIntegerMap () {
        super ();
    }

    //  The following two overrides make all other methods work:
    @Override
    protected void          putKey (CharSequence key, int pos, boolean wasNotFound) {
        super.putKey (key.toString (), pos, wasNotFound);
    }

    @Override
    protected int           find (CharSequence key) {
        if (mBuffer != key)     // This check is critical for preserving range!
            mBuffer.set (key);
        
        return (super.find (mBuffer));
    }

    //  The following methods are useful for working with sub-sequences.
    public final int        get (CharSequence key, int start, int end)
        throws KeyNotFoundException
    {
        mBuffer.set (key, start, end);        
        return (get (mBuffer));
    }
    
    public final int        get (CharSequence key, int start, int end, int notFoundValue) {
        mBuffer.set (key, start, end);
        return (get (mBuffer, notFoundValue));
    }

    public boolean          put (CharSequence key, int start, int end, int value) {
        mBuffer.set (key, start, end);
        return (put (mBuffer, value));
    }
    
    public boolean          containsKey (CharSequence key, int start, int end) {
        mBuffer.set (key, start, end);
        return (containsKey (mBuffer));
    }

    public int              remove (CharSequence key, int start, int end, int notFoundValue) {
        mBuffer.set (key, start, end);
        return remove (mBuffer, notFoundValue);
    }

    public int              remove (CharSequence key, int start, int end)
        throws KeyNotFoundException
    {
        mBuffer.set (key, start, end);
        return remove (mBuffer);
    }
}
