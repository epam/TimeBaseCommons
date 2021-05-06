package com.epam.deltix.streaming;

import com.epam.deltix.util.lang.Disposable;

/**
 *  Generic message consumer interface.
 */
public interface MessageChannel <T> extends Disposable {
    /**
     *  This method is invoked to send a message to the object.
     *
     *  @param msg  A temporary buffer with the message.
     *              By convention, the message is only valid for the duration
     *              of this call.
     */
    public void         send (T msg);
}

