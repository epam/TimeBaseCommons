/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.util.io;

import com.epam.deltix.util.collections.GapByteQueue;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;

/**
 *  Input stream regarding from an embedded queue. Read operations block
 *  for more data, or until the {@link #finish} method is called.
 */
public class GapQueueInputStream extends InputStream {
    private GapByteQueue            q;
    private boolean                 endOfQueue = false;
    private IOException             exception;

    public GapQueueInputStream(int capacity) {
        q = new GapByteQueue (capacity);
    }

    public GapQueueInputStream(GapByteQueue queue) {
        q = queue;
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

    public synchronized boolean     putData (byte [] data, int offset, int length, int position)
            throws IOException
    {
        if (endOfQueue)
            throw new EOQException("Finished");

        if (q == null)
            throw new EOFException ("Closed");

        if (q.write(data, offset, length, position)) {
            notify ();
            return true;
        }

        return false;
    }

    @Override
    public synchronized int         available () throws IOException {

        if (q == null)
            throw new EOFException ("Closed");

        int size = q.size();

        if (size == 0 && endOfQueue)
            throw new EOQException("Finished");        

        return size;
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

            if (q.size() > 0)
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

//    @Override
//    public synchronized long skip (long n) throws IOException {
//        long remaining = n;
//
//        int         size;
//
//        while (remaining > 0) {
//            if (exception != null)
//                throw exception;
//
//            if (q == null)
//                throw new EOFException ("Stream is closed");
//
//            size = q.size ();
//
//            if (size > 0) {
//                if (size > remaining)
//                    size = (int) remaining;
//
//                remaining -= q.skip(size);
//            }
//
//            if (endOfQueue)
//                return (-1);
//
//            waitUnchecked();
//        }
//
//        return (n - remaining);
//    }


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