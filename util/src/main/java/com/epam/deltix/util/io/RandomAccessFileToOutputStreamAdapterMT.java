package com.epam.deltix.util.io;

import java.io.*;

/**
 *  Adapts java.io.RandomAccessFile to java.io.OutputStream interface,
 *  allowing to use the RandomAccessFile from multiple adapters and
 *  multiple threads.
 *  Useful for buffering data being read from RandomAccessFiles.
 *  Override close () if necessary, default implementation does nothing!
 */
public class RandomAccessFileToOutputStreamAdapterMT extends OutputStream {
    protected final RandomAccessFile            raf;
    protected final Object                      lock;
    private long                                offset;
    
    public RandomAccessFileToOutputStreamAdapterMT (
        RandomAccessFile                raf
    )
    {
        this (raf, 0);
    }
    
    public RandomAccessFileToOutputStreamAdapterMT (
        RandomAccessFile                raf,
        long                            offset
    )
    {
        this (raf, raf, offset);
    }
    
    public RandomAccessFileToOutputStreamAdapterMT (
        Object                          lock,
        RandomAccessFile                raf,
        long                            offset
    )
    {
        this.lock = lock;
        this.raf = raf;
        this.offset = offset;
    }

    public long             getOffset () {
        return (offset);
    }
    
    public void             seek (long offset) {
        this.offset = offset;
    }
    
    @Override
    public void             write (int b) throws IOException {
        synchronized (lock) {
            raf.seek (offset);
            raf.write (b);
            offset++;
        }
    }

    /**
     *  Note: override to close RandomAccessFile, if desired. Default
     *  implementation only calls flush ().
     */
    @Override
    public void             close () throws IOException {
        flush ();
    }

    @Override
    public void             write (byte [] b, int off, int len) 
        throws IOException 
    {
        synchronized (lock) {
            raf.seek (offset);
            raf.write (b, off, len);
            offset += len;
        }
    }        
}
