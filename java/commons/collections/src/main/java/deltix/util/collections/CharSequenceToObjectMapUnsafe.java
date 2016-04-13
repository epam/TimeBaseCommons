package deltix.util.collections;

import java.io.IOException;

import deltix.util.collections.generated.*;
import deltix.util.collections.hash.*;
import deltix.util.lang.*;

/**
 * Similar to CharSequenceToObjectMapQuick but relies on caller supplying immutable CharSequence for keys used for put operations.
 */
public class CharSequenceToObjectMapUnsafe <T> extends ObjectToObjectHashMap <CharSequence, T> {
    private transient CharSubSequence     mBuffer = new CharSubSequence ();

    public CharSequenceToObjectMapUnsafe (int initialCapacity) {
        super (initialCapacity, StringHashCodeComputer.INSTANCE);
    }

    public CharSequenceToObjectMapUnsafe () {
        super (StringHashCodeComputer.INSTANCE);
    }

    //  The following 3 overrides make all other methods work:

// This disabled override is the only difference from CharSequenceToObjectMapQuick
//    @Override
//    protected void          putKey (int pos, CharSequence key) {
//        super.putKey (pos, key.toString());  <== allocation
//    }

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
