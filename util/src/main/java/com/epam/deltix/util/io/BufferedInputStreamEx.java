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

import com.epam.deltix.util.lang.Bits;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * {@link BufferedInputStream} populates it's buffer with initial data from provided buffer.
 */
public class BufferedInputStreamEx extends BufferedInputStream {
    /**
     * Use {@link #BufferedInputStreamEx(InputStream, byte[], int, int)} instead.
     */
    @Deprecated
    public BufferedInputStreamEx(InputStream in, byte[] buf, int size) {
        this(in, buf, size, 8192);
    }

    /**
     * @param size amount of data in the provided buffer, will be copied to the new buffer
     * @param minBufferSize minimum size for a new buffer
     */
    public BufferedInputStreamEx(InputStream in, byte[] buf, int size, int minBufferSize) {
        super(in, Bits.nextPowerOfTwo(Math.max(size, minBufferSize)));

        assert size <= this.buf.length;

        // Copy provided buffer to internal buffer
        System.arraycopy(buf, 0, this.buf, 0, size);
        this.count = size;
    }
}