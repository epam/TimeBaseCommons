package com.epam.deltix.util.collections;

import java.util.*;

/**
 *  A read-only set consisting of a single fixed element.
 */
public class SingleElementSet <T>
    extends AbstractSet <T> 
    implements Set <T> 
{
    private T           mElement;
    
    public SingleElementSet (T elem) {
        mElement = elem;
    }
    
    public boolean          contains (Object o) {
        return (mElement.equals (o));
    }

    public int              size () {
        return (1);
    }
    
    public boolean          isEmpty () {
        return (false);
    }
    
    @SuppressWarnings ("unchecked")
    public Iterator <T>     iterator () {
        return (new ArrayIterator <T> (mElement));
    }
    
    public boolean          remove (Object o) {
        throw new UnsupportedOperationException ();
    }

    public boolean          addAll (Collection<? extends T> c) {
        throw new UnsupportedOperationException ();
    }

    public boolean          removeAll (Collection<?> c) {
        throw new UnsupportedOperationException ();
    }

    public boolean          retainAll (Collection<?> c) {
        throw new UnsupportedOperationException ();
    }

    public boolean          add (T o) {
        throw new UnsupportedOperationException ();
    }

    public void             clear () {
        throw new UnsupportedOperationException ();
    }
}
