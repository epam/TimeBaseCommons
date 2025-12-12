package com.epam.deltix.util.io;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * Counts bytes as they pass through this filter stream.
 */
public class CountingInputStream extends FilterInputStream {
    private long                    mNumBytesRead = 0;
    private long                    mNumBytesNotified = 0;
    private long                    mMark = 0;
    private int                     notifyThreshold;    

    public CountingInputStream(InputStream delegate, int notifyThreshold ) {
        super (delegate);
        this.notifyThreshold = notifyThreshold;
    }

    public long                 getNumBytesRead () {
        return (mNumBytesRead);
    }

    public void                 setNumBytesRead(long mNumBytesRead) {
        this.mNumBytesNotified = this.mNumBytesRead = mNumBytesRead;
    }

    private void                onDataRead(long bytes) {
        mNumBytesRead += bytes;

        long change = mNumBytesRead - mNumBytesNotified;

        if (change >= notifyThreshold && bytesRead(change))
            mNumBytesNotified = mNumBytesRead;        
    }

    protected boolean           bytesRead(long change) {
        return true;
    }    

    @Override
    public void                 mark (int readlimit) {
        super.mark (readlimit);
        mMark = mNumBytesRead;
    }

    @Override
    public int                  read () throws IOException {
        int             b = super.read ();

        if (b >= 0)
            onDataRead(1);
        else if (b == -1)
            onEOF();

        return (b);
    }

    @Override
    public int                  read (byte[] b, int off, int len) throws IOException {
        int             n = super.read (b, off, len);

        if (n > 0)
            onDataRead(n);
        else if (n == -1)
            onEOF();

        return (n);
    }

    @Override
    public int                  read(byte[] b) throws IOException {
         int             n = super.read (b);

        if (n > 0)
            onDataRead(n);
        else if (n == -1)
            onEOF();

        return (n);
    }

    protected void              onEOF() throws IOException {
       //throw new EOFException(); 
    }

    @Override
    public void                 reset () throws IOException {
        super.reset ();

        if (mNumBytesRead != mMark) {
            mNumBytesRead = mMark;
        }
    }

    @Override
    public long                 skip (long n) throws IOException {
        long            skipped = super.skip (n);

        if (skipped > 0)
            onDataRead(skipped);

        return (skipped);
    }
}
