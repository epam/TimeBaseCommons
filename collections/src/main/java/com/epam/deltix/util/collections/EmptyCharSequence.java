package com.epam.deltix.util.collections;

/**
 *
 */
public class EmptyCharSequence implements CharSequence {
    public char             charAt (int index) {
        throw new IndexOutOfBoundsException ();
    }

    public CharSequence     subSequence (int inStart, int inEnd) {
        if (inStart == inEnd)
            return ("");
        else
            throw new IndexOutOfBoundsException ();
    }

    public int              length () {
        return (0);
    }

    public String           toString () {
        return ("");
    }     
    
    private EmptyCharSequence () { }
    
    public static final EmptyCharSequence   INSTANCE = new EmptyCharSequence ();
}
