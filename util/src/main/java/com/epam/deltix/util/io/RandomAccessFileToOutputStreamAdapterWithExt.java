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

import com.epam.deltix.util.lang.GrowthPolicy;
import java.io.*;

/**
 *
 */
public class RandomAccessFileToOutputStreamAdapterWithExt extends OutputStream {
    protected final RandomAccessFile            raf;
    public GrowthPolicy                         policy;
    private long                                length;
    private long                                offset;
        
    public RandomAccessFileToOutputStreamAdapterWithExt (
        RandomAccessFile        raf,
        GrowthPolicy            policy
    ) 
        throws IOException 
    {
        this.raf = raf;
        this.policy = policy;
        length = raf.length ();
        offset = raf.getFilePointer ();
    }

    private final void      makeRoom (int n) throws IOException {
        offset += n;
        
        if (offset > length) {
            length = policy.computeLength (length, offset);            
            assert length >= offset;
            
            raf.setLength (length);
        }
    }
    
    public void             seek (long offset) throws IOException {
        this.offset = offset;
        raf.seek (offset);
    }
        
    @Override
    public void             write (int b) throws IOException {
        makeRoom (1);
        raf.write (b);
    }

    /**
     *  Note: override to close RandomAccessFile, if desired. Default
     *  implementation only calls flush ().
     */
    @Override
    public void             close () throws IOException {
        flush ();
    }

    @Override
    public void             write (byte [] b, int off, int len) 
        throws IOException 
    {
        makeRoom (len);
        raf.write (b, off, len);
    }
    
    
}