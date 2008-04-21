package deltix.data.stream;

import deltix.util.concurrent.AbstractCursor;

/**
 *
 */
public interface MessageSource <T> extends AbstractCursor {
    public T            getMessage ();
}
