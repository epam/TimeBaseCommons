/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.util.memory;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;
import com.epam.deltix.util.lang.Disposable;
import com.epam.deltix.util.lang.Util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;


/**
 *
 */
public class DisposableResourceTracker implements Disposable {
    private static final Log LOG = LogFactory.getLog(DisposableResourceTracker.class);
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
            LOG.error ("%s was never closed: %s").with(mCreationStackTrace.getMessage ()).with(mCreationStackTrace);
            close ();
        }
        
        super.finalize ();
        
        Util.close(mResource);
    }
    

}