package deltix.util.net;

import java.io.*;
import java.net.*;

import deltix.util.*;

/**
 *
 */
public class TCPLogger {
    static class Tee extends Thread {
        private OutputStream        mLog;    
        private InputStream         mIn;
        private OutputStream        mOut;
    
        public Tee (String id, InputStream is, OutputStream os) 
            throws IOException
        {
            mLog = new FileOutputStream (id);
            mIn = is;
            mOut = os;
        } 
        
        public void         run () {
            byte []     buf = new byte [1024];
            
            try {
                for (;;) {
                    int     numRead = mIn.read (buf);
                    
                    if (numRead < 0)
                        break;
                    
                    mOut.write (buf, 0, numRead);
                    mOut.flush ();
                    
                    mLog.write (buf, 0, numRead);
                    mLog.flush ();
                }
            } catch (IOException iox) {
                iox.printStackTrace ();
            }       
            
            Util.close (mLog);
        }
    }

    public static void main (String [] args) throws Exception {
        String              delegateHost = args [0];
        int                 localPort = Integer.parseInt (args [1]);
        int                 delegatePort = Integer.parseInt (args [2]);
        
        ServerSocket        ss = new ServerSocket (localPort); 

        System.out.println ("Server ready on port " + ss.getLocalPort ());

        int                 num = 0;
        
        for (;;) {
            Socket          s = ss.accept ();
            num++;
            
            Socket          delegate = new Socket (delegateHost, delegatePort);
            
            new Tee ("req-" + num + ".txt", s.getInputStream (), delegate.getOutputStream ()).start ();
            new Tee ("resp-" + num + ".txt", delegate.getInputStream (), s.getOutputStream ()).start ();
        }            
    }
}
