package deltix.util.id;

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
public final class FileHiLowIdentifierGenerator extends HiLowIdentifierGenerator implements Closeable {

    private final File seqFile;
    private final RandomAccessFile raf;
    private final FileChannel channel;
    private final FileLock lock;

    public FileHiLowIdentifierGenerator (String dir, String key, int blockSize)
        throws IOException
    {
        super(key, blockSize);
        seqFile = new File (dir, "seq-"+key+".id");
        seqFile.getAbsoluteFile().getParentFile().mkdirs();

        raf = new RandomAccessFile(seqFile, "rw");
        channel = raf.getChannel();
        lock = channel.lock();
    }

    @Override
    protected long aquireNextBlock() {
        try {

            long nextBlock;
            if (seqFile.length() == 0) {
                nextBlock = 1;
            } else {
                nextBlock = raf.readLong() + blockSize;
            }

            raf.seek(0L);
            raf.writeLong (nextBlock);

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
