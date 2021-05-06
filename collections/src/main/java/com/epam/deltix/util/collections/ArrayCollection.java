package com.epam.deltix.util.collections;

import java.util.*;

/**
 *  Adapts array to Collection API.
 */
public class ArrayCollection <T> extends AbstractCollection <T> {
    private final T []        array;

    @SafeVarargs
    public ArrayCollection (T ... array) {
        this.array = array;
    }

    @Override
    public boolean          addAll (Collection<? extends T> c) {
        throw new UnsupportedOperationException ("Immutable.");
    }

    @Override
    public void             clear () {
        throw new UnsupportedOperationException ("Immutable.");
    }

    public Iterator <T>     iterator () {
        return (new ArrayIterator<T> (array));
    }

    @Override
    public boolean          remove (Object o) {
        throw new UnsupportedOperationException ("Immutable.");
    }

    @Override
    public boolean          removeAll (Collection <?> c) {
        throw new UnsupportedOperationException ("Immutable.");
    }

    @Override
    public boolean          retainAll (Collection<?> c) {
        throw new UnsupportedOperationException ("Immutable.");
    }

    public int              size () {
        return (array.length);
    }

    @Override
    public Object []        toArray () {
        return (array.clone ());
    }
}
