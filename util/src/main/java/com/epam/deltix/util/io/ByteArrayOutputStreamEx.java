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

/**
 *  Allows access to the internal buffer.
 */
public class ByteArrayOutputStreamEx extends ByteArrayOutputStream {
    /**
     * Creates a new byte array output stream. The buffer capacity is 
     * initially 32 bytes, though its size increases if necessary. 
     */
    public ByteArrayOutputStreamEx () {
    }

    /**
     * Creates a new byte array output stream, with a buffer capacity of 
     * the specified size, in bytes. 
     *
     * @param   size   the initial size.
     * @exception  IllegalArgumentException if size is negative.
     */
    public ByteArrayOutputStreamEx (int size) {    
        super (size);
    }
    
    public byte []          getInternalBuffer () {
        return (buf);
    }
    
    /**
     *  Resets the current position. Seeking forward will lead to arbitrary
     *  data being placed in the buffer.
     */
    public void             reset (int position) {    
        ensureCapacity (position);       
        count = position;
    }

    /**
     *  Make sure buffer capacity is at least the specified size.
     */
    public void             ensureCapacity (int n) {    
        if (n > buf.length) {
            byte newbuf [] = new byte [Math.max (buf.length << 1, n)];
            System.arraycopy (buf, 0, newbuf, 0, count);
            buf = newbuf;
        }        
    }

    public synchronized int capacity() {
        return buf.length;
    }
    
    public InputStream      openInput () {
        return (new ByteArrayInputStreamEx (buf, 0, count));
    }
}