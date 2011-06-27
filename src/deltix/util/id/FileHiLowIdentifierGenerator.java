package deltix.util.id;

import deltix.util.io.IOUtil;

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
    private static final String FILE_MODE = System.getProperty("UHF.idGeneratorFileMode", "rwd");
    private final RandomAccessFile raf;
    private final FileChannel channel;
    private final FileLock lock;

    public FileHiLowIdentifierGenerator (String key, int blockSize)
        throws IOException
    {
    	this (key, blockSize, 1);
	}

    public FileHiLowIdentifierGenerator (String key, int blockSize, long startId)
        throws IOException
    {
        super(key, blockSize, startId);

        boolean tryMigrate = ! file.exists();
        raf = new RandomAccessFile(file, FILE_MODE);
        channel = raf.getChannel();

        lock = channel.tryLock();
        if (lock == null)
            throw new RuntimeException("Another program holds lock for file " + file.getAbsolutePath());

        if(tryMigrate)
            migrateIdFromOldFile ();
    }

    /** Prior to May 5 2010, we used to store last used block rather than next free block in the file */
    private void migrateIdFromOldFile() throws IOException {
        File oldFile = getSequenceFileOld(key);
        if (oldFile.exists()) {
            try {
                String [] lines = IOUtil.readLinesFromTextFile(oldFile);
                if (lines != null && lines.length > 0) {
                    long nextBlock = Long.parseLong(lines[0]) + blockSize;
                    store(nextBlock > 0 ? nextBlock : 100*blockSize);
                }
                oldFile.delete();
            } catch (Exception e) {
                throw new RuntimeException("Error reading last sequence number" + e.getMessage(), e);
            }
        }
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
        storeLastUsed();
        if (lock != null)
            lock.release();
        if (channel != null)
            channel.close();
        if (raf != null)
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
