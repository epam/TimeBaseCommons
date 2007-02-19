package deltix.util.concurrent;

/**
 *  An abstract source of data, allowing a single consumer to
 *  perform work while the data is not available, or to multiplex
 *  multiple data sources.
 */
public interface AsynchronousDataSource {
    /**
     *  Performs a non-blocking check for data availability.
     *
     *  @param timeout      Maximum time to wait until data is available, 
     *                      in milliseconds. The value of 0 causes a non-blocking
     *                      check to be performed.
     *  @param listener     If specified, and if this method returned false, then
     *                      the listener will be notified (once) when data becomes available.
     *                      Implementations of this class only support
     *                      a single listener.
     *  @return             True if data is available, false if timeout has expired.
     */
    public boolean      isDataAvailable (long timeout, Runnable listener)
        throws InterruptedException;
    
    /**
     *  Performs a non-blocking check for data availability.
     *
     *  @param listener     If specified, and if this method returned false, then
     *                      the listener will be notified (once) when data becomes available.
     *                      Implementations of this class only support
     *                      a single listener.
     *  @return             If data is available.
     */
    public boolean      isDataAvailable (Runnable listener);    
}
