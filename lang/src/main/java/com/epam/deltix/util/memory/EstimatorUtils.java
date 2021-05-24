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

/**
 *
 */
public abstract class EstimatorUtils implements MemorySizeEstimator {
    public static int       getSizeInMemory (byte [] a) {
        return (a == null ? 0 : (7 + ARRAY_OVERHEAD + a.length) / 8 * 8);
    }

    public static int       getSizeInMemory (boolean [] a) {
        return (a == null ? 0 : (7 + ARRAY_OVERHEAD + a.length * SIZE_OF_BOOLEAN) / 8 * 8);
    }

    public static int       getSizeInMemory (short [] a) {
        return (a == null ? 0 : (7 + ARRAY_OVERHEAD + a.length * SIZE_OF_SHORT) / 8 * 8);
    }

    public static int       getSizeInMemory (char [] a) {
        return (a == null ? 0 : (7 + ARRAY_OVERHEAD + a.length * SIZE_OF_CHAR) / 8 * 8);
    }

    public static int       getSizeInMemory (int [] a) {
        return (a == null ? 0 : (7 + ARRAY_OVERHEAD + a.length * SIZE_OF_INT) / 8 * 8);
    }

    public static int       getSizeInMemory (long [] a) {
        return (a == null ? 0 : (7 + ARRAY_OVERHEAD + a.length * SIZE_OF_LONG) / 8 * 8);
    }

    public static int       getSizeInMemory (float [] a) {
        return (a == null ? 0 : (7 + ARRAY_OVERHEAD + a.length * SIZE_OF_FLOAT) / 8 * 8);
    }

    public static int       getSizeInMemory (double [] a) {
        return (a == null ? 0 : (7 + ARRAY_OVERHEAD + a.length * SIZE_OF_DOUBLE) / 8 * 8);
    }

    public static long       getSizeInMemory (MemorySizeEstimator obj) {
        return (obj == null ? 0 : obj.getSizeInMemory ());
    }

    /**
     *  Conservative estimate of string size - assumes the string does not
     *  share its value with any other string.
     */
    public static int       getSizeInMemory (String s) {
        if (s == null)
            return SIZE_OF_POINTER;

        if (s == s.intern())
            return SIZE_OF_POINTER;

        return OBJECT_OVERHEAD + SIZE_OF_POINTER + 3 * SIZE_OF_INT +   // 3 = sizeof int fields {substring.from + substring.length + hashcode }
               ARRAY_OVERHEAD + s.length () * SIZE_OF_CHAR;
    }

    public static long       getSizeInMemory (Object obj) {
        if (obj == null)
            return (0);
        else if (obj instanceof double[])
            return (getSizeInMemory ((double[]) obj));
        else if (obj instanceof byte[])
            return (getSizeInMemory ((byte[]) obj));
        else if (obj instanceof Object[])
            return (getSizeInMemory ((Object[]) obj));
        else if (obj instanceof MemorySizeEstimator)
            return (getSizeInMemory ((MemorySizeEstimator) obj));
        else if (obj instanceof String)
            return (getSizeInMemory ((String) obj));
        else if (obj instanceof float[])
            return (getSizeInMemory ((float[]) obj));
        else if (obj instanceof short[])
            return (getSizeInMemory ((short[]) obj));
        else if (obj instanceof int[])
            return (getSizeInMemory ((int []) obj));
        else if (obj instanceof long[])
            return (getSizeInMemory ((long[]) obj));

        return (OBJECT_OVERHEAD);
     }

     public static long       getSizeInMemory (Object [] a) {
        if (a == null)
            return (0);

        int             used = (7 + ARRAY_OVERHEAD + a.length * SIZE_OF_POINTER) / 8 * 8;

        for (int ii = 0; ii < a.length; ii++)
            used += getSizeInMemory (a [ii]);

        return (used);
    }

}