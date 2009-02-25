package deltix.util.id;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import deltix.temp.NetBeansDebugMain;

/**
 * HiLowIdentifierGenerator that uses file storage in given directory to persist last allocated block.
 * This keep file storage locked during application up time, disallowing access from another process.
 *
 * @see SharedFileHiLowIdentifierGenerator
 */
public final class FileHiLowIdentifierGenerator extends HiLowIdentifierGenerator implements Closeable {

    private final File seqFile;
    private final RandomAccessFile raf;
    private final FileChannel channel;
    private final FileLock lock;

    public FileHiLowIdentifierGenerator (String dir, String key, int blockSize)
	    throws IOException
	{
    	this (dir, key, blockSize, 1);
	}

    public FileHiLowIdentifierGenerator (String dir, String key, int blockSize, long startId)
        throws IOException
    {
        super(key, blockSize, startId);
        seqFile = new File (dir, "seq-block-"+key+".id");
        seqFile.getAbsoluteFile().getParentFile().mkdirs();

        raf = new RandomAccessFile(seqFile, "rw");
        channel = raf.getChannel();
        lock = channel.lock();
    }

    @Override
    protected long aquireNextBlock(long resetNextBlock) {
        try {

            final long nextBlock;
            
            if (resetNextBlock != 0) {
            	nextBlock = resetNextBlock;
            } else {
                if (seqFile.length() == 0) {
                    nextBlock = startId;
                } else {
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
        if (lock != null)
            lock.release();
        if (channel != null)
            channel.close();
        if (raf != null)
            raf.close();
    }


}
