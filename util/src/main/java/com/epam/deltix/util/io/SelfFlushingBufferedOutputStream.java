package com.epam.deltix.util.io;

import java.io.*;

/**
 *
 */
public final class SelfFlushingBufferedOutputStream extends BufferedOutputStream { //Class is final because constructor creates a thread
    private int                     flushInterval = 1;
    private Throwable               exception = null;
    
    class Flusher extends Thread {
        Flusher () {
            super ("Flusher Thread for " + SelfFlushingBufferedOutputStream.this);
            setDaemon (true);
        }
        
        @Override
        public void         run () {
            for (;;) {
                int     interval;
                
                synchronized (SelfFlushingBufferedOutputStream.this) { 
                    interval = flushInterval;

                    if (exception == null && count > 0)
                        try {
                            flush ();                            
                        } catch (Throwable x) {
                            exception = x;
                        }      
                }
                
                try {
                    Thread.sleep (interval);
                } catch (InterruptedException x) {
                    break;
                }
            }
        }
    };
        
    private Flusher                 flusher = new Flusher ();
    
    public SelfFlushingBufferedOutputStream (OutputStream out, int size) {
        super (out, size);
        flusher.start ();
    }

    public SelfFlushingBufferedOutputStream (OutputStream out) {
        this (out, 8192);
    }

    public synchronized int         getFlushInterval () {
        return flushInterval;
    }

    public synchronized void        setFlushInterval (int flushInterval) {
        this.flushInterval = flushInterval;
    }
    
    private void                    checkException () throws IOException {
    	assert Thread.holdsLock(this);
    	
        if (exception != null) {
            Throwable   x = exception;
            
            exception = null;
            
            if (x instanceof IOException)
                throw (IOException) x;
            
            if (x instanceof Error)
                throw (Error) x;
            
            if (x instanceof RuntimeException)
                throw (RuntimeException) x;
            
            throw new RuntimeException ("Unexpected exception type", x);
        }
    }

    @Override
    public final synchronized void        flush () throws IOException {
        checkException ();
        super.flush ();
    }

    @Override
    public synchronized void        write (int b) throws IOException {
        checkException ();
        super.write (b);
    }

    @Override
    public synchronized void        write (byte[] b, int off, int len) 
        throws IOException 
    {
        checkException ();
        super.write (b, off, len);
    }
        
    @Override
    public void                     close () throws IOException {
        if (flusher != null) {
            flusher.interrupt ();        
            flusher = null;
        }
        
        super.close ();
    }    
}
