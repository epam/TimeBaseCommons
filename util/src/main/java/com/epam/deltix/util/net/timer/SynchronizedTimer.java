package com.epam.deltix.util.net.timer;

/**
 *
 */
public abstract class SynchronizedTimer {
    private static final TimerClient    client;
    
    static {
        client = new TimerClient (System.getenv ("DTSERVER"));
        client.start ();
    }
    
    public static long                  nanoTime () {
        return (client.nanoTime ());
    }
}
