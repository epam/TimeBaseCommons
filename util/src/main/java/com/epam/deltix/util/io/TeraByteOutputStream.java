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
import java.util.Arrays;

/**
 *
 */
public class TeraByteOutputStream extends OutputStream {
    private static final int        MB = 1 << 20;
    private static final int        GB = 1 << 30;
    
    private final int               bufferSize;
    private final long              maxTotalSize;
    private byte []                 curBuffer;
    private int                     curCount = 0;
    private int                     avail = 0;
    private byte [][]               buffers;
    private int                     numBuffers = 0;
    
    public TeraByteOutputStream () {
        this (128L * GB);
    }
    
    public TeraByteOutputStream (long maxTotalSize) {
        this (maxTotalSize, 16 * MB);
    }
    
    public TeraByteOutputStream (long maxTotalSize, int bufferSize) {
        this.maxTotalSize = maxTotalSize;
        this.bufferSize = bufferSize;
        this.buffers = new byte [(int) ((maxTotalSize + bufferSize - 1) / bufferSize)][];
        
        alloc ();
    }
    
    public void         reset () {
        Arrays.fill (buffers, 1, numBuffers, null);
        numBuffers = 1;
        curBuffer = buffers [0];
        curCount = 0;
        avail = bufferSize;
    }

    private void        alloc () {
        curBuffer = new byte [bufferSize];
        curCount = 0;
        avail = bufferSize;
        buffers [numBuffers++] = curBuffer;
    }
    
    @Override
    public void         write (int b) throws IOException {
        if (avail == 0)
            alloc ();
                
        curBuffer [curCount++] = (byte) b;
        avail--;
    }

    @Override
    public void         write (byte [] b, int off, int len) throws IOException {
        for (;;) {
            boolean     fit = len <= avail;            
            int         num = fit ? len : avail;
            
            System.arraycopy (b, off, curBuffer, curCount, num);
            
            if (fit) {
                avail -= num;
                curCount += num;
                break;
            }
            
            off += num;
            len -= num;            
            
            alloc ();
        }                
    }

    @Override
    public void         close () throws IOException {
        super.close ();
    }

    public long         size () {
        return (numBuffers * ((long) bufferSize) - avail);
    }
    
    public void         setUpForReading (
        ByteArrayInputStreamEx  is, 
        long                    offset, 
        int                     length
    )
    {
        setUpForReading (is, offset, length, null);
    }
    
    public void         setUpForReading (
        ByteArrayInputStreamEx  is, 
        long                    offset, 
        int                     length,
        byte []                 buffer
    )
    {
        int                 startOffset = (int) (offset % bufferSize);
        int                 endOffset = startOffset + length;
        
        //  Check for >= 0 is necessary to handle overflow.
        if (endOffset >= 0 && endOffset <= bufferSize) {
            is.setBuffer (buffers [(int) (offset / bufferSize)]);
            is.setPosition (startOffset);
            is.setLimit (endOffset);
        }
        else {
            if (buffer == null || buffer.length < length)
                buffer = new byte [length];
            
            read (offset, buffer, 0, length);
            
            is.setBuffer (buffer);
            is.setPosition (0);
            is.setLimit (length);
        }        
    }
    
    public void         read (long offset, byte [] dest, int destOffset, int length) {
        int                 bufferIdx = (int) (offset / bufferSize);
        int                 bufferOffset = (int) (offset % bufferSize);
        int                 bufAvail = bufferSize - bufferOffset;
                
        for (;;) {
            boolean         fit = length <= bufAvail;
            int             num = fit ? length : bufAvail;
            
            System.arraycopy (buffers [bufferIdx], bufferOffset, dest, destOffset, num);
            
            if (fit)
                break;
            
            length -= num;
            destOffset += num;
            bufferIdx++;
            bufferOffset = 0;
            bufAvail = bufferSize;
        }
    }
    
    public void         writeTo (OutputStream os, long offset, int length) 
        throws IOException 
    {
        int                 bufferIdx = (int) (offset / bufferSize);
        int                 bufferOffset = (int) (offset % bufferSize);
        int                 bufAvail = bufferSize - bufferOffset;
                
        for (;;) {
            boolean         fit = length <= bufAvail;
            int             num = fit ? length : bufAvail;
            
            os.write (buffers [bufferIdx], bufferOffset, num);
            
            if (fit)
                break;
            
            length -= num;
            bufferIdx++;
            bufferOffset = 0;
            bufAvail = bufferSize;
        }
    }
}