package com.epam.deltix.util.collections;

import java.util.Iterator;

/**
 *  Adapts an iterator over one data type to an iterator over another data type,
 *  by applying user-defined transformation.
 */
public abstract class IteratorTransformer <X, Y> implements Iterator <Y> {
    private final Iterator <X>          delegate;

    public IteratorTransformer (Iterator <X> delegate) {
        this.delegate = delegate;
    }

    protected abstract Y            transform (X arg);
    
    @Override
    public final boolean            hasNext () {
        return (delegate.hasNext ());
    }

    @Override
    public final Y                  next () {
        return (transform (delegate.next ()));
    }

    @Override
    public final void               remove () {
        delegate.remove ();
    }        
}
