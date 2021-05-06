package com.epam.deltix.util.io;

import com.epam.deltix.util.collections.*;

import java.io.*;

/**
 *  Input stream regarding from an embedded queue. Read operations block
 *  for more data, or until the {@link #finish} method is called.
 */
public class ByteQueueInputStream extends InputStream {
    private ByteQueue               q;
    private boolean                 endOfQueue = false;
    private IOException             exception;
    
    public ByteQueueInputStream (int capacity) {
        q = new ByteQueue (capacity);
    }

    public ByteQueueInputStream(ByteQueue q) {
        this.q = q;
    }

    @Override
    public synchronized void        close () {
        q = null;
        notify ();
    }

    public synchronized void        finish () {        
        endOfQueue = true;
        notify ();
    }

    public synchronized void        putError (IOException exception) {
        this.exception = exception;
        notify ();
    }

    public synchronized void        putData (byte data)
            throws IOException
    {
        if (endOfQueue)
            throw new EOQException("Finished");

        if (q == null)
            throw new EOFException ("Closed");

        q.offer (data);
        notify ();
    }

    public synchronized void        putData (byte [] data, int offset, int length)
            throws IOException
    {
        if (endOfQueue)
            throw new EOQException("Finished");            
        
        if (q == null)
            throw new EOFException ("Closed");

        q.offer (data, offset, length);
        notify ();
    }

    @Override
    public synchronized int         available () throws IOException {

        if (q == null)
            throw new EOFException ("Closed");

        if (q.size() == 0 && endOfQueue)
            throw new EOQException("Finished");        

        return q.size();
    }

    protected void                  waitUnchecked () throws IOException {
        try {
            wait ();
        } catch (InterruptedException x) {
            throw new InterruptedIOException ();
        }
    }

    @Override
    public synchronized int         read () throws IOException {
        for (;;) {
            if (exception != null)
                throw exception;

            if (q == null)
                throw new EOFException ("Stream is closed");

            if (!q.isEmpty ())
                break;

            if (endOfQueue)
                return (-1);

            waitUnchecked ();
        }

        return (q.poll () & 0xFF);
    }

    public synchronized boolean     isClosed() {
        return q == null || endOfQueue;
    }

    @Override
    public synchronized int         read (byte [] b, int off, int len)
        throws IOException 
    {
        int         size;

        for (;;) {
            if (exception != null)
                throw exception;

            if (q == null)
                throw new EOFException ("Stream is closed");

            size = q.size ();

            if (size > 0)
                break;

            if (endOfQueue)
                return (-1);

            waitUnchecked ();
        }

        if (size > len)
            size = len;

        q.poll (b, off, size);
        return (size);
    }
}
