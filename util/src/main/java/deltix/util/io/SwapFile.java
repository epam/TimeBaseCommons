package deltix.util.io;

import deltix.gflog.Log;
import deltix.gflog.LogFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SwapFile {

    public static final Log LOGGER = LogFactory.getLog("deltix.util.io.SwapFile");

    private final File swapFile;
    protected final RandomAccessFile file;
    private final int blockSize;
    private final int minGrowCount;
    private final LongQueue freeBlocks;

    public SwapFile(String directory, int blockSize, int minGrowCount) {
        super();
        try {
            String filename = "swap_" + System.identityHashCode(this) + "_" + System.currentTimeMillis();
            swapFile = new File(directory, filename);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug().append("Creating swap file ").append(swapFile.getPath()).commit();
            }
            boolean fileExists = swapFile.exists();
            swapFile.deleteOnExit();
            file = new RandomAccessFile(swapFile, "rw");
            this.blockSize = blockSize;
            this.minGrowCount = minGrowCount;
            freeBlocks = new LongQueue(minGrowCount);
            if (fileExists) {
                file.setLength(0);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Swap file %s exists, truncating").with(swapFile.getPath());
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SwapHandle write(byte[] data) throws IOException {
        int blockCount = (data.length - 1) / blockSize + 1;
        long[] offsets = reserveFreeBlocks(blockCount);
        int lastBlockSize = (data.length - 1) % blockSize + 1;
        SwapHandle handle = new SwapHandle(offsets, lastBlockSize);
        for (int i = 0; i < blockCount; ++i) {
            int dataSize = i < blockCount - 1 ? blockSize : lastBlockSize;
            int dataOffset = i * blockSize;
            write(data, dataSize, dataOffset, offsets[i]);
        }
        return handle;
    }

    protected void write(byte[] data, int dataSize, int dataOffset, long fileOffset) throws IOException {
        synchronized (this) {
            file.seek(fileOffset);
            file.write(data, dataOffset, dataSize);
        }
    }

    public byte[] read(SwapHandle handle, boolean free) throws IOException {
        long[] offsets = handle.getOffsets();
        int totalLength = (offsets.length - 1) * blockSize + handle.getLastSize();
        byte[] data = new byte[totalLength];
        for (int i = 0; i < offsets.length; ++i) {
            int dataOffset = i * blockSize;
            int dataLength = i < offsets.length - 1 ? blockSize : handle.getLastSize();
            read(data, dataOffset, dataLength, offsets[i]);
        }
        if (free) {
            freeBlocks(offsets);
        }
        return data;
    }

    protected void read(byte[] data, int dataOffset, int dataLength, long fileOffset) throws IOException {
        synchronized (this) {
            file.seek(fileOffset);
            file.readFully(data, dataOffset, dataLength);
        }
    }

    public void free(SwapHandle handle) {
        freeBlocks(handle.getOffsets());
    }

    public void dispose() {
        synchronized (this) {
            if (swapFile.exists()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug().append("Disposing swap file ").append(swapFile.getPath()).commit();
                }
                try {
                    file.close();
                } catch (IOException e) {
                    LOGGER.warn().append("Not able to close swap file ").append(swapFile.getPath()).commit();
                }
                if (!swapFile.delete()) {
                    LOGGER.warn().append("Not able to delete swap file ").append(swapFile.getPath()).commit();
                }
            }
        }
    }

    protected void finalize() throws Throwable {
        dispose();
        super.finalize();
    }

    protected synchronized long[] reserveFreeBlocks(int blockCount) throws IOException {
        int growCount = blockCount - freeBlocks.size();
        if (growCount > 0) {
            if (growCount < minGrowCount) {
                growCount = minGrowCount;
            }
            long length = file.length();
            long newLength = length + growCount * blockSize;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Growing swap file %s with %s blocks x %s bytes to size %s")
                        .with(swapFile.getPath()).with(growCount).with(blockSize).with(newLength);
            }
            file.setLength(newLength);
            for (int i = 0; i < growCount; ++i) {
                freeBlocks.addLast(length + i * blockSize);
            }
        }
        long[] offsets = new long[blockCount];
        for (int i = 0; i < blockCount; i++) {
            offsets[i] = freeBlocks.popFirst();
        }
        return offsets;
    }

    protected synchronized void freeBlocks(long[] offsets) {
        for (int i = offsets.length - 1; i >= 0; --i) {
            freeBlocks.addFirst(offsets[i]);
        }
    }

    protected static class LongQueue {

        private final int minGrowCount;
        private long[] vals;
        private int size;
        private int first;
        private int last;

        public LongQueue(int minGrowCount) {
            super();
            this.minGrowCount = minGrowCount;
            vals = new long[minGrowCount];
            size = 0;
            first = 0;
            last = 0;
        }

        public void addFirst(long val) {
            growIfFull();
            --first;
            if (first == -1) {
                first = vals.length - 1;
            }
            vals[first] = val;
            ++size;
        }

        public void addLast(long val) {
            growIfFull();
            vals[last] = val;
            ++size;
            ++last;
            if (last == vals.length) {
                last = 0;
            }
        }

        public long popFirst() {
            if (size == 0) {
                throw new RuntimeException("Queue underflow");
            }
            long val = vals[first];
            ++first;
            if (first == vals.length) {
                first = 0;
            }
            --size;
            return val;
        }

        protected void growIfFull() {
            int valsLenght = vals.length;
            if (size == valsLenght) {
                int newLength = (valsLenght * 3) / 2 + 1;
                if (newLength - valsLenght < minGrowCount) {
                    newLength = valsLenght + minGrowCount;
                }
                long[] newVals = new long[newLength];
                System.arraycopy(vals, first, newVals, 0, valsLenght - first);
                if (last > 0) {
                    System.arraycopy(vals, 0, newVals, valsLenght - first, last);
                }
                vals = newVals;
                first = 0;
                last = valsLenght;
            }
        }

        public int size() {
            return size;
        }
    }


    public static class SwapHandle {

        private final long[] offsets;
        private final int lastSize;

        public SwapHandle(long[] offsets, int lastSize) {
            super();
            this.offsets = offsets;
            this.lastSize = lastSize;
        }

        public long[] getOffsets() {
            return offsets;
        }

        public int getLastSize() {
            return lastSize;
        }
    }

}

