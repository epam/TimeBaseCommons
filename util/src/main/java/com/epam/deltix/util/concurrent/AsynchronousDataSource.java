package com.epam.deltix.util.concurrent;

/**
 *  An abstract source of data, allowing a single consumer to
 *  perform work while the data is not available, or to multiplex
 *  multiple data sources.
 */
public interface AsynchronousDataSource {
    public void         addAvailabilityListener (Runnable listener);
    
    public void         removeAvailabilityListener (Runnable listener);
    
    /**
     *  Performs a non-blocking check for data availability.
     *
     *  @param timeout      Maximum time to wait until data is available, 
     *                      in milliseconds. The value of 0 causes a non-blocking
     *                      check to be performed.
     *  @return             True if data is available, false if timeout has expired.
     */
    public boolean      isDataAvailable (long timeout)
        throws InterruptedException;
    
    /**
     *  Performs a non-blocking check for data availability.
     *
     *  @return             If data is available.
     */
    public boolean      isDataAvailable ();    
}
