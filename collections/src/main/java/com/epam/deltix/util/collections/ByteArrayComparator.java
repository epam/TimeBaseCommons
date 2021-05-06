package com.epam.deltix.util.collections;

/**
 *  Compares sequences of bytes.
 */
public interface ByteArrayComparator {
    public int          compare (
        byte []             data1,
        int                 offset1,
        byte []             data2,
        int                 offset2,
        int                 dataSize
    );
}
