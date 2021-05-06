package com.epam.deltix.util.collections;

/**
 *  A generic visitor.
 */
public interface Visitor <T> {
    /**
     *  Visits the specified object.
     *
     *  @return     Whether iteration should continue
     */
    public boolean      visit (T object);
}
