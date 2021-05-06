package com.epam.deltix.util.io;

import com.epam.deltix.util.lang.Util;
import com.epam.deltix.util.time.GlobalTimer;

import java.io.File;
import java.util.TimerTask;

/**
 *
 */
public class FileModificationTracker {
    private File                    mFile;
    private Runnable                mAction = null;
    private TimerTask               mTask = null;
    private long                    mLastModTimeWhenExisted = 0;
    
    public FileModificationTracker (File f) {
        mFile = f;
        mLastModTimeWhenExisted = f.lastModified ();
    }
    
    public FileModificationTracker (File f, Runnable action) {
        this (f);
        setAction (action);
    }
    
    public final void               setAction (Runnable action) {
        mAction = action;
    }
    
    /**
     *  Override this method, or call {@link #setAction}. 
     *  Default implementation runs the Runnable supplied to the {@link #setAction} call.
     */
    public void                     fileWasModified (File f) {
        if (mAction != null)
            mAction.run ();
    }
    
    public final void               checkModified () {
        long        t = mFile.lastModified ();
        
        if (t != 0 && t != mLastModTimeWhenExisted) {
            fileWasModified (mFile);
            mLastModTimeWhenExisted = t;
        }
    }

    public long getLastModTimeWhenExisted() {
        return mLastModTimeWhenExisted;
    }

    public final void               checkModifiedLogExceptions () {
        try {
            checkModified ();
        } catch (Throwable x) {
            Util.logException ("Uncaught exception", x);
        }
    }
    
    public final TimerTask          createTimerTask () {
        return (
            new TimerTask () {
                public void run () {
                    checkModifiedLogExceptions ();
                }
            }
        );
    }
    
    public final void               schedulePeriodicCheck (long delay, long period) {
        cancelPeriodicCheck ();
        mTask = createTimerTask ();
        GlobalTimer.INSTANCE.schedule (mTask, delay, period);
    }
    
    public final void               cancelPeriodicCheck () {
        if (mTask != null) {
            mTask.cancel ();
            mTask = null;
        }
    }
}
