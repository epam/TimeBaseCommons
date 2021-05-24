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
import java.util.concurrent.atomic.AtomicLong;

/**
 *  Counts bytes that pass through.
 */
public class ByteCountingOutputStream extends FilterOutputStream {
    private final AtomicLong    mNumBytesWritten = new AtomicLong();
    
    public ByteCountingOutputStream (OutputStream os) {
        super (os);
    }
    
    public void             reset () {
        mNumBytesWritten.set(0);
    }
    
    public void             setNumBytesWritten (long n) {
        mNumBytesWritten.set(n);
    }
    
    public long             getNumBytesWritten () {
        return (mNumBytesWritten.longValue());
    }
    
    public void             write (byte [] b) throws IOException {
        out.write (b);
        mNumBytesWritten.addAndGet(b.length);
    } 
    
    public void             write (byte [] b, int off, int len) throws IOException {
        out.write (b, off, len);
        mNumBytesWritten.addAndGet(len);
    } 
    
    public void             write (int b) throws IOException {
        out.write (b);
        mNumBytesWritten.incrementAndGet();
    } 
}