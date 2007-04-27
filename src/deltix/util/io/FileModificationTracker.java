package deltix.util.io;

import java.io.*;
import java.util.logging.*;
import java.util.*;

import deltix.util.Util;

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
    
    public final void               checkModifiedLogExceptions () {
        try {
            checkModified ();
        } catch (Throwable x) {
            Util.LOGGER.log (Level.SEVERE, "Uncaught: " + x, x);
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
        Util.GLOBAL_TIMER.schedule (mTask, delay, period);
    }
    
    public final void               cancelPeriodicCheck () {
        if (mTask != null) {
            mTask.cancel ();
            mTask = null;
        }
    }
}
