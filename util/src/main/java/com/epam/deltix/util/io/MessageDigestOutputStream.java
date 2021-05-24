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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * OutputStream which updates a MessageDigest 
 */
public class MessageDigestOutputStream extends OutputStream {
    public final MessageDigest          md;
    
    public MessageDigestOutputStream (MessageDigest md) {
        this.md = md;
    }
    
    public MessageDigestOutputStream (String algo) throws NoSuchAlgorithmException {
        this.md = MessageDigest.getInstance (algo);
    }
    
    public MessageDigestOutputStream () {
        try {
            this.md = MessageDigest.getInstance ("SHA-256");
        } catch (NoSuchAlgorithmException x) {
            throw new RuntimeException ("No SHA-256??", x);
        }
    }
    
    @Override
    public void     write (int b) throws IOException {
        md.update ((byte) b);
    }

    @Override
    public void     write (byte[] b, int off, int len) throws IOException {
        md.update (b, off, len);
    }        
}