package com.epam.deltix.util.collections;

import com.epam.deltix.util.collections.generated.ObjectToIntegerHashMap;
import com.epam.deltix.util.collections.hash.*;
import com.epam.deltix.util.lang.*;

/**
 *  
 */
public class CharSequenceToIntegerMap extends ObjectToIntegerHashMap<CharSequence> {
    private final CharSubSequence     mBuffer = new CharSubSequence ();
    
    public CharSequenceToIntegerMap (int initialCapacity) {
        super (initialCapacity, StringHashCodeComputer.INSTANCE);
    }
    
    public CharSequenceToIntegerMap () {
        super (StringHashCodeComputer.INSTANCE);
    }

    //  The following 3 overrides make all other methods work:
    @Override
    protected void          putKey (int pos, CharSequence key) {
        super.putKey (pos, key.toString ());
    }

    @Override
    protected int           find (CharSequence key) {
        if (mBuffer != key)     // This check is critical for preserving range!
            mBuffer.set (key);
        
        return (super.find (mBuffer));
    }

    @Override
    protected boolean       keyEquals (CharSequence a, CharSequence b) {
        return (Util.equals (a, b));
    }
    
    public final int        get (CharSequence key, int start, int end, int notFoundValue) {
        mBuffer.set (key, start, end);
        return (super.get (mBuffer, notFoundValue));
    }

    public boolean          put (CharSequence key, int start, int end, int value) {
        mBuffer.set (key, start, end);
        return (super.put (mBuffer, value));
    }
    
    public boolean          containsKey (CharSequence key, int start, int end) {
        mBuffer.set (key, start, end);
        return (super.containsKey (mBuffer));
    }

    public int              remove (CharSequence key, int start, int end, int notFoundValue) {
        mBuffer.set (key, start, end);
        return super.remove (mBuffer, notFoundValue);
    }
}
