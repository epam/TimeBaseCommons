package com.epam.deltix.util.io;

import java.io.*;

/**
 *
 */
public class RandomAccessFileToOutputStreamAdapter extends OutputStream {
    protected final RandomAccessFile            raf;

    public RandomAccessFileToOutputStreamAdapter (RandomAccessFile raf) {
        this.raf = raf;
    }

    public void             seek (long offset) throws IOException {
        raf.seek (offset);
    }
    
    @Override
    public void             write (int b) throws IOException {
        raf.write (b);
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
        raf.write (b, off, len);
    }
    
    
}
