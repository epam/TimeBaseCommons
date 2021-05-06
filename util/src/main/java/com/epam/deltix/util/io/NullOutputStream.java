package com.epam.deltix.util.io;

import java.io.OutputStream;

/**
 * Null output.
 */
public class NullOutputStream extends OutputStream {
    private NullOutputStream () { }
    
    public void write(int b) {}

    public void write(byte b[]) {}

    public void write(byte b[], int off, int len) {}

    public void flush() {}

    public void close()  {}
    
    public static final OutputStream    INSTANCE = new NullOutputStream ();
}
