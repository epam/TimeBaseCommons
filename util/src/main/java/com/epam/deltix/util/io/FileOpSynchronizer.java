package com.epam.deltix.util.io;

import java.io.*;
import java.util.*;
import java.nio.channels.*;

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

    private static final int    EXCLUSIVE_LOCK = -1;

    private static class LockRecord {
        final String            globalPath;
        final RandomAccessFile  raf;
        final FileLock          lock;
        int                     count;

        public LockRecord (String globalPath, RandomAccessFile raf, boolean shared)
            throws IOException
        {
            this.globalPath = globalPath;
            this.raf = raf;
            this.lock = raf.getChannel ().lock (0, Long.MAX_VALUE, shared);;
            this.count = shared ? 1 : EXCLUSIVE_LOCK;
        }
    }

    /**
     *  Maps file path to whether the number of shared locks
     */
    private static final HashMap <String, LockRecord>    locks =
        new HashMap <String, LockRecord> ();

    private static LockRecord   getLock (
        String                      globalPath,
        RandomAccessFile            raf,
        boolean                     shared
    )
        throws InterruptedException, IOException
    {
        synchronized (locks) {
            //
            //  Get the current lock status
            //
            LockRecord  record;

            for (;;) {
                record = locks.get (globalPath);

                if (record == null) {
                    //  Get the OS lock ...
                    record = new LockRecord (globalPath, raf, shared);

                    locks.put (globalPath, record);

                    return (record);
                }

                if (shared && record.count != EXCLUSIVE_LOCK) {
                    record.count++;

                    return (record);
                }
                   
                // wait for locks to be released
                locks.wait ();
            }           
        }
    }

    private static void         releaseLock (LockRecord record)
        throws InterruptedException, IOException
    {
        synchronized (locks) {
            if (record.count == EXCLUSIVE_LOCK || record.count == 1) {
                record.raf.close ();
                locks.remove (record.globalPath);
                locks.notifyAll ();
            }
            else
                record.count--;
        }
    }

    public static void          perform (
        File                        f,
        Operation                   op,
        boolean                     shared
    )
        throws IOException, InterruptedException
    {
        String              globalPath = f.getCanonicalPath ();
        RandomAccessFile    raf = new RandomAccessFile (f, shared ? "r" : "rw");        
        LockRecord          record = getLock (globalPath, raf, shared);
        
        try {
            op.perform (f, raf);
        } finally {
            releaseLock (record);

            if (raf != record.raf)
                raf.close ();
        }        
    }
}
