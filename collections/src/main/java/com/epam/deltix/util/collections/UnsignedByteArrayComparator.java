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
package com.epam.deltix.util.collections;

import com.epam.deltix.util.lang.Util;

/**
 *  Compares bytes arrays as unsigned MSBF.
 */
public final class UnsignedByteArrayComparator implements ByteArrayComparator {
    private int     mOrder;
    
    private UnsignedByteArrayComparator (int order) {
        mOrder = order;
    }
    
    public int      compare (byte [] key1, int offset1, byte [] key2, int offset2, int keySize) {
        return (mOrder * Util.arrayucomp (key1, offset1, key2, offset2, keySize));
    }
    
    public static final ByteArrayComparator       ASCENDING = new UnsignedByteArrayComparator (1);
    
    public static final ByteArrayComparator       DESCENDING = new UnsignedByteArrayComparator (-1);
}