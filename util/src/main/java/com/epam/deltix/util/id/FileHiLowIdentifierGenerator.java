package com.epam.deltix.util.id;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * HiLowIdentifierGenerator that uses file storage in given directory to persist last allocated block.
 * This keep file storage locked during application up time, disallowing access from another process.
 *
 * @see SharedFileHiLowIdentifierGenerator
 */
public final class FileHiLowIdentifierGenerator extends FileBasedHiLowIdentifierGenerator implements Closeable {
    private final RandomAccessFile raf;
    private final FileChannel channel;
    private final FileLock lock;
    private final boolean storeLastUsedOnClose;

    public FileHiLowIdentifierGenerator (File dir, String key, int blockSize, boolean writeLastUsedOnClose) throws IOException {
        this (dir, key, blockSize, 1, writeLastUsedOnClose, "rwd");
	}

    public FileHiLowIdentifierGenerator(File dir, String key, int blockSize, long startId, boolean writeLastUsedOnClose, String fileMode) throws IOException {
        super(dir, key, blockSize, startId);

        storeLastUsedOnClose = writeLastUsedOnClose;
        raf = new RandomAccessFile(file, fileMode);
        channel = raf.getChannel();

        lock = channel.tryLock();
        if (lock == null)
            throw new RuntimeException("Another program holds lock for file " + file.getAbsolutePath());
    }


    @Override
    protected long acquireNextBlock(long resetNextBlock) {
        try {

            final long currentBlock;
            
            if (resetNextBlock != 0) {
            	currentBlock = resetNextBlock;
            } else {
                if (file.length() == 0) {
                    currentBlock = startId;
                } else {
                	raf.seek(0L);
                	String lastBlock = raf.readLine();
                    currentBlock = Long.parseLong(lastBlock);
                }
            }

            raf.seek(0L);
            raf.write(Long.toString(currentBlock + blockSize).getBytes());  // assuming number of digits always grows
            //channel.force(true); not required since we access file in "rwd" mode (d).
            return currentBlock;



        } catch (IOException e) {
            throw new RuntimeException ("Error accessing sequence storage file: " + e.getMessage(), e);
        }
    }

    @Override
    public void close() throws IOException {
        if (storeLastUsedOnClose)
            storeLastUsed();

        lock.release();
        channel.close();
        raf.close();
    }

    private void storeLastUsed() {
        store(next());
    }

    private void store(long lastUsed) {
        if (channel != null && channel.isOpen() && raf != null)
            try {
                raf.seek(0L);

                // storing last used may reduce number of bytes stored compared to last block, lets adjust file size
                String block = Long.toString(lastUsed);
                raf.write(block.getBytes());
                raf.setLength(block.length());

            } catch (Throwable e) {
                e.printStackTrace();
            }
    }

}
