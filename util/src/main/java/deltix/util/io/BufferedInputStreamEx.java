package deltix.util.io;

import deltix.util.lang.Bits;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * {@link BufferedInputStream} populates it's buffer with initial data from provided buffer.
 */
public class BufferedInputStreamEx extends BufferedInputStream {
    /**
     * Use {@link #BufferedInputStreamEx(InputStream, byte[], int, int)} instead.
     */
    @Deprecated
    public BufferedInputStreamEx(InputStream in, byte[] buf, int size) {
        this(in, buf, size, 8192);
    }

    /**
     * @param size amount of data in the provided buffer, will be copied to the new buffer
     * @param minBufferSize minimum size for a new buffer
     */
    public BufferedInputStreamEx(InputStream in, byte[] buf, int size, int minBufferSize) {
        super(in, Bits.nextPowerOfTwo(Math.max(size, minBufferSize)));

        assert size <= this.buf.length;

        // Copy provided buffer to internal buffer
        System.arraycopy(buf, 0, this.buf, 0, size);
        this.count = size;
    }
}
