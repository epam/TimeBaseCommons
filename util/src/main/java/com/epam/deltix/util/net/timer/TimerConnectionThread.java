package com.epam.deltix.util.net.timer;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;
import com.epam.deltix.util.lang.Util;
import com.epam.deltix.util.memory.*;
import java.io.*;
import java.net.*;

/**
 *
 */
public class TimerConnectionThread extends Thread {
    private static final Log            LOG = LogFactory.getLog(Util.class);
    private final Socket                s;
    private final OutputStream          os;
    private final byte []               buffer = new byte [8];
    
    public TimerConnectionThread (Socket s) throws IOException {
        super ("Timer Connection Thread from " + s);
        
        this.s = s;
        this.os = s.getOutputStream ();
    }

    @Override
    public void                         run () {   
        LOG.info ( "%s connected.").with(s.getRemoteSocketAddress ());
                                
        try {
            for (int ii = 0; ii < TimerServer.WARMUP_NUM; ii++) {
                sendTime ();
            }
                        
            for (;;) {
                sendTime ();
                
                Thread.sleep (1000);
            }
        } catch (Exception x) {
            if ((x instanceof SocketException) && x.getMessage ().contains ("reset by peer"))
                LOG.info ("%s disconnected").with(s.getRemoteSocketAddress ());
            else
                LOG.info ("Connection %s produced error %s").with(s.getRemoteSocketAddress ()).with(x);
        } finally {
            Util.close (s);
        }
    } 

    private void sendTime () throws IOException {
        DataExchangeUtils.writeLong (buffer, 0, System.nanoTime ());
        os.write (buffer);
        os.flush ();
    }
}
