package com.epam.deltix.util.net.timer;

import com.epam.deltix.util.io.*;
import com.epam.deltix.util.lang.*;
import com.epam.deltix.util.memory.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.*;
import java.net.*;

/**
 *
 */
public class TimerClient extends Thread {
    private final String                host;
    private final int                   port;
    private Socket                      socket;
    private InputStream                 is;
    private final byte []               buffer = new byte [8];
    private volatile long               correction = 0;
    
    public TimerClient (String host) {
        this (host, TimerServer.DEF_PORT);
    }
    
    public TimerClient (String host, int port) {
        super ("Timer Client Thread <-- " + host + ":" + port);
        
        if (host == null)
            host = "localhost";
        
        this.host = host;
        this.port = port;
        
        setDaemon (true);
    }
    
    public long                         nanoTime () {
        return (System.nanoTime () + correction);
    }

    @SuppressFBWarnings("UNENCRYPTED_SOCKET")
    @Override
    public void                         run () {
        try {
            socket = new Socket (host, port);
        
            socket.setTcpNoDelay (true);
            socket.setSoTimeout (0);
            socket.setKeepAlive (true);
            socket.setReuseAddress (true);

            is = socket.getInputStream ();

            for (int ii = 1; ii < TimerServer.WARMUP_NUM; ii++)
                applyCorrection ();
            
            for (;;) {
                long        oldCorrection = correction;
                
                applyCorrection ();
                
//                    System.out.println (
//                        "Correction adjustment: " + (correction - oldCorrection)
//                    );
            }             
        } catch (Exception x) {
            Util.handleException (x);
        } finally {
            Util.close (socket);
        }
    }

    private void applyCorrection () throws IOException {
        IOUtil.readFully (is, buffer, 0, 8);
        
        long        st = DataExchangeUtils.readLong (buffer, 0);
        
        correction = st - System.nanoTime ();
    }
}
