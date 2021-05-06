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
