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
