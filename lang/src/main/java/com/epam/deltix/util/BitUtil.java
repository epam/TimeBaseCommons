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

public final class BitUtil {

    public static final int SIZE_OF_BYTE = 1;
    public static final int SIZE_OF_BOOLEAN = 1;
    public static final int SIZE_OF_CHAR = 2;
    public static final int SIZE_OF_SHORT = 2;
    public static final int SIZE_OF_INT = 4;
    public static final int SIZE_OF_FLOAT = 4;
    public static final int SIZE_OF_LONG = 8;
    public static final int SIZE_OF_DOUBLE = 8;

    public static int nextPowerOfTwo(int value) {
        return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
    }

    public static int align(int value, int alignment) {
        int mask = alignment - 1;
        return (value + mask) & ~mask;
    }

    public static boolean isEven(int value) {
        return (value & 1) == 0;
    }

    public static boolean isPowerOfTwo(int value) {
        return value > 0 && ((value & (~value + 1)) == value);
    }

    public static int calculateShift(int scale) {
        if (scale == 4)
            return 2;
        else if (scale == 8)
            return 3;

        throw new IllegalArgumentException("Unknown pointer size");
    }

}