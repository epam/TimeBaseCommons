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
public class Dump {
    public static void            dump (byte [] bytes, int offset, int length) {
        for (int ii = 0; ii < length; ii++) {
            if ((ii & 15) == 0)
                System.out.printf ("%04X: ", ii);
            
            System.out.printf ("%02X ", bytes [offset + ii]);
            
            if ((ii & 15) == 15)
                System.out.println ();
        }
        
        System.out.println ();
    }
    
    public static void            dump (long base, byte [] bytes, int offset, int length) {
        for (int ii = 0; ii < length; ii++) {
            if ((ii & 15) == 0)
                System.out.printf ("%020d: ", base + ii);
            
            System.out.printf ("%02X ", bytes [offset + ii]);
            
            if ((ii & 15) == 15)
                System.out.println ();
        }
        
        System.out.println ();
    }    
    
}