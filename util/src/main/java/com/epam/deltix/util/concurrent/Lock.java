package com.epam.deltix.util.concurrent;

import java.util.*;

import com.epam.deltix.util.lang.Disposable;

/**
 *
 */
public final class Lock extends Throwable implements Disposable {
    public interface Manager {
        public Object           getSyncObject ();
        
        public void             release (Lock lock);
    }
    
    private Manager         mParent;
    private Thread              mThread = Thread.currentThread ();        
    private long                mCreationTime = System.currentTimeMillis ();
    private boolean             mIsShared;
    private boolean             mIsClosed = false;

    public Lock (Manager parent, boolean isShared) {
        mParent = parent;
        mIsShared = isShared;
    }

    public void                 close () {
        synchronized (mParent.getSyncObject ()) {
            if (mIsClosed) 
                throw new IllegalStateException (this + " is already closed");

            mParent.release (this);
            mIsClosed = true;
        }
    }        

    public boolean              isShared () {
        return (mIsShared);
    }

    public Thread               getCreatorThread () {
        return (mThread);
    }

    public long                 getCreationTime () {
        return (mCreationTime);
    }

    public Thread               getCreator () {
        return (mThread);
    }

    @Override
    public String               toString () {
        return (
            (mIsShared ? "Shared" : "Exclusive") +
            " lock obtained by " + mThread.getName () + " at " + 
            new Date (mCreationTime)
        );
    }
}
