package com.epam.deltix.util.collections;

import com.epam.deltix.util.lang.InstanceOfFilter;
import java.util.Iterator;

/**
 *  Filtering iterator. Do not call remove() after hasNext().
 */
public final class TypeFilterIterator <Generic, Specific> 
    implements Iterator <Specific> 
{    
    private final Iterator <Generic>    delegate;
    
    public TypeFilterIterator (Iterator <Generic> delegate, Class <Specific> cls) {
        this.delegate = new FilterIterator <Generic> (delegate, new InstanceOfFilter (cls));
    }

    public TypeFilterIterator (Iterator <Generic> delegate, InstanceOfFilter filter) {
        this.delegate = new FilterIterator <Generic> (delegate, filter);
    }

    @Override
    public boolean                      hasNext () {
        return (delegate.hasNext ());                        
    }

    @Override @SuppressWarnings ("unchecked")
    public Specific                     next () {       
        return ((Specific) delegate.next ());        
    }

    @Override
    public void                         remove () {
        delegate.remove ();
    }        
}
