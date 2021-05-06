package com.epam.deltix.util.io;

import java.io.IOException;

/**
 *  Streams capable of random access implement this interface.
 */
public interface SeekCapable {
    public void     seek (long position) throws IOException;
    
    public long     getPosition () throws IOException;
}
