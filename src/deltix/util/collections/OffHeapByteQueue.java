package deltix.util.collections;

import deltix.util.memory.UnsafeDirectByteBuffer;
import deltix.util.memory.UnsafeAccess;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.locks.LockSupport;

/**
 *
 */
public class OffHeapByteQueue {

    static class SuspenseHelper {
        private static final long       WAIT_NANOS = Long.getLong("waitNanos",0L) ;
        private static final boolean    SHOULD_WAIT = (WAIT_NANOS != 0L);
        private static final boolean    SHOULD_YIELD = !Boolean.getBoolean("not_yield");

        public static void              yield() {
            if(SHOULD_YIELD){
                Thread.yield();
            }
        }
        public static void              waitSome() {
            if(SHOULD_WAIT){
                LockSupport.parkNanos(WAIT_NANOS);
            }
        }
    }

    public final static int         QUEUE_SIZE = 1 << 20;   //1024Kb

    private final static int        CACHE_LINE_SIZE = 256;
    private final static int        MEMBUF_SIZE = QUEUE_SIZE + 2*CACHE_LINE_SIZE;
    private final static int        MASK = QUEUE_SIZE - 1;

    private MappedByteBuffer        membuf;

    private final long              headAddr;
    private final long              tailAddr;
    private final long              queueAddr;

    private long                    headCache;
    private long                    tailCache;

    public OffHeapByteQueue(FileChannel channel) throws IOException {
        membuf = channel.map(FileChannel.MapMode.READ_WRITE, 0, MEMBUF_SIZE);
        membuf.put(new byte[MEMBUF_SIZE]); //zero fill
        headAddr = UnsafeDirectByteBuffer.getAddress(membuf);
        tailAddr = headAddr + CACHE_LINE_SIZE;
        queueAddr = tailAddr + CACHE_LINE_SIZE;
    }

    public int                      available() {
        return (int) (getTail() - getHead());
    }

    public boolean                  isEmpty() {
        return getHead() >= getTail();
    }

    public boolean                  isFull() {
        return getTail() - getHead() >= QUEUE_SIZE;
    }

    public int                      waitAvailable(int cycles) {
        long head = getHead();
        long tail = getTail();

        if (head >= tail) {
            while (--cycles > 0 && tailChanged(tail))
                SuspenseHelper.yield();
            tail = getTail();
            if (head >= tail)
                return -1;
        }

        return (int) (tail - head);
    }

    public int                      waitNotFull(int cycles) {
        long head = getHead();
        long tail = getTail();

        if (tail - head >= QUEUE_SIZE) {
            while (--cycles > 0 && headChanged(head))
                SuspenseHelper.yield();
            tail = getTail();
            if (tail - head >= QUEUE_SIZE)
                return -1;
        }

        return (int) (tail - head);
    }

    public int                      readByte() throws IOException {
        long head = getHeadChecked();

        int res = UnsafeAccess.UNSAFE.getByteVolatile(null, queueAddr + ((head++) & MASK));
        putHead(head);

        return res & 0xFF;
    }

    public int                      readByte(byte b[], int off, int len) throws IOException {
        long head = getHeadChecked();

        long tail = UnsafeAccess.UNSAFE.getLongVolatile(null, tailAddr);
        int available = (int) (tail - head);
        int count = available < len ? available : len;

        long gap = (head & MASK) + count - QUEUE_SIZE;
        if (gap > 0) {
            UnsafeAccess.UNSAFE.copyMemory(null, queueAddr + (head & MASK), b, UnsafeAccess.UNSAFE.ARRAY_BYTE_BASE_OFFSET + off, count - gap);
            UnsafeAccess.UNSAFE.copyMemory(null, queueAddr, b, UnsafeAccess.UNSAFE.ARRAY_BYTE_BASE_OFFSET + off + count - gap, gap);
        } else {
            UnsafeAccess.UNSAFE.copyMemory(null, queueAddr + (head & MASK), b, UnsafeAccess.UNSAFE.ARRAY_BYTE_BASE_OFFSET + off, count);
        }

        putHead(head + count);

        return count;
    }

    public void                     writeByte(byte b) throws IOException {
        long tail = getTailChecked();

        UnsafeAccess.UNSAFE.putByte(null, queueAddr + ((tail++) & MASK), b);
        putTail(tail);
    }

    public void                     writeByte(byte b[], int off, int len) throws IOException {
        long tail = getTailChecked();

        long gap = (tail & MASK) + len - QUEUE_SIZE;
        if (gap > 0) {
            UnsafeAccess.UNSAFE.copyMemory(b, UnsafeAccess.UNSAFE.ARRAY_BYTE_BASE_OFFSET + off, null, queueAddr + (tail & MASK), len - gap);
            UnsafeAccess.UNSAFE.copyMemory(b, UnsafeAccess.UNSAFE.ARRAY_BYTE_BASE_OFFSET + off + len - gap, null, queueAddr, gap);
        } else {
            UnsafeAccess.UNSAFE.copyMemory(b, UnsafeAccess.UNSAFE.ARRAY_BYTE_BASE_OFFSET + off, null, queueAddr + (tail & MASK), len);
        }

        putTail(tail + len);
    }

    private long                    getHead() {
        return UnsafeAccess.UNSAFE.getLongVolatile(null, headAddr);
    }

    private long                    getTail() {
        return UnsafeAccess.UNSAFE.getLongVolatile(null, tailAddr);
    }

    private void                    putHead(long head) {
        UnsafeAccess.UNSAFE.putOrderedLong(null, headAddr, head);
    }

    private void                    putTail(long tail) {
        UnsafeAccess.UNSAFE.putOrderedLong(null, tailAddr, tail);
    }

    private long                    getHeadChecked() throws IOException {
        long head = getHead();
        if (head >= tailCache) {
            tailCache = getTail();
            if (head >= tailCache)
                throw new IOException("Error OffHeapQueue is empty");
        }

        return head;
    }

    private long                    getTailChecked() throws IOException {
        long tail = getTail();

        final long wrapPoint = tail - QUEUE_SIZE;
        if (headCache <= wrapPoint) {
            headCache = getHead();
            if (headCache <= wrapPoint)
                throw new IOException("Error OffHeapQueue is full");
        }

        return tail;
    }

    private boolean                 tailChanged(long tail) {
        return UnsafeAccess.UNSAFE.compareAndSwapLong(null, tailAddr, tail, tail);
    }

    private boolean                 headChanged(long head) {
        return UnsafeAccess.UNSAFE.compareAndSwapLong(null, headAddr, head, head);
    }

}
