package deltix.util.io.offheap;

import deltix.util.collections.OffHeapByteQueue;
import deltix.util.io.idlestrat.BusySpinIdleStrategy;
import deltix.util.io.idlestrat.IdleStrategy;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class OffHeapInputStream extends InputStream {
    protected OffHeapByteQueue          queue;
    private IdleStrategy                idleStrategy;

    OffHeapInputStream(OffHeapByteQueue queue) {
        this(queue, new BusySpinIdleStrategy());
    }

    OffHeapInputStream(OffHeapByteQueue queue, IdleStrategy idleStrategy) {
        this.queue = queue;
        this.idleStrategy = idleStrategy;
    }

    @Override
    public int                          read() throws IOException {
        int workCount = 0;
        int res;
        while ((res = queue.poll()) < 0) {
            idleStrategy.idle(workCount++);
        }

        return res;
    }

    @Override
    public int                          read(byte b[], int off, int len)
            throws IOException {

        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int count;
        int workCount = 0;
        while ((count = queue.poll(b, off, len)) <= 0) {
            idleStrategy.idle(workCount++);
        }

        return count;
    }

    @Override
    public void                         close() {
    }
}
