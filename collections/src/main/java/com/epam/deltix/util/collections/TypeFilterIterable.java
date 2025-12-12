package com.epam.deltix.util.collections;

import com.epam.deltix.util.lang.InstanceOfFilter;
import java.util.Iterator;

/**
 *  Iterable that filters by type
 */
public final class TypeFilterIterable <Generic, Specific> 
    implements Iterable <Specific> 
{
    private Iterable <Generic>      delegate;
    private InstanceOfFilter        filter;

    public TypeFilterIterable (Iterable <Generic> delegate, Class <Specific> cls) {
        this.delegate = delegate;
        this.filter = new InstanceOfFilter (cls);
    }
        
    @Override
    public Iterator <Specific>      iterator () {  
        return (new TypeFilterIterator <Generic, Specific> (delegate.iterator (), filter));
    }        
}
