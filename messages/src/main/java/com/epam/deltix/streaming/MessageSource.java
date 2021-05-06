package com.epam.deltix.streaming;

import com.epam.deltix.util.concurrent.AbstractCursor;

/**
 * Generic message source interface
 */
public interface MessageSource <T> extends AbstractCursor {

    /*
     * @return current message located by {@link AbstractCursor#next()} method call. Use {@link AbstractCursor#next()} to scroll cursor to the next message.
     */
    public T            getMessage ();
}
