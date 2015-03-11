package deltix.util.collections;

import deltix.util.lang.Changeable;
import deltix.util.memory.UnsafeDirectByteBuffer;
import deltix.util.memory.UnsafeAccess;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 */
public class OffHeapByteQueue {

    public class Sequence implements Changeable {
        public long addr;
        public long value;

        public Sequence(long addr) {
            this.addr = addr;
            value = getLong(addr);
        }

        public boolean changed() {
            if (valueChanged(addr, value)) {
                value = getLong(addr);
                return true;
            }
            return false;
        }
    }

    private final int               capacity;
    private final int               mask;

    private ByteBuffer              buf;

    private final long              headAddr;
    private final long              tailAddr;
    private final long              queueAddr;

    private long                    headCache;
    private long                    tailCache;
    private Sequence                headSequence;
    private Sequence                tailSequence;

    /**
     * @param buf - ByteBuffer, which capacity must be
     *              returned by OffHeapByteQueue.getRecommendedBufSize.
     * @throws IOException
     */
    public OffHeapByteQueue(ByteBuffer buf) throws IOException {
        capacity = buf.capacity() - 2*UnsafeDirectByteBuffer.CACHE_LINE_SIZE;
        if (Integer.bitCount(capacity) != 1)
            throw new IllegalArgumentException(
                "ByteBuffer capacity must be power of 2 + 2 * cache line size. " +
                "Please use getRecommendedBufSize method.");
        mask = capacity - 1;

        this.buf = buf;
        headAddr = UnsafeDirectByteBuffer.getAddress(this.buf);
        tailAddr = headAddr + UnsafeDirectByteBuffer.CACHE_LINE_SIZE;
        queueAddr = tailAddr + UnsafeDirectByteBuffer.CACHE_LINE_SIZE;

        headSequence = new Sequence(headAddr);
        tailSequence = new Sequence(tailAddr);
    }

    /**
     * Returns optimal size of buffer, which will be used in OffHeapByteQueue.
     * The size must be power of 2 + 2*CACHE_LINE_SIZE, so this method
     * will return correct size for your count of elements.
     * @param elements - minimal number of element in queue.
     * @return size of buffer.
     */
    public static int               getRecommendedBufSize(int elements) {
        return (1 << (int)(Math.ceil(Math.log(elements)/Math.log(2))))
               + 2*UnsafeDirectByteBuffer.CACHE_LINE_SIZE;
    }

    public int                      capacity() {
        return capacity;
    }

    public int                      size() {
        return (int) (getTail() - getHead());
    }

    public int                      free() {
        return capacity() - size();
    }

    public boolean                  isEmpty() throws IOException {
        return getHeadChecked() == -1;
    }

    public boolean                  isFull() throws IOException {
        return getTailChecked() == -1;
    }

    public Changeable               getHeadSequence() {
        return headSequence;
    }

    public Changeable               getTailSequence() {
        return tailSequence;
    }

    public int                      poll() throws IOException {
        long head = getHeadChecked();
        if (head < 0)
            throw new IOException("Error OffHeapQueue is empty");

        int res = UnsafeAccess.UNSAFE.getByteVolatile(null, queueAddr + ((head++) & mask));
        putHead(head);

        return res & 0xFF;
    }

    public int                      poll(byte b[], int off, int len) throws IOException {
        long head = getHeadChecked();
        if (head < 0)
            throw new IOException("Error OffHeapQueue is empty");

        int available = (int) (getTail() - head);
        int count = available < len ? available : len;

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

    public void                     offer(byte b) throws IOException {
        long tail = getTailChecked();
        if (tail < 0)
            throw new IOException("Error OffHeapQueue is full");

        UnsafeAccess.UNSAFE.putByte(null, queueAddr + ((tail++) & mask), b);
        putTail(tail);
    }

    public void                     offer(byte b[], int off, int len) throws IOException {
        long tail = getTailChecked();
        if (tail < 0)
            throw new IOException("Error OffHeapQueue is full");

        long gap = (tail & mask) + len - capacity;
        if (gap > 0) {
            UnsafeAccess.UNSAFE.copyMemory(b, UnsafeAccess.UNSAFE.ARRAY_BYTE_BASE_OFFSET + off, null, queueAddr + (tail & mask), len - gap);
            UnsafeAccess.UNSAFE.copyMemory(b, UnsafeAccess.UNSAFE.ARRAY_BYTE_BASE_OFFSET + off + len - gap, null, queueAddr, gap);
        } else {
            UnsafeAccess.UNSAFE.copyMemory(b, UnsafeAccess.UNSAFE.ARRAY_BYTE_BASE_OFFSET + off, null, queueAddr + (tail & mask), len);
        }

        putTail(tail + len);
    }

    private long                    getHead() {
        return getLong(headAddr);
    }

    private long                    getTail() {
        return getLong(tailAddr);
    }

    private void                    putHead(long head) {
        putLong(headAddr, head);
    }

    private void                    putTail(long tail) {
        putLong(tailAddr, tail);
    }

    private long                    getLong(long addr) {
        return UnsafeAccess.UNSAFE.getLongVolatile(null, addr);
    }

    private void                    putLong(long addr, long value) {
        UnsafeAccess.UNSAFE.putOrderedLong(null, addr, value);
    }

    private long                    getHeadChecked() throws IOException {
        long head = getHead();
        if (head >= tailCache) {
            tailCache = getTail();
            if (head >= tailCache)
                return -1;
        }

        return head;
    }

    private long                    getTailChecked() throws IOException {
        long tail = getTail();

        final long wrapPoint = tail - capacity;
        if (headCache <= wrapPoint) {
            headCache = getHead();
            if (headCache <= wrapPoint)
                return -1;
        }

        return tail;
    }

    private boolean                 valueChanged(long addr, long value) {
        return !UnsafeAccess.UNSAFE.compareAndSwapLong(null, addr, value, value);
    }

}
