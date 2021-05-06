package com.epam.deltix.util.collections;

/**
 *  A mutable integer set.
 */
public interface MutableIntegerSet extends ReadOnlyIntegerSet {
    public void             clear ();
    
    public boolean          remove (int value);
    
    public boolean          add (int value);
}
