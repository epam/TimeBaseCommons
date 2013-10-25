package deltix.util.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPProxy {
    static class Proxy extends Thread {
        private InputStream mIn;
        private OutputStream        mOut;

        public Proxy(InputStream is, OutputStream os)
            throws IOException
        {
            mIn = is;
            mOut = os;
        }

        public void         run () {
            byte []     buf = new byte [8192];

            try {
                while (true) {
                    int     numRead = mIn.read (buf);

                    if (numRead < 0)
                        break;

                    mOut.write (buf, 0, numRead);
                    mOut.flush ();
                }
            } catch (IOException iox) {
                iox.printStackTrace ();
            }
        }
    }

    public static void main (String [] args) throws Exception {
        if (args.length != 3) {
            System.out.println ("Command line args: <bind port> <proxy host> <proxy port>");
            return;
        }

        int                 bindPort = Integer.parseInt (args [0]);
        String              proxyHost = args[1];
        int                 proxyPort = Integer.parseInt (args [2]);

        ServerSocket ss = new ServerSocket (bindPort);

        System.out.println ("Proxy is listening on port " + ss.getLocalPort ());


        while (true) {
            Socket s = ss.accept ();

            Socket          delegate = new Socket (proxyHost, proxyPort);

            new Proxy(s.getInputStream (), delegate.getOutputStream ()).start();
        }
    }

}
