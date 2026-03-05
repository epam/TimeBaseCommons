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