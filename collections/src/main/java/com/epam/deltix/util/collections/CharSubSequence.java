package com.epam.deltix.util.collections;

import com.epam.deltix.util.lang.Util;

/**
 *  Provides implementation-independent equals/hashCode implementation
 *  for comparing instances of CharSequence interface purely based on their
 *  character content. Therefore, equal CharSequences are guaranteed to be equal.
 *  If an instance of CharSequenceKey is added to a Map, the caller is responsible
 *  for keeping the charSequence object immutable.
 */
public final class CharSubSequence implements CharSequence {
    public CharSequence         delegate;
    public int                  start;
    public int                  end;

    public CharSubSequence () {
        delegate = null;
        start = -1;
        end = -1;
    }
    
    public CharSubSequence (CharSequence inDelegate) {
        set (inDelegate);
    }
    
    public CharSubSequence (CharSequence inDelegate, int inStart, int inEnd) {
        set (inDelegate, inStart, inEnd);
    }
    
    public void                 set (CharSequence inDelegate) {
        delegate = inDelegate;
        start = 0;
        end = inDelegate.length ();
    }
    
    public void                 set (CharSequence inDelegate, int inStart, int inEnd) {
        delegate = inDelegate;
        start = inStart;
        end = inEnd;
    }
    
    public final char             charAt (int index) {
        return (delegate.charAt (start + index));
    }

    public final CharSequence     subSequence (int inStart, int inEnd) {
        return (delegate.subSequence (start + inStart, start + inEnd).toString ());
    }

    public final int              length () {
        return (end - start);
    }

    @Override
    public final String           toString () {
        return (delegate.subSequence (start, end).toString ());
    }        
    
    public final void             trimWhitespace () {
        while (start < end && Character.isWhitespace (delegate.charAt (start)))
            start++;

        while (start < end) {
            final int       prev = end - 1;

            if (!Character.isWhitespace (delegate.charAt (prev)))
                break;

            end = prev;
        }  
    }
    
    @Override
    public boolean                  equals (Object other) {
        return (
            this == other ||
            other instanceof CharSequence &&
                Util.equals (this, (CharSequence) other)
        );
    }
    
    @Override
    public int                      hashCode () {
        return (Util.hashCode (this));
    }    
}
