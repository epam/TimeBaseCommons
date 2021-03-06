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
package com.epam.deltix.util.text;

import java.util.Arrays;

/** CharSequence that contains zero-padded counter */
public class CharSequenceCounter implements CharSequence {
    public static final int WIDTH = 20;
    private final byte [] buffer;
    private final int offset;


    /** Constructs counter start starts with "00000000000000000000" */
    public CharSequenceCounter () {
        buffer = new byte [WIDTH];
        offset = 0;
        Arrays.fill(buffer, (byte)'0');
    }

    /** Constructs counter start starts given value (padded with zeros) */
    public CharSequenceCounter (long initialValue) {
        this();

        byte[] valueBytes = Long.toString(initialValue).getBytes();
        System.arraycopy(valueBytes, 0, buffer, WIDTH - valueBytes.length, valueBytes.length);
    }

    /** Constructs counter that is mapped to external buffer. Counter starts at zero. */
    public CharSequenceCounter (byte [] buffer, int offset) {
        if (buffer.length - offset < WIDTH)
            throw new IllegalArgumentException();

        this.buffer = buffer;
        this.offset = offset;
        Arrays.fill(this.buffer, offset, offset+WIDTH, (byte)'0');
    }


    public void increment() {
        int i = WIDTH - 1 + offset;
        while (true) {
            byte c = buffer[i];
            // increment character at index i
            if (c < '9') {
                buffer[i] = (byte) (c + 1);
                break;
            } else {
                buffer[i] = '0';
            }
            if (--i == 0)
                throw new ArithmeticException("Overflow");
        }
    }


    @Override
    public int length() {
        return WIDTH;
    }

    @Override
    public char charAt(int index) {
        return (char) buffer[index + offset];
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return new String (buffer, offset, WIDTH);
    }
}