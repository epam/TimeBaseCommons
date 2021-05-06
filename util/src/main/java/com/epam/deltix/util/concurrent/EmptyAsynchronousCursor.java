package com.epam.deltix.util.concurrent;

import com.epam.deltix.util.memory.*;

/**
 *  An AsynchronousCursor that returns no frames.
 */
public class EmptyAsynchronousCursor 
    implements AsynchronousCursor
{
    private DisposableResourceTracker   mTracker;
    private boolean                     mNextWasCalled = false;
    
    public EmptyAsynchronousCursor () {
        mTracker = new DisposableResourceTracker (this);
    }
    
    public void close () {
        if (mTracker != null) {
            mTracker.close ();
            mTracker = null;
        }
    }
    
    public void             removeAvailabilityListener (Runnable listener) {
    }

    public void             addAvailabilityListener (Runnable listener) {
    }

    public boolean          isDataAvailable (long timeout) throws InterruptedException {
        return (true);
    }

    public boolean          isDataAvailable () {
        return (true);
    }
    
    public boolean          next () {
        if (isAtEnd ())
            throw new IllegalStateException ("Cursor is at end");
        
        mNextWasCalled = true;
        return (false);
    }
    
    public boolean          isAtBeginning () {
        return (!mNextWasCalled);
    }

    public boolean          isAtEnd () {
        return (mNextWasCalled);
    }

    
}
