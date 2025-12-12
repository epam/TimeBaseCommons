package com.epam.deltix.util.collections;

import java.util.*;

/**
 *
 */
public class EmptyIterator <T> implements Iterator <T>, Iterable <T> {   
    public static final EmptyIterator   INSTANCE = new EmptyIterator ();
    
    public EmptyIterator () {
    }
    
    @Override
    public void             remove () {
        throw new UnsupportedOperationException ();
    }

    @Override
    public T                next () {
        throw new UnsupportedOperationException ();
    }

    @Override
    public boolean          hasNext () {
        return (false);
    }

    @Override
    public Iterator <T>     iterator() {
        return this;
    }
}
