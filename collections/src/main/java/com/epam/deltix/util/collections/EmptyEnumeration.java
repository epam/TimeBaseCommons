package com.epam.deltix.util.collections;

import java.util.Enumeration;

/**
 *
 */
public class EmptyEnumeration <T> implements Enumeration <T> {
    public boolean      hasMoreElements () {
        return (false);
    }

    public T            nextElement () {
        throw new IllegalStateException ("Empty");
    }

    public EmptyEnumeration () { 
    }        
}
