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

import java.io.*;

public class FlushableOutputStream extends FilterOutputStream {

    private ByteArrayOutputStream   cache;
    private boolean                 canFlush = true;

    private final int               limit;

    /**
     * Creates a new buffered output stream to write data to the
     * specified underlying output stream.
     *
     * @param   out   the underlying output stream.
     */
    public FlushableOutputStream(OutputStream out) {
        this(out, 8192);
    }

    /**
     * Creates a new buffered output stream to write data to the
     * specified underlying output stream with the specified buffer
     * size.
     *
     * @param   out    the underlying output stream.
     * @param   size   the buffer size.
     * @exception IllegalArgumentException if size &lt;= 0.
     */
    public FlushableOutputStream(OutputStream out, int size) {
        super(out);

        if (size <= 0)
            throw new IllegalArgumentException("Buffer size <= 0");

        limit = size;
        cache = new ByteArrayOutputStream(size);
    }

    /** Flush the internal buffer */
    private void            flushInternal() throws IOException {
        if (cache.size() > 0) {
            cache.writeTo(out);
            cache.reset();
        }
    }

    public synchronized void flushBuffer() throws IOException {
        if (canFlush)
            flushInternal();
    }

    public synchronized void            enableFlushing() throws IOException {
        canFlush = true;

        if (cache.size() > limit)
            flushInternal();
    }

    public synchronized void            disableFlushing() {
        canFlush = false;
    }

    /**
     * Writes the specified byte to this buffered output stream.
     *
     * @param      b   the byte to be written.
     * @exception  IOException  if an I/O error occurs.
     */
    public synchronized void write(int b) throws IOException {
        if (cache.size() > limit && canFlush) {
            flushInternal();
        }

        cache.write(b);
    }

    /**
     * Writes <code>len</code> bytes from the specified byte array
     * starting at offset <code>off</code> to this buffered output stream.
     *
     * <p> Ordinarily this method stores bytes from the given array into this
     * stream's buffer, flushing the buffer to the underlying output stream as
     * needed.  If the requested length is at least as large as this stream's
     * buffer, however, then this method will flush the buffer and write the
     * bytes directly to the underlying output stream.  Thus redundant
     * <code>BufferedOutputStream</code>s will not copy data unnecessarily.
     *
     * @param      b     the data.
     * @param      off   the start offset in the data.
     * @param      len   the number of bytes to write.
     * @exception  IOException  if an I/O error occurs.
     */
    public synchronized void write(byte b[], int off, int len) throws IOException {

        if (!canFlush) {
            cache.write(b, off, len);
            return;
        }

        if (len >= limit) {
            /* If the request length exceeds the size of the output buffer,
               flush the output buffer and then write the data directly.
               In this way buffered streams will cascade harmlessly. */
            flushInternal();
            out.write(b, off, len);
            return;
        }

        if (len > limit - cache.size()) {
            flushInternal();
        }

        cache.write(b, off, len);
    }

    /**
     * Flushes this buffered output stream. This forces any buffered
     * output bytes to be written out to the underlying output stream.
     *
     * @exception  IOException  if an I/O error occurs.
     * @see        java.io.FilterOutputStream#out
     */
    public synchronized void flush() throws IOException {
        flushInternal();
        out.flush();
    }

}