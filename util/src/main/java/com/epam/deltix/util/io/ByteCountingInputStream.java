package com.epam.deltix.util.io;

import java.io.*;

/**
 *
 */
public class ByteCountingInputStream extends FilterInputStream {
    private long                    mNumBytesRead = 0;
    private long                    mMark = 0;
    
    public ByteCountingInputStream (InputStream delegate) {
        super (delegate);
    }

    public long                 getNumBytesRead () {
        return (mNumBytesRead);
    }
     
    public void                 setNumBytesRead (long num) {
        mNumBytesRead = num;
        numBytesChanged ();
    }
    
    public void                 numBytesChanged () {        
    }
    
    @Override
    public void                 mark (int readlimit) {
        super.mark (readlimit);
        mMark = mNumBytesRead;
    }

    @Override
    public int                  read () throws IOException {
        int             b = super.read ();
        
        if (b >= 0) {
            mNumBytesRead++;
            numBytesChanged ();
        }
        
        return (b);
    }

    @Override
    public int                  read (byte[] b, int off, int len) throws IOException {
        int             n = super.read (b, off, len);
        
        if (n > 0) {
            mNumBytesRead += n;
            numBytesChanged ();
        }
        
        return (n);
    }

    @Override
    public void                 reset () throws IOException {
        super.reset ();
        
        if (mNumBytesRead != mMark) {
            mNumBytesRead = mMark;
            numBytesChanged ();
        }
    }

    @Override
    public long                 skip (long n) throws IOException {
        long            skipped = super.skip (n);
        
        if (skipped > 0) {
            mNumBytesRead += skipped;
            numBytesChanged ();
        }
        
        return (skipped);
    }
    
    
}
