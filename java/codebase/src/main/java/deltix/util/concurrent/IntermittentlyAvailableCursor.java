package deltix.util.concurrent;

import deltix.util.collections.AbstractCursor;

public interface IntermittentlyAvailableCursor extends AbstractCursor {

    public NextResult            nextIfAvailable  ();
}
