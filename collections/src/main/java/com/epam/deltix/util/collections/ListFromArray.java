package com.epam.deltix.util.collections;

import java.util.*;

/**
 *  Adapts an array to the List interface.
 */
public class ListFromArray <T> extends AbstractList <T> {
    private final T []          array;
    private final int           offset;
    private final int           length;

    public ListFromArray (T [] array, int offset, int length) {
        this.array = array;
        this.offset = offset;
        this.length = length;
    }
        
    public ListFromArray (T [] array) {
        this (array, 0, array.length);
    }

    @Override
    public T            get (int index) {
        return (array [offset + index]);
    }

    @Override
    public int          size () {
        return (length);
    }        
}
