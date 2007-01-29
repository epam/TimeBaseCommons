package deltix.util.concurrent;

/**
 *  An abstract source of data.
 */
public interface AsynchronousDataSource {
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
     *  Performs a <i>non-blocking</i> check for data availability.
     *
     *  @return             True if data is available, false if not.
     */
    public boolean      isDataAvailable ();
    
    /**
     *  Adds Runnable to be notified of data availability
     */
    public void         addNotificationListener (Runnable l);
    
    /**
     *  Removes a Runnable previously registered with {@link #addNotificationListener}
     */
    public void         removeNotificationListener (Runnable l);
    
}
