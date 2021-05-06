package com.epam.deltix.util.io;

import java.io.*;

/**
 *  Adapts java.io.RandomAccessFile to java.io.InputStream interface,
 *  allowing to use the RandomAccessFile from multiple adapters and
 *  multiple threads.
 *  Useful for buffering data being read from RandomAccessFiles.
 *  Override close () if necessary, default implementation does nothing!
 */
public class RandomAccessFileToInputStreamAdapterMT 
    extends InputStream 
    implements SeekCapable
{
    protected final RandomAccessFile    raf;
    protected final Object              lock;
    private long                        offset;
    private long                        mark = -1;

    public RandomAccessFileToInputStreamAdapterMT (
        RandomAccessFile                raf
    )
    {
        this (raf, 0);
    }
    
    public RandomAccessFileToInputStreamAdapterMT (
        RandomAccessFile                raf,
        long                            offset
    )
    {
        this (raf, raf, offset);
    }
    
    public RandomAccessFileToInputStreamAdapterMT (
        Object                          lock,
        RandomAccessFile                raf,
        long                            offset
    )
    {
        this.lock = lock;
        this.raf = raf;
        this.offset = offset;
    }

    public long                 getPosition () {
        return (offset);
    }

    public void                 seek (long position) {
        offset = position;
    }
    
    /**
     *  Override to impose additional limit on file length.
     *  Default implementation returns <tt>Long.MAX_VALUE</tt>.
     */
    protected long              additionalLimit () throws IOException {
        return (Long.MAX_VALUE);
    }

    @Override
    public int                  read (byte [] b, int off, int len)
        throws IOException
    {
        synchronized (lock) {
            long    dist = additionalLimit () - offset;

            if (dist < len) {
                if (dist <= 0)
                    return (-1);

                len = (int) dist;
            }

            raf.seek (offset);
            int     numRead = raf.read (b, off, len);
            offset += numRead;
            return (numRead);
        }
    }

    @Override
    public int                  read (byte [] b)
        throws IOException
    {
        return (read (b, 0, b.length));
    }

    public int                  read () throws IOException {
        synchronized (lock) {
            if (additionalLimit () <= offset)
                return (-1);

            raf.seek (offset);

            int     ret = raf.read ();

            if (ret >= 0)
                offset++;

            return (ret);
        }
    }

    @Override
    public void                 mark (int readlimit) {
        mark = offset;
    }

    @Override
    public void                 reset () throws IOException {
        if (mark < 0)
            throw new IOException ("mark () has not been called");

        offset = mark;
    }

    public long                 availableEx () throws IOException {
        long        n;

        synchronized (lock) {
            n = Math.min (additionalLimit (), raf.length ());
        }

        n -= offset;

        return (n);
    }

    @Override
    public int                  available () throws IOException {
        long        n = availableEx ();

        if (n > Integer.MAX_VALUE)
            n = Integer.MAX_VALUE;

        return ((int) n);
    }

    @Override
    public boolean              markSupported () {
        return (true);
    }

    @Override
    public long                 skip (long n) throws IOException {
        if (n <= 0)
            return (0);

        long            avail = availableEx ();

        if (n > avail)
            n = avail;

        offset += n;
        return (n);
    }

    public long getOffset () {
        return offset;
    }
}
