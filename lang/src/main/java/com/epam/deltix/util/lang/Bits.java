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
package com.epam.deltix.util.lang;

public final class Bits {

    private Bits() {
        throw new AssertionError("Not for you!");
    }

    public static int computeShift(int scale) {
        return Integer.numberOfTrailingZeros(scale);
    }

    public static int nextPowerOfTwo(int value) {
        return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
    }

    public static boolean isPowerOfTwo(int value) {
        return value > 0 && (value & (value - 1)) == 0;
    }

}