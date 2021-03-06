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
 *  Adapts java.io.RandomAccessFile to java.io.InputStream interface. 
 *  Useful for buffering data being read from RandomAccessFiles.
 *  Override close () if necessary, default implementation does nothing!
 */
public class RandomAccessFileToInputStreamAdapter 
    extends InputStream 
    implements SeekCapable
{
    private final RandomAccessFile      mIn;
    private long                        mMark = -1;
    
    public RandomAccessFileToInputStreamAdapter (RandomAccessFile in) {
        mIn = in;
    }
    
    public RandomAccessFileToInputStreamAdapter (File f) 
        throws FileNotFoundException 
    {
        this (new RandomAccessFile (f, "r"));
    }
    
    protected RandomAccessFile  randomAccessFile () {
        return (mIn);
    }

    public void                 seek (long position) throws IOException {
        mIn.seek (position);
    }
    
    public long                 getPosition () throws IOException {
        return (mIn.getFilePointer ());
    }
    
    @Override
    public int                  read (byte [] b, int off, int len) 
        throws IOException 
    {
        return (mIn.read (b, off, len));
    }

    @Override
    public int                  read (byte [] b) 
        throws IOException 
    {
        return (mIn.read (b));
    }

    public int                  read () throws IOException {
        return (mIn.read ());
    }

    @Override
    public void                 mark (int readlimit) {
        super.mark(readlimit);
    }

    @Override
    public int                  available () throws IOException {
        long        n = mIn.length () - mIn.getFilePointer ();
        
        if (n > Integer.MAX_VALUE)
            return (Integer.MAX_VALUE);
        
        return ((int) n);
    }

    @Override
    public void                 reset () throws IOException {
        if (mMark < 0)
            throw new IOException ("mark () has not been called");
        
        mIn.seek (mMark);
    }

    @Override
    public boolean              markSupported () {
        return (true);
    }

    @Override
    public long                 skip (long n) throws IOException {
        if (n <= 0)
            return (0);
        
        long            curPos = mIn.getFilePointer ();
        long            limit = mIn.length ();
        long            newPos = curPos + n;
        
        if (newPos > limit) {
            n = limit - curPos;
            newPos = limit;
        }
        
        mIn.seek (newPos);
        return (n);
    }
}