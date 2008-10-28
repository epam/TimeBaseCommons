package deltix.util.id;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;


/**
 * HiLowIdentifierGenerator that uses file storage in given directory to persist last allocated block.
 * This implementation doesn't keep file storage open, allowing concurrent access from multiple processes.
 *
 * @see FileHiLowIdentifierGenerator
 */
public final class SharedFileHiLowIdentifierGenerator extends HiLowIdentifierGenerator {

    private final File seqFile;

    public SharedFileHiLowIdentifierGenerator (String dir, String key, int blockSize) {
        super(key, blockSize);
        seqFile = new File (dir, "seq-"+key+".id");

        seqFile.getAbsoluteFile().getParentFile().mkdirs();
    }

    @Override
    protected long aquireNextBlock() {
        RandomAccessFile raf = null;
        FileChannel channel = null;
        FileLock lock = null;
        try {

            try {
                raf = new RandomAccessFile(seqFile, "rw");
                channel = raf.getChannel();
                lock = channel.lock();

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
            } finally {
                    if (lock != null)
                        lock.release();
                    if (channel != null)
                        channel.close();
                    if (raf != null)
                        raf.close();
            }

        } catch (IOException e) {
            throw new RuntimeException ("Error accessing sequence storage file: " + e.getMessage(), e);
        }

    }

}
