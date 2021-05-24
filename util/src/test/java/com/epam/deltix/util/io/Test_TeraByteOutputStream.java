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

import org.junit.*;
import static org.junit.Assert.*;

import org.junit.experimental.categories.Category;

/**
 *
 */
public class Test_TeraByteOutputStream {
    @Test
    public void     testSingleBytes () throws IOException {
        final int                   num = 7777;
        TeraByteOutputStream        tbos = new TeraByteOutputStream (num, 33);
        
        for (int ii = 0; ii < num; ii++)
            tbos.write (ii);
        
        assertEquals (num, tbos.size ());
                
        ByteArrayOutputStreamEx     bos = new ByteArrayOutputStreamEx ();
        
        tbos.writeTo (bos, 0, num);
        
        assertEquals (num, bos.size ());
        
        for (int ii = 0; ii < num; ii++)
            assertEquals ((byte) ii, bos.getInternalBuffer () [ii]);
        
        final int                   a = 267;
        final int                   n2 = 468;
        
        bos.reset ();
        
        tbos.writeTo (bos, a, n2);
        
        assertEquals (n2, bos.size ());
        
        for (int ii = 0; ii < n2; ii++)
            assertEquals ((byte) (ii + a), bos.getInternalBuffer () [ii]);
    }
    
    
}