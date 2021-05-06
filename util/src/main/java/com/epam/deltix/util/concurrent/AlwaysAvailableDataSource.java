package com.epam.deltix.util.concurrent;

/**
 *  An implementation of AsynchronousDataSource, which always has the data available.
 *  Convenient base class for implementing "always ready" AsynchronousDataSource instances.
 */
public class AlwaysAvailableDataSource implements AsynchronousDataSource {
    /**
     *  Immediately returns <code>true</code>.
     */
    public boolean                      isDataAvailable (long timeout)
        throws InterruptedException
    {
        return (true);
    }
    
    /**
     *  Immediately returns <code>true</code>.
     */
    public boolean                      isDataAvailable () {
        return (true);
    }    

    /**
     *  Does nothing.
     */
    public void                         removeAvailabilityListener (Runnable listener) {
    }

    /**
     *  Does nothing.
     */
    public void                         addAvailabilityListener (Runnable listener) {
    }       
}
