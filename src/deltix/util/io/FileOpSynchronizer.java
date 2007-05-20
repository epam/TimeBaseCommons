package deltix.util.io;

import java.io.*;
import java.nio.channels.*;

import deltix.util.*;
import deltix.util.collections.generated.*;

/**
 *  A globally synchronized file operation.
 */
public abstract class FileOpSynchronizer {
    public interface Operation {
        public void             perform (
            File                        f,
            RandomAccessFile            raf
        )
            throws IOException, InterruptedException;
    }
    
    private static final int    NO_LOCK = 0;
    private static final int    EXCLUSIVE_LOCK = -1;
    
    /**
     *  Maps file path to whether the number of shared locks
     */
    private static ObjectToIntegerHashMap <String>        mLocks = 
        new ObjectToIntegerHashMap <String> ();
    
    public static void          perform (
            File                    f, 
            Operation           op,
            boolean                 readOnly
    )
        throws IOException, InterruptedException
    {
        String              globalPath = f.getCanonicalPath ();
        RandomAccessFile    raf = null;
        FileLock            flock = null;
        boolean             gotLocalLock = false;
        
        try {
            synchronized (mLocks) {   
                int     lockCount;
                
                for (;;) {
                    lockCount = mLocks.get (globalPath, NO_LOCK);
                    
                    if (lockCount == NO_LOCK)
                        break;
                    
                    if (readOnly && lockCount != EXCLUSIVE_LOCK)
                        break;
                    
                    mLocks.wait ();
                }
                
                if (readOnly)
                    lockCount++;
                else
                    lockCount = EXCLUSIVE_LOCK;
                
                mLocks.put (globalPath, lockCount);
                gotLocalLock = true;
            }
                
            raf = new RandomAccessFile (f, readOnly ? "r" : "rw");
            
            flock = raf.getChannel ().lock (0, Long.MAX_VALUE, readOnly);
            
            op.perform (f, raf);
        } finally {
            // MUST release in reverse order
            
            if (flock != null)
                flock.release ();
            
            Util.close (raf);
            
            if (gotLocalLock) {
                try {
                    synchronized (mLocks) {
                        if (readOnly) {
                            int     lockCount = mLocks.get (globalPath);

                            lockCount--;

                            if (lockCount == 0) {
                                mLocks.remove (globalPath);
                                mLocks.notifyAll ();
                            }
                            else
                                mLocks.put (globalPath, lockCount);
                        }
                        else {
                            mLocks.remove (globalPath);
                            mLocks.notifyAll ();
                        }
                    }
                } catch (ObjectToIntegerHashMap.KeyNotFoundException x) {
                    throw new RuntimeException ("Lock record is gone", x);
                }
            }
        }        
    }
}
