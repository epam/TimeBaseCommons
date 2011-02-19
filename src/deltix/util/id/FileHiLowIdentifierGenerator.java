package deltix.util.id;

import java.io.Closeable;
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

    public FileHiLowIdentifierGenerator (String key, int blockSize)
	    throws IOException
	{
    	this (key, blockSize, 1);
	}

    public FileHiLowIdentifierGenerator (String key, int blockSize, long startId)
        throws IOException
    {
        super(key, blockSize, startId);

        raf = new RandomAccessFile(file, "rw");
        channel = raf.getChannel();

        lock = channel.tryLock();
        if (lock == null)
            throw new RuntimeException("Another program holds lock for file " + file.getAbsolutePath());
    }

    @Override
    protected long aquireNextBlock(long resetNextBlock) {
        try {

            final long nextBlock;
            
            if (resetNextBlock != 0) {
            	nextBlock = resetNextBlock;
            } else {
                if (file.length() == 0) {
                    nextBlock = startId;
                } else {
                	raf.seek(0L);
                	String lastBlock = raf.readLine();
                    nextBlock = Long.parseLong(lastBlock) + blockSize;
                }
            }

            raf.seek(0L);
            raf.write(Long.toString(nextBlock).getBytes());

            return nextBlock;



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
        long lastUsed = next();
        try {
            raf.seek(0L);
            raf.write(Long.toString(lastUsed - blockSize + 1).getBytes());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


}
