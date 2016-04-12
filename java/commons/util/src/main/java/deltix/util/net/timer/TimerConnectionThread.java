package deltix.util.net.timer;

import deltix.util.lang.Util;
import deltix.util.memory.*;
import java.io.*;
import java.net.*;
import java.util.logging.*;

/**
 *
 */
public class TimerConnectionThread extends Thread {
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
        Util.LOGGER.info (s.getRemoteSocketAddress () + " connected.");
                                
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
                Util.LOGGER.info (s.getRemoteSocketAddress () + " disconnected");
            else
                Util.LOGGER.log (Level.WARNING, null, x);
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
