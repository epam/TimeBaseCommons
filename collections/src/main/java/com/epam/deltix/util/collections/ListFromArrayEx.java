package com.epam.deltix.util.collections;

import java.util.*;

/**
 *  Adapts an array of one type to the List interface of another type.
 */
public abstract class ListFromArrayEx <E, AE> extends AbstractList <E> {
    private final AE []         array;
    private final int           offset;
    private final int           length;

    public ListFromArrayEx (AE [] array, int offset, int length) {
        this.array = array;
        this.offset = offset;
        this.length = length;
    }
        
    public ListFromArrayEx (AE [] array) {
        this (array, 0, array.length);
    }

    protected abstract E    convert (AE value);
    
    @Override
    public E                get (int index) {
        return (convert (array [offset + index]));
    }

    @Override
    public int              size () {
        return (length);
    }        
}
