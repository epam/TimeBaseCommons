package deltix.util.io;

import java.util.Map;
import java.util.HashMap;
import java.io.RandomAccessFile;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.nio.channels.ClosedChannelException;

/**
 *  A globally synchronized <b>exclusive</b> file lock.
 */
public abstract class FileLockSynchronizer {
    private static final Map<String, FileLock> mLocks = new HashMap<String, FileLock>(1);

    public static String lock(final File file, final RandomAccessFile raf, boolean readOnly) throws IOException {
        String globalPath = file.getCanonicalPath();
        synchronized (mLocks) {
            if (mLocks.containsKey(globalPath))
                throw new IllegalStateException("The file is locked already: " + globalPath);

            FileLock flock = raf.getChannel().tryLock(0, Long.MAX_VALUE, readOnly);
            if (flock == null)
                throw new RuntimeException("Cannot lock file " + globalPath);
            mLocks.put(globalPath, flock);
        }
        return globalPath;
    }

    public static void release(final File file) throws IOException {
        String globalPath = file.getCanonicalPath();
        synchronized (mLocks) {
            FileLock flock = mLocks.remove(globalPath);
            if (flock == null)
                throw new IllegalStateException("The file was not locked: " + globalPath);
            else
                try {
                    flock.release();
                }
                catch (ClosedChannelException e) {
                }
        }
    }
}
