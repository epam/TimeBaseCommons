package com.epam.deltix.util.io;

import com.epam.deltix.util.lang.Bits;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * {@link BufferedInputStream} populates it's buffer with initial data from provided buffer.
 */
public class BufferedInputStreamEx extends BufferedInputStream {
    public BufferedInputStreamEx(InputStream in, byte[] buf, int size) {
        super(in, Math.max(Bits.nextPowerOfTwo(size), 8192));

        assert size <= this.buf.length;

        // Copy provided buffer to internal buffer
        System.arraycopy(buf, 0, this.buf, 0, size);
        this.count = size;
    }
}
