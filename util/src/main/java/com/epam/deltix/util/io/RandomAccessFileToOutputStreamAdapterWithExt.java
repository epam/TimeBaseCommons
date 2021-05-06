package com.epam.deltix.util.io;

import com.epam.deltix.util.lang.GrowthPolicy;
import java.io.*;

/**
 *
 */
public class RandomAccessFileToOutputStreamAdapterWithExt extends OutputStream {
    protected final RandomAccessFile            raf;
    public GrowthPolicy                         policy;
    private long                                length;
    private long                                offset;
        
    public RandomAccessFileToOutputStreamAdapterWithExt (
        RandomAccessFile        raf,
        GrowthPolicy            policy
    ) 
        throws IOException 
    {
        this.raf = raf;
        this.policy = policy;
        length = raf.length ();
        offset = raf.getFilePointer ();
    }

    private final void      makeRoom (int n) throws IOException {
        offset += n;
        
        if (offset > length) {
            length = policy.computeLength (length, offset);            
            assert length >= offset;
            
            raf.setLength (length);
        }
    }
    
    public void             seek (long offset) throws IOException {
        this.offset = offset;
        raf.seek (offset);
    }
        
    @Override
    public void             write (int b) throws IOException {
        makeRoom (1);
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
        makeRoom (len);
        raf.write (b, off, len);
    }
    
    
}
