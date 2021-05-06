package com.epam.deltix.util.collections;

import java.io.IOException;

import com.epam.deltix.util.collections.generated.*;
import com.epam.deltix.util.collections.hash.*;
import com.epam.deltix.util.lang.*;

public class CharSequenceToObjectMapQuick <T> extends ObjectToObjectHashMap <CharSequence, T> {
    private transient CharSubSequence     mBuffer = new CharSubSequence ();
    
    public CharSequenceToObjectMapQuick (int initialCapacity) {
        super (initialCapacity, StringHashCodeComputer.INSTANCE);
    }
    
    public CharSequenceToObjectMapQuick () {
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
    
    public final T              get (CharSequence key, int start, int end, T notFoundValue) {
        mBuffer.set (key, start, end);        
        return (super.get (mBuffer, notFoundValue));
    }
    
    public T                    putAndGet (CharSequence key, int start, int end, T value, T notFoundValue) {
        mBuffer.set (key, start, end);  
        return super.putAndGet (mBuffer, value, notFoundValue);
    }

    public boolean              containsKey (CharSequence key, int start, int end) {
        mBuffer.set (key, start, end);
        return super.containsKey (mBuffer);
    }

    public T                    remove (CharSequence key, int start, int end, T notFoundValue) {
        mBuffer.set (key, start, end);
        return super.remove (mBuffer, notFoundValue);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        mBuffer = new CharSubSequence();
    }
}
