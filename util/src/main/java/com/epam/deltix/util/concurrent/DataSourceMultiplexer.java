package com.epam.deltix.util.concurrent;

import java.util.*;

/**
 *  Allows a single thread to receive data from multiple
 *  data sources. Note that the public methods of this class are NOT designed to 
 *  be called concurrently, although the object itself is designed to manage
 *  concurrent processes. TODO: fix synchronization
 */
public class DataSourceMultiplexer <T extends AsynchronousDataSource> 
    implements AsynchronousDataSource
{
    private Runnable            mLock = 
        new Runnable () {
            public void         run () {
                synchronized (this) {
                    notify ();
                }
                
                fireDataAvailable ();
            }
        };
        
    private Set <T>             mDataSources =
        new HashSet <T> ();
    
    private Set <Runnable>      mListeners = new HashSet <Runnable> ();
    
    public DataSourceMultiplexer () {        
    }
    
    public void                 add (T ds) {
        boolean     ok = mDataSources.add (ds);
        
        if (!ok)
            throw new RuntimeException ("Failed to add " + ds);
        
        ds.addAvailabilityListener (mLock);
    }
    
    public void                 remove (T ds) {
        boolean     ok = mDataSources.remove (ds);
        
        if (!ok)
            throw new RuntimeException ("Failed to remove " + ds);
        
        ds.removeAvailabilityListener (mLock);
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
        synchronized (mLock) {
            for (T ds : mDataSources)
                if (ds.isDataAvailable ())
                    return (ds);
        }
        
        return (null);
    }
    
    public static boolean       isDataAvailableInAll (AsynchronousDataSource ... dss) {
        for (AsynchronousDataSource ds : dss)
            if (!ds.isDataAvailable ())
                return (false);
        
        return (true);
    }
    
    public static boolean       isDataAvailableInAny (AsynchronousDataSource ... dss) {
        for (AsynchronousDataSource ds : dss)
            if (ds.isDataAvailable ())
                return (true);
        
        return (false);
    }
    
    public static boolean       isDataAvailableInAll (long timeout, AsynchronousDataSource ... dss) 
        throws InterruptedException
    {
        boolean             firstTime = true;
        long                limit = 0;
        NotifyingRunnable   lock = new NotifyingRunnable ();
        
        synchronized (lock) {
            try {
                for (AsynchronousDataSource ds : dss)
                    ds.addAvailabilityListener (lock);
                          
                for (;;) {
                    if (isDataAvailableInAll (dss))
                        return (true);

                    long         waitTimeout;
                    
                    if (limit == 0) {
                        if (timeout <= 0)
                            return (false);

                        limit = System.currentTimeMillis () + timeout;
                        waitTimeout = timeout;
                    }
                    else {
                        waitTimeout = limit - System.currentTimeMillis ();

                        if (waitTimeout <= 0)
                            return (false);                
                    }

                    lock.wait (waitTimeout);
                }                                    
            } finally {
                for (AsynchronousDataSource ds : dss)
                    ds.removeAvailabilityListener (lock);
            }
        }
    }
    
    public T                    getSourceWithAvailableData () 
        throws InterruptedException
    {
        synchronized (mLock) {
            for (;;) {               
                T           ds = getSourceWithAvailableDataNoBlocking ();

                if (ds != null)
                    return (ds);

                mLock.wait ();
            }
        }
    }

    public T                    getSourceWithAvailableData (long timeout) 
        throws InterruptedException
    {
        long            limit = 0;
        long            waitTimeout;
        
        synchronized (mLock) {
            for (;;) {               
                T           ds = getSourceWithAvailableDataNoBlocking ();

                if (ds != null)
                    return (ds);

                if (limit == 0) {
                    if (timeout <= 0)
                        return (null);
                    
                    limit = System.currentTimeMillis () + timeout;
                    waitTimeout = timeout;
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

    protected void              fireDataAvailable () {
        if (mListeners != null)
            for (Runnable l : mListeners)
                l.run ();
    }
    
    public void    removeAvailabilityListener (Runnable listener) {
        synchronized (mListeners) {
            mListeners.remove (listener);
        }
    }

    public void    addAvailabilityListener (Runnable listener) {
        synchronized (mListeners) {
            mListeners.add (listener);
        }
    }

    public boolean              isDataAvailable (long timeout) 
        throws InterruptedException 
    {
        return (getSourceWithAvailableData (timeout) != null);
    }

    public boolean              isDataAvailable () {
        return (getSourceWithAvailableDataNoBlocking () != null);
    }
}
