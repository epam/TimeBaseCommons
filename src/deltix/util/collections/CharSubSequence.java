package deltix.util.collections;

/**
 *
 */
public class CharSubSequence implements CharSequence {
    public CharSequence         delegate;
    public int                  start;
    public int                  end;

    public CharSubSequence () {
        delegate = null;
        start = -1;
        end = -1;
    }
    
    public CharSubSequence (CharSequence inDelegate) {
        delegate = inDelegate;
        start = -1;
        end = -1;
    }
    
    public CharSubSequence (CharSequence inDelegate, int inStart, int inEnd) {
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
}
