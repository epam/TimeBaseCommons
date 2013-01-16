package deltix.util.collections;

import deltix.util.concurrent.AbstractCursor;
import java.util.*;

/**
 *  Allows retrieval of the same element several times (without calling next ()).
 */
public class CursorFromIterator <T> implements AbstractCursor {
    private final Iterator <T>      iter;
    private T                       value;
    private boolean                 hasNext;
    
    public CursorFromIterator (Iterator <T> iter) {
        this.iter = iter;
    }
    
    public CursorFromIterator (Iterable <T> iterable) {
        this.iter = iterable.iterator ();
    }

    @Override
    public boolean              isAtEnd () {
        return (!hasNext);
    }

    @Override
    public boolean              next () {        
        hasNext = iter.hasNext ();
        value = hasNext ? iter.next () : null;
        
        return (hasNext);
    }

    @Override
    public void                 close () {        
    }

    public T                    get () {
        return (value);
    }
}
