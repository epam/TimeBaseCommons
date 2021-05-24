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
package com.epam.deltix.util.memory;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

import com.epam.deltix.util.lang.Bits;

public final class UnsafeAccess {

    public static final Unsafe UNSAFE;

    static {
        try {
            // This is a bit of voodoo to force the UNSAFE object into
            // visibility and acquire it.
            // This is not playing nice, but as an established back door it is
            // not likely to be
            // taken away.
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UnsafeAccess() {
        throw new AssertionError("Not for you!");
    }

    public static int arrayBaseOffset(Class clazz) {
        return UNSAFE.arrayBaseOffset(clazz);
    }

    public static int arrayIndexScale(Class clazz) {
        int scale = UNSAFE.arrayIndexScale(clazz);

        if (!Bits.isPowerOfTwo(scale))
            throw new Error("data type scale not a power of two: " + clazz);

        return scale;
    }

}