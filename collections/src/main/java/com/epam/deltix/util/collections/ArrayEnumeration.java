package com.epam.deltix.util.collections;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public class ArrayEnumeration <T> implements Enumeration <T> {
    private Object[] mArray;
    private int mIdx;
    private int mEnd;
    private boolean mHasNext;

    public ArrayEnumeration (T [] array, int offset, int length) {
        mArray = array;
        mIdx = offset;
        mEnd = offset + length;
        mHasNext = mIdx < mEnd;
    }

    @SafeVarargs
    public ArrayEnumeration (T ... array) {
        this (array, 0, array == null ? 0 : array.length);
    }

    public boolean hasMoreElements() {
        return mHasNext;
    }

    @SuppressWarnings ("unchecked")
    public T nextElement() {
        if (mHasNext) {
            Object o = mArray[mIdx];
            mIdx++;
            mHasNext = (mIdx < mEnd);
            return (T) o;
        } 
        else
            throw new NoSuchElementException("No Elements left in Enumeration");
    }
}