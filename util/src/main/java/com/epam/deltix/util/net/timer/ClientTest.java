package com.epam.deltix.util.net.timer;

/**
 *
 */
public class ClientTest {
    public static void          main (String [] args) throws Exception {
        for (;;) {
            System.out.println (SynchronizedTimer.nanoTime ());
            Thread.sleep (2000); 
        }
    }
}
