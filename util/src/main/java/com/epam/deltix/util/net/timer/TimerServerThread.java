package com.epam.deltix.util.net.timer;

import com.epam.deltix.util.lang.*;
import java.io.*;
import java.net.*;

/**
 *
 */
public class TimerServerThread extends Thread {
    private final ServerSocket       ss;

    public TimerServerThread (int port) throws IOException {
        super ("Timer Server Thread on port " + port);
        
        ss = new ServerSocket (port);
        ss.setReuseAddress (true);
    }

    @Override
    public void run () {            
        System.out.println ("Timer Server is up on port " + ss.getLocalPort ());
                
        try {
            for (;;) {
            
                Socket              s = ss.accept ();
                
                new TimerConnectionThread (s).start ();        
            }
        } catch (Throwable x) {
            Util.handleException (x);
        } finally {
            Util.close (ss);
        }        
    }    
}
