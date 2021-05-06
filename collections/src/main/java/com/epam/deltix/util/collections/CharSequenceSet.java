package com.epam.deltix.util.collections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class CharSequenceSet extends HashSet <String> {
    private static final long serialVersionUID = 1L;
    private transient CharSubSequence     mBuffer = new CharSubSequence ();
    private transient ByteArrayToCharSequence mBuffer2 = new ByteArrayToCharSequence ();

    public CharSequenceSet (int initialCapacity, float loadFactor) {
        super (initialCapacity, loadFactor);
    }

    public CharSequenceSet (int initialCapacity) {
        super (initialCapacity);
    }

    public CharSequenceSet () {
        super ();
    }

    public CharSequenceSet (Set <String> unionMembers) {
        addAll (unionMembers);
    }

    public CharSequenceSet (Set <String> a, Set <String> b) {
        addAll (a);
        addAll (b);
    }

    @SuppressWarnings(value = {"unchecked", "varargs"})
    @SafeVarargs
    public CharSequenceSet (Set <String> ... unionMembers) {
        for (Set <String> s : unionMembers)
            addAll (s);
    }

    /**
     *  Create a new CharSequenceSet which is a union of the incoming sets.
     *  Incoming sets are allowed to be null, which is considered equivalent to 
     *  an empty set. This method returns null if and only if both incoming 
     *  arguments are null.
     */
    public static CharSequenceSet unionCopy (CharSequenceSet a, CharSequenceSet b) {
        if (a == null)
            if (b == null)
                return (null);
            else
                return (new CharSequenceSet (b));
        else
            if (b == null)
                return (new CharSequenceSet (a));
            else
                return (new CharSequenceSet (a, b));
    }

    /**
     *  Create a new CharSequenceSet which is a subtraction of the incoming sets.
     *  Incoming sets are not allowed to be null.
     */
    public static CharSequenceSet subtractionCopy(CharSequenceSet a, CharSequenceSet b) {
        if (b.isEmpty())
            return a != null ? new CharSequenceSet(a) : null;
        else {
            final CharSequenceSet result = new CharSequenceSet(a);
            for (String s : b) {
                result.removeCharSequence(s);
            }
            return result;
        }
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
        return (super.remove (mBuffer));
    }

    public boolean              removeCharSequence (CharSequence key, int start, int end) {
        mBuffer.set (key, start, end);
        return (super.remove (mBuffer));
    }

    public final boolean        containsCharSequence (CharSequence key) {
        mBuffer.set (key);
        return (super.contains (mBuffer));
    }

    public final boolean        containsCharSequence (CharSequence key, int start, int end) {
        mBuffer.set (key, start, end);
        return (super.contains (mBuffer));
    }

    public final boolean        containsCharSequence (byte [] key, int start, int end) {
        mBuffer2.set (key, start, end);
        return (super.contains (mBuffer2));
    }

    private void readObject (ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        mBuffer = new CharSubSequence ();
        mBuffer2 = new ByteArrayToCharSequence ();
    }
}
