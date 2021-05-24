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
package com.epam.deltix.util;

import com.epam.deltix.util.buffer.Buffer;
import com.epam.deltix.util.buffer.MutableBuffer;
import com.epam.deltix.util.buffer.UnsafeBuffer;


public class ByteSequenceWrapper implements ByteSequence {

    protected static final byte[] EMPTY = new byte[0];

    protected final MutableBuffer buffer = new UnsafeBuffer(EMPTY);

    public ByteSequenceWrapper() {
    }

    public ByteSequenceWrapper(Buffer buffer, int offset, int length) {
        wrap(buffer, offset, length);
    }

    public ByteSequenceWrapper(Buffer buffer) {
        wrap(buffer);
    }

    public ByteSequenceWrapper wrap(Buffer buffer) {
        this.buffer.wrap(buffer, 0, buffer.capacity());
        return this;
    }

    public ByteSequenceWrapper wrap(Buffer buffer, int offset, int length) {
        this.buffer.wrap(buffer, offset, length);
        return this;
    }

    public Buffer buffer() {
        return buffer;
    }

    @Override
    public int length() {
        return buffer.capacity();
    }

    @Override
    public byte byteAt(int index) {
        return buffer.getByte(index);
    }

    @Override
    public ByteSequence subSequence(int start, int end) {
        ByteSequenceWrapper sequence = new ByteSequenceWrapper();
        sequence.wrap(buffer, start, end - start);
        return sequence;
    }

    public ByteSequenceWrapper clear() {
        buffer.wrap(EMPTY);
        return this;
    }

    @Override
    public String toString() {
        return new StringBuilder(this).toString();
    }

}