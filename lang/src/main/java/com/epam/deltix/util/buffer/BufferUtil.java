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
package com.epam.deltix.util.buffer;

import com.epam.deltix.util.ByteSequenceWrapper;
import com.epam.deltix.util.StringUtil;

import java.nio.ByteBuffer;


public class BufferUtil {

    public static UnsafeBuffer fromString(String string) {
        return new UnsafeBuffer(StringUtil.asciiBytes(string));
    }

    public static String toString(Buffer buffer) {
        return new ByteSequenceWrapper(buffer).toString();
    }

    static void boundsCheck(byte[] buffer, long index, int length) {
        int capacity = buffer.length;
        long resultingPosition = index + (long) length;
        if (index < 0 || resultingPosition > capacity) {
            throw new IndexOutOfBoundsException(String.format("index=%d, length=%d, capacity=%d", index, length, capacity));
        }
    }

    static void boundsCheck(ByteBuffer buffer, long index, int length) {
        int capacity = buffer.capacity();
        long resultingPosition = index + (long) length;
        if (index < 0 || resultingPosition > capacity) {
            throw new IndexOutOfBoundsException(String.format("index=%d, length=%d, capacity=%d", index, length, capacity));
        }
    }

}