package deltix.util.memory;

import deltix.util.lang.Util;
import deltix.util.lang.Disposable;
import java.io.*;
import java.util.*;
import java.util.logging.*;


/**
 *
 */
public class DisposableResourceTracker implements Disposable {
    private static final HashSet <DisposableResourceTracker>  mOpenResources =
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

    public Disposable               getResource () {
        return mResource;
    }

    public static DisposableResourceTracker []  getOpenResources () {
        synchronized (mOpenResources) {
            return (mOpenResources.toArray (new DisposableResourceTracker [mOpenResources.size ()]));
        }
    }

    public static void              dumpOpenResources () {
        dumpOpenResources (System.out);
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

    public String                   dumpToString () {
        StringWriter    swr = new StringWriter ();
        PrintWriter     pwr = new PrintWriter (swr);

        dump (pwr);

        pwr.close ();
        return (swr.toString ());
    }

    public static String            dumpOpenResourcesToString () {
        StringWriter    swr = new StringWriter ();
        PrintWriter     pwr = new PrintWriter (swr);

        dumpOpenResources (pwr);

        pwr.close ();
        return (swr.toString ());
    }

    public static void              dumpOpenResources (PrintWriter ps) {
        synchronized (mOpenResources) {
            for (DisposableResourceTracker dtt : mOpenResources)
                dtt.dump (ps);
        }
    }

    public void                     dump (PrintWriter ps) {
        mCreationStackTrace.printStackTrace (ps);
    }

    public void                     close () {
        synchronized (mOpenResources) {
            mOpenResources.remove (this);
        }
        
        mCreationStackTrace = null;
        mResource = null;
    }
    
    @Override
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
