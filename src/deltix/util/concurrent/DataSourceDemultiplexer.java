package deltix.util.concurrent;

import java.util.*;
import java.util.logging.*;

import deltix.util.Util;

/**
 *  Allows a single thread to receive data from multiple
 *  data sources. Note that the public methods of this class are NOT designed to 
 *  be called concurrently, although the object itself is designed to manage
 *  concurrent processes.
 */
public class DataSourceDemultiplexer <T extends AsynchronousDataSource> {
    private Runnable            mLock = 
        new NotifyingRunnable ();
        
    private Set <T>             mDataSources =
        new HashSet <T> ();
    
    public DataSourceDemultiplexer () {        
    }
    
    public void                 add (T ds) {
        if (mDataSources.add (ds))
            ds.addNotificationListener (mLock);
    }
    
    public void                 remove (T ds) {
        if (mDataSources.remove (ds))
            ds.removeNotificationListener (mLock);
    }

    /**
     *  Returns a read-only set of registered data sources
     */
    public Set <T>              dataSources () {
        return (Collections.unmodifiableSet (mDataSources));
    }
    
    public int                  getNumDataSources () {
        return (mDataSources.size ());
    }
    /**
     *  If one of registered data sources has data available, return it.
     *  Otherwise, return null.
     */
    public T                    getSourceWithAvailableDataNoBlocking () {
        for (T ds : mDataSources)
            if (ds.isDataAvailable ())
                return (ds);
        
        return (null);
    }
    
    public T                    getSourceWithAvailableData (long timeout) 
        throws InterruptedException
    {
        boolean         firstTime = true;
        long            limit = 0;
        long            waitTimeout;
        
        synchronized (mLock) {
            for (;;) {               
                T           ds = getSourceWithAvailableDataNoBlocking ();

                if (ds != null)
                    return (ds);

                if (firstTime) {
                    if (timeout <= 0)
                        return (null);
                    
                    limit = System.currentTimeMillis () + timeout;
                    waitTimeout = timeout;
                    firstTime = false;
                }
                else {
                    waitTimeout = limit - System.currentTimeMillis ();
                    
                    if (waitTimeout <= 0)
                        return (null);                
                }
                                
                mLock.wait (waitTimeout);
            }
        }
    }
}
