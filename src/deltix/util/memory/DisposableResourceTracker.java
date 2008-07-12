package deltix.util.memory;

import deltix.util.lang.Util;
import deltix.util.lang.Disposable;
import java.io.*;
import java.util.*;
import java.util.logging.*;

import deltix.util.*;

/**
 *
 */
public class DisposableResourceTracker implements Disposable {
    private static HashSet <DisposableResourceTracker>  mOpenResources =
        new HashSet <DisposableResourceTracker> ();
    
    private Disposable              mResource;
    private Throwable               mCreationStackTrace;
    
    public DisposableResourceTracker () {
        this (null);
    }
    
    public DisposableResourceTracker (Disposable id) {
        mResource = id;
        
        if (id == null)
            id = this;
        
        mCreationStackTrace = new Throwable (id + " allocated below");
        
        synchronized (mOpenResources) {
            mOpenResources.add (this);
        }
    }
    
    public static void              dumpOpenResources (PrintStream ps) {
        synchronized (mOpenResources) {
            for (DisposableResourceTracker dtt : mOpenResources) 
                dtt.dump (ps);
        }
    }
    
    public void                     dump (PrintStream ps) {
        mCreationStackTrace.printStackTrace (ps);
    }
    
    public void                     close () {
        synchronized (mOpenResources) {
            mOpenResources.remove (this);
        }
        
        mCreationStackTrace = null;
    }
    
    protected void                  finalize () 
        throws Throwable 
    {
        if (mCreationStackTrace != null) {
            Util.LOGGER.log (
                Level.SEVERE,
                mCreationStackTrace.getMessage () + " was never closed",
                mCreationStackTrace
            );

            close ();
        }
        
        super.finalize ();
        
        mResource.close ();
    }
    

}
