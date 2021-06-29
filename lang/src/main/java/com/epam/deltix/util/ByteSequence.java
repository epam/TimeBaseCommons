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


public interface ByteSequence extends CharSequence {

    byte byteAt(int index);

    Buffer buffer();

    default char charAt(int index) {
        return (char) byteAt(index);
    }

    default boolean equals(CharSequence sequence) {
        if(sequence == null)
            return false;

        int length = length();
        if (sequence.length() != length)
            return false;

        for (int i = 0; i < length; i++)
            if (byteAt(i) != sequence.charAt(i))
                return false;

        return true;
    }

    default boolean equals(ByteSequence sequence) {
        if(sequence == null)
            return false;

        int length = length();
        if (sequence.length() != length)
            return false;

        for (int i = 0; i < length; i++)
            if (byteAt(i) != sequence.byteAt(i))
                return false;

        return true;
    }

}