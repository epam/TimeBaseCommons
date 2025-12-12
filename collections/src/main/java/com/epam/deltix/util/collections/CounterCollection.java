package com.epam.deltix.util.collections;

import java.util.*;

/**
 *  Counts added elements, but does not keep track of them
 */
public class CounterCollection <T> implements Collection <T> {
    private int             mCount = 0;
    
    public boolean          add (T o) {
        mCount++;
        return (true);
    }

    public void             clear () {
        mCount = 0;
    }

    public boolean          isEmpty () {
        return (mCount == 0);
    }

    public int              size () {
        return (mCount);
    }

    public boolean          contains (Object o) {
        throw new UnsupportedOperationException ();
    }

    public boolean          remove (Object o) {
        mCount--;
        return (true);
    }

    public <T> T []         toArray (T[] a) {
        throw new UnsupportedOperationException ();
    }

    public boolean          addAll (Collection<? extends T> c) {
        mCount += c.size ();
        return (true);
    }

    public boolean          containsAll (Collection<?> c) {
        throw new UnsupportedOperationException ();
    }

    public boolean          removeAll (Collection<?> c) {
        throw new UnsupportedOperationException ();
    }

    public boolean          retainAll (Collection<?> c) {
        throw new UnsupportedOperationException ();
    }

    public Iterator<T>      iterator () {
        throw new UnsupportedOperationException ();
    }

    public Object[]         toArray () {
        throw new UnsupportedOperationException ();
    }    
}
