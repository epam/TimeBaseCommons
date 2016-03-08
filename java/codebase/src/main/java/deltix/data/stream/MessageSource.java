package deltix.data.stream;

import deltix.util.collections.AbstractCursor;

/**
 *
 */
public interface MessageSource <T> extends AbstractCursor {

    /*
    * @return current message located by {@link AbstractCursor#next()} method call. Use {@link AbstractCursor#next()} to scroll cursor to the next message.
    */
    public T            getMessage ();
}
