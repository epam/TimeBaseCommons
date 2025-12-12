package com.epam.deltix.util.collections.hash;

/**
 *
 */
public class SimpleHashCodeComputer extends HashCodeComputer {
    public static final SimpleHashCodeComputer     INSTANCE = new SimpleHashCodeComputer ();

    private static final long serialVersionUID = 1L;

    protected SimpleHashCodeComputer () { }

    private Object readResolve() {
        return INSTANCE;
    }
}
