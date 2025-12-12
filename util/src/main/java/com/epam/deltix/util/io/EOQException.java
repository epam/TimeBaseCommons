package com.epam.deltix.util.io;

import java.io.EOFException;

/**
 * Signals end of queue.
 * 
 * @see ByteQueueInputStream
 */
public class EOQException extends EOFException {
    
    public EOQException() {
    }

    public EOQException(String s) {
        super(s);
    }
}
