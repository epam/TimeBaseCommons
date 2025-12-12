package com.epam.deltix.util.io;

import java.io.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 *  Counts bytes that pass through.
 */
public class ByteCountingOutputStream extends FilterOutputStream {
    private final AtomicLong    mNumBytesWritten = new AtomicLong();
    
    public ByteCountingOutputStream (OutputStream os) {
        super (os);
    }
    
    public void             reset () {
        mNumBytesWritten.set(0);
    }
    
    public void             setNumBytesWritten (long n) {
        mNumBytesWritten.set(n);
    }
    
    public long             getNumBytesWritten () {
        return (mNumBytesWritten.longValue());
    }
    
    public void             write (byte [] b) throws IOException {
        out.write (b);
        mNumBytesWritten.addAndGet(b.length);
    } 
    
    public void             write (byte [] b, int off, int len) throws IOException {
        out.write (b, off, len);
        mNumBytesWritten.addAndGet(len);
    } 
    
    public void             write (int b) throws IOException {
        out.write (b);
        mNumBytesWritten.incrementAndGet();
    } 
}
