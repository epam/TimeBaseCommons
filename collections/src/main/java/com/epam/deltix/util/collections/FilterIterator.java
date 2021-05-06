package com.epam.deltix.util.collections;

import com.epam.deltix.util.lang.Filter;
import java.util.Iterator;

/**
 *  Filtering iterator. Do not call remove() after hasNext().
 */
public final class FilterIterator <T> implements Iterator <T> {
    private enum State {
        UNKNOWN,
        BUFFERED,
        END
    }
    
    private final Iterator <T>          delegate;
    private final Filter <? super T>    filter;
    private State                       state = State.UNKNOWN;
    private T                           value;
    
    public FilterIterator (Iterator <T> delegate, Filter <? super T> filter) {
        this.delegate = delegate;
        this.filter = filter;
    }

    private void                        advance () {
        value = null;
                
        for (;;) {
            boolean     dhn = delegate.hasNext ();

            if (!dhn) {
                state = State.END;
                break;
            }

            if (filter == null || filter.accept (value)) {
                value = delegate.next ();
                state = State.BUFFERED;
                break;
            }                        
        } 
    }
    
    @Override
    public boolean                      hasNext () {
        if (state == State.UNKNOWN)
            advance ();
        
        return (state == State.BUFFERED);                        
    }

    @Override
    public T                            next () {
        if (state == State.UNKNOWN) 
            advance ();
        
        if (state == State.END)
            throw new IllegalStateException ("hasNext() returned false");
                   
        final T         ret = value;

        state = null;
        value = null;

        return (ret);        
    }

    @Override
    public void                         remove () {
        delegate.remove ();
        value = null;
    }        
}
