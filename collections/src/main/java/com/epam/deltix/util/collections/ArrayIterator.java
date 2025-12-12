package com.epam.deltix.util.collections;

import java.util.*;

/**
 *
 */
public class ArrayIterator <T> implements Iterator <T>, Iterable <T> {
    private T []            mArray;
    private int             mIdx = 0;

    @SafeVarargs
    public ArrayIterator (T ... array) {
        mArray = array;
    }

    public void             reset(T[] array) {
        mArray = array;
        mIdx = 0;
    }
    
    @Override
    public void             remove () {
        throw new UnsupportedOperationException ();
    }

    @Override
    public T                next () {
        return (mArray [mIdx++]);
    }

    @Override
    public boolean          hasNext () {
        return (mIdx < mArray.length);
    }

    @Override
    public Iterator <T>    iterator() {
        return this;
    }
}
