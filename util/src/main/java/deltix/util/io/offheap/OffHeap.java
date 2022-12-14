package deltix.util.io.offheap;

import deltix.util.collections.OffHeapByteQueue;
import deltix.util.io.IOUtil;
import deltix.util.io.idlestrat.BusySpinIdleStrategy;
import deltix.util.io.waitstrat.ParkWaitStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public final class OffHeap {
    public static final Logger                  LOGGER = Logger.getLogger("deltix.vsoffheap");

    public static final int                     QUEUE_SIZE = 1 << 19;
    public static String                        OFFHEAP_DIR;

    private OffHeap() {
        throw new RuntimeException("Not for you!");
    }

    public synchronized static void             start(String offheapDir, boolean isServer) {
        OFFHEAP_DIR = offheapDir;
        if (isServer)
            prepareDirectory(offheapDir);
    }

    public static InputStream                   createInputStream(String name) throws IOException {
        return createInputStream(createRandomAccessFile(name));
    }

    public static InputStream                   createInputStream(RandomAccessFile raf) throws IOException {
        OffHeapPollableInputStream in =
            new OffHeapPollableInputStream(createByteQueue(raf), new ParkWaitStrategy());
        OffHeapPollableInputStream.POLLER.add(in);
        return in;
        //return new OffHeapInputStream(createByteQueue(raf), new NoOpIdleStrategy());
    }

    public static OutputStream                  createOutputStream(String name) throws IOException {
        return createOutputStream(createRandomAccessFile(name));
    }

    public static OffHeapOutputStream           createOutputStream(RandomAccessFile raf) throws IOException {
        return new OffHeapOutputStream(createByteQueue(raf), new BusySpinIdleStrategy());
    }

    public synchronized static RandomAccessFile createRandomAccessFile(String name) throws FileNotFoundException {
        File file = new File(OFFHEAP_DIR + "/" + name);
        file.deleteOnExit();
        return new RandomAccessFile(file, "rw");
    }

    public static OffHeapByteQueue              createByteQueue(RandomAccessFile raf) throws IOException {
        return OffHeapByteQueue.newInstance(raf, QUEUE_SIZE);
    }

    public synchronized static String           getOffHeapDir() {
        return OFFHEAP_DIR;
    }

    private static void                         prepareDirectory(String offheapDir) {
        try {
            IOUtil.delete(new File(offheapDir));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Delete '" + offheapDir + "' failed.", e);
        }

        if (!new File(offheapDir).mkdirs())
            LOGGER.log(Level.WARNING, "Create '" + offheapDir + "' failed.");
    }
}
