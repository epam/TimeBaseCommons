package com.epam.deltix.util.io;

import java.util.*;
import java.io.*;
import java.nio.channels.*;

/**
 *  A globally synchronized file lock. Extends java.nio.channels.FileLock
 *  functionality by managing locks acquired by threads within the same JVM.
 */
public abstract class FileLockSynchronizer {
    private static final Map <String, LockSet>      locks = 
        new HashMap <String, LockSet> ();

    public static class Lock extends Throwable {
        public final String             canonicalPath;
        public final File               file;
        public final boolean            readOnly;

        public Lock (String canonicalPath, File file, boolean readOnly) {
            super (
                (readOnly ? "Read-Only" : "Read-Write") +
                " lock on " + canonicalPath
            );
            
            this.canonicalPath = canonicalPath;
            this.file = file;
            this.readOnly = readOnly;
        }                        
    }
    
    private static class LockSet {
        private final String            canonicalPath;
        private final FileLock          fileLock;

        protected LockSet (String canonicalPath, FileLock fileLock) {
            this.canonicalPath = canonicalPath;
            this.fileLock = fileLock;
        }                
    }
    
    private static class SharedLockSet extends LockSet {
        private final Set <Lock>        readers = new HashSet <Lock> ();

        SharedLockSet (String canonicalPath, FileLock fileLock) {
            super (canonicalPath, fileLock);
        }                
    }
    
    private static class ExclusiveLockSet extends LockSet {
        private final Lock              writer;

        ExclusiveLockSet (String canonicalPath, FileLock fileLock, Lock writer) {
            super (canonicalPath, fileLock);
            this.writer = writer;
        }                 
    }
    
    public static Lock          acquire (
        final File                  file, 
        final RandomAccessFile      raf, 
        final boolean               readOnly
    ) 
        throws IOException 
    {
        final String                canonicalPath = file.getCanonicalPath ();
        
        synchronized (locks) {
            LockSet                 lockSet = locks.get (canonicalPath);
            
            if (lockSet instanceof ExclusiveLockSet)
                throw new IllegalStateException (
                    canonicalPath + " is already write-locked by this process (lock attached)",
                    ((ExclusiveLockSet) lockSet).writer
                );

            if (!readOnly && lockSet != null)
                throw new IllegalStateException (
                    canonicalPath + " is already read-locked by this process (one lock attached)",
                    ((SharedLockSet) lockSet).readers.iterator ().next ()
                );

            Lock                    lock = new Lock (canonicalPath, file, readOnly);
            
            if (lockSet == null) {
                FileLock            flock = raf.getChannel ().tryLock (0, Long.MAX_VALUE, readOnly);
                        
                if (flock == null)
                    throw new RuntimeException ("Failed to lock " + canonicalPath);
                        
                if (readOnly)
                    lockSet = new SharedLockSet (canonicalPath, flock);
                else
                    lockSet = new ExclusiveLockSet (canonicalPath, flock, lock);
                        
                locks.put (canonicalPath, lockSet);
            }
            
            if (readOnly)
                ((SharedLockSet) lockSet).readers.add (lock);
            
            return lock;
        }                
    }    
    
    public static void         release (Lock lock) throws IOException {
        final String                canonicalPath = lock.canonicalPath;
        
        synchronized (locks) {
            LockSet                 lockSet = locks.get (canonicalPath);
            
            if (!(lock.readOnly ? lockSet instanceof SharedLockSet : lockSet instanceof ExclusiveLockSet))
                throw new IllegalArgumentException ("Invalid lock; lockSet = " + lockSet, lock);
            
            boolean                 removeFileLock;
                
            if (lock.readOnly) {
                SharedLockSet       sls = (SharedLockSet) lockSet;
                
                if (!sls.readers.remove (lock))
                    throw new IllegalArgumentException ("Invalid lock", lock);
                
                removeFileLock = sls.readers.isEmpty ();                
            }
            else {
                ExclusiveLockSet    els = (ExclusiveLockSet) lockSet;
                
                if (els.writer != lock)
                    throw new IllegalArgumentException ("Invalid lock", lock);
                
                removeFileLock = true;
            }
            
            if (removeFileLock) {
                try {
                    lockSet.fileLock.release();
                } catch (ClosedChannelException x) {
                }
                
                locks.remove (canonicalPath);
            }
        }
    }
}
