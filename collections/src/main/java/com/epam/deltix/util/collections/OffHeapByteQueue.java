package com.epam.deltix.util.collections;

import com.epam.deltix.util.lang.Bits;
import com.epam.deltix.util.lang.Changeable;
import com.epam.deltix.util.memory.UnsafeDirectByteBuffer;
import com.epam.deltix.util.memory.UnsafeAccess;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 */

class OffHeapByteQueuePadding1 {
    @SuppressWarnings("unused")
    long p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15;
}

class OffHeapByteQueueFields1 extends OffHeapByteQueuePadding1 {
    long headCache = 0;
}

class OffHeapByteQueuePadding2 extends OffHeapByteQueueFields1 {
    @SuppressWarnings("unused")
    long p16, p17, p18, p19, p20, p21, p22, p23, p24, p25, p26, p27, p28, p29, p30;
}

class OffHeapByteQueueFields2 extends OffHeapByteQueuePadding2 {
    long tailCache = 0;
}

public class OffHeapByteQueue extends OffHeapByteQueueFields2 {

    public static class Sequence implements Changeable {
        public long addr;
        public long value;

        public Sequence(long addr) {
            this.addr = addr;
            value = getLongVolatile(addr);
        }

        public boolean changed() {
            long curValue = getLongVolatile(addr);
            if (value != curValue) {
                value = curValue;
                return true;
            }
            return false;
        }
    }

    @SuppressWarnings("unused")
    private final ByteBuffer        buf;    //prevent GC

    private final int               capacity;
    private final int               mask;

    private final long              headAddr;
    private final long              tailAddr;
    private final long              queueAddr;

    private Sequence                tailSequence;

    /**
     * @param buf - ByteBuffer, which capacity must be
     *              returned by OffHeapByteQueue.getRecommendedBufSize.
     */
    private OffHeapByteQueue(ByteBuffer buf) {
        this.buf = buf;
        capacity = buf.capacity() - 4*UnsafeDirectByteBuffer.CACHE_LINE_SIZE;
        if (!Bits.isPowerOfTwo(capacity))
            throw new IllegalArgumentException(
                    "ByteBuffer capacity must be power of 2 + 2 * cache line size. " +
                            "Please use getRecommendedBufSize() method.");
        mask = capacity - 1;

        headAddr = UnsafeDirectByteBuffer.getAddress(buf);
        if (!UnsafeDirectByteBuffer.isAligned(headAddr, 8))
            throw new IllegalStateException("ByteBuffer address [" + headAddr + "] must be aligned by 8.");

        tailAddr = headAddr + UnsafeDirectByteBuffer.CACHE_LINE_SIZE;
        queueAddr = tailAddr + UnsafeDirectByteBuffer.CACHE_LINE_SIZE;

        headCache = 0;
        tailCache = 0;
        putHead(headCache);
        putTail(tailCache);

        tailSequence = new Sequence(tailAddr);
    }

    /**
     * Returns optimal size of buffer, which will be used in OffHeapByteQueue.
     * The size must be power of 2 + 4*CACHE_LINE_SIZE, so this method
     * will return correct size for your count of elements.
     * @param elements - minimal number of element in queue.
     * @return size of buffer.
     */
    public static int               getRecommendedBufSize(int elements) {
        return Bits.nextPowerOfTwo(elements) + 4*UnsafeDirectByteBuffer.CACHE_LINE_SIZE;
    }

    public static OffHeapByteQueue  newInstance(RandomAccessFile raf, int elements) throws IOException {
        int queueSize = OffHeapByteQueue.getRecommendedBufSize(elements);
        MappedByteBuffer buffer = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, queueSize);
        return new OffHeapByteQueue(buffer);
    }

    public int                      capacity() {
        return capacity;
    }

    public int                      poll() throws IOException {
        long head = getLong(headAddr);

        if (head >= tailCache) {
            tailCache = getLongVolatile(tailAddr);
            if (head >= tailCache)
                return -1;
        }

        int res = UnsafeAccess.UNSAFE.getByteVolatile(null, queueAddr + ((head++) & mask));
        putHead(head);

        return res & 0xFF;
    }

    public int                      poll(byte b[], int off, int len) throws IOException {
        long head = getLong(headAddr);

        if (head >= tailCache) {
            tailCache = getLongVolatile(tailAddr);
            if (head >= tailCache)
                return 0;
        }

        int count = Math.min((int) (tailCache - head), len);
        long gap = (head & mask) + count - capacity;
        if (gap > 0) {
            UnsafeAccess.UNSAFE.copyMemory(null, queueAddr + (head & mask), b, UnsafeAccess.UNSAFE.ARRAY_BYTE_BASE_OFFSET + off, count - gap);
            UnsafeAccess.UNSAFE.copyMemory(null, queueAddr, b, UnsafeAccess.UNSAFE.ARRAY_BYTE_BASE_OFFSET + off + count - gap, gap);
        } else {
            UnsafeAccess.UNSAFE.copyMemory(null, queueAddr + (head & mask), b, UnsafeAccess.UNSAFE.ARRAY_BYTE_BASE_OFFSET + off, count);
        }

        putHead(head + count);

        return count;
    }

    public boolean                  offer(byte b) throws IOException {
        long tail = getLong(tailAddr);

        final long wrapPoint = tail - capacity;
        if (headCache <= wrapPoint) {
            headCache = getLongVolatile(headAddr);
            if (headCache <= wrapPoint)
                return false;
        }

        UnsafeAccess.UNSAFE.putByte(null, queueAddr + ((tail++) & mask), b);
        putTail(tail);

        return true;
    }

    public int                      offer(byte b[], int off, int len) throws IOException {
        long tail = getLong(tailAddr);

        final long wrapPoint = tail - capacity;
        if (headCache <= wrapPoint) {
            headCache = getLongVolatile(headAddr);
            if (headCache <= wrapPoint)
                return 0;
        }

        int toWrite = Math.min(len, (int) (capacity - (tail - headCache)));
        long gap = (tail & mask) + toWrite - capacity;
        if (gap > 0) {
            UnsafeAccess.UNSAFE.copyMemory(b, UnsafeAccess.UNSAFE.ARRAY_BYTE_BASE_OFFSET + off, null, queueAddr + (tail & mask), toWrite - gap);
            UnsafeAccess.UNSAFE.copyMemory(b, UnsafeAccess.UNSAFE.ARRAY_BYTE_BASE_OFFSET + off + toWrite - gap, null, queueAddr, gap);
        } else {
            UnsafeAccess.UNSAFE.copyMemory(b, UnsafeAccess.UNSAFE.ARRAY_BYTE_BASE_OFFSET + off, null, queueAddr + (tail & mask), toWrite);
        }

        putTail(tail + toWrite);

        return toWrite;
    }

    private void                    putHead(long head) {
        putOrderedLong(headAddr, head);
    }

    private void                    putTail(long tail) {
        putOrderedLong(tailAddr, tail);
    }

    public Changeable               getTailSequence() {
        return tailSequence;
    }

    private static long             getLong(long addr) {
        return UnsafeAccess.UNSAFE.getLong(null, addr);
    }

    private static long             getLongVolatile(long addr) {
        return UnsafeAccess.UNSAFE.getLongVolatile(null, addr);
    }

    private static void             putOrderedLong(long addr, long value) {
        UnsafeAccess.UNSAFE.putOrderedLong(null, addr, value);
    }

}
