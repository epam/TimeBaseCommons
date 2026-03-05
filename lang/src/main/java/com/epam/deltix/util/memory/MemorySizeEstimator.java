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

import com.epam.deltix.util.lang.Util;

/**
 *  Implemented by objects that can estimate the size they occupy in memory.
 */
public interface MemorySizeEstimator {
    public static final int     SIZE_OF_BYTE = 1;
    public static final int     SIZE_OF_BOOLEAN = 1;
    public static final int     SIZE_OF_CHAR = 2;
    public static final int     SIZE_OF_SHORT = 2;
    public static final int     SIZE_OF_INT = 4;
    public static final int     SIZE_OF_LONG = 8;
    public static final int     SIZE_OF_FLOAT = 4;
    public static final int     SIZE_OF_DOUBLE = 8;
    public static final int     SIZE_OF_POINTER = Util.IS64BIT ? 8 : 4;
    public static final int     OBJECT_OVERHEAD = 2 * SIZE_OF_POINTER;
    public static final int     ARRAY_OVERHEAD = Util.IS64BIT ? 24 : 16;

    public long          getSizeInMemory ();
}