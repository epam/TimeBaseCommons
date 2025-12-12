package com.epam.deltix.util.concurrent;

public interface IntermittentlyAvailableCursor extends AbstractCursor {

    public NextResult            nextIfAvailable  ();
}
