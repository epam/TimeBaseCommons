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
        if (args.length == 0) {
            System.out.println ("Command line args:\n\t<bind port> <proxy host> <proxy port>\nOR\n\t<src host> <src port> <proxy host> <proxy port>");
            return;
        }

        if (args.length == 3) {
            int bindPort = Integer.parseInt (args [0]);
            String proxyHost = args[1];
            int proxyPort = Integer.parseInt (args [2]);


            if (Boolean.getBoolean("reverse"))
                runServerReverse(bindPort, proxyHost, proxyPort);
            else
                runServer(bindPort, proxyHost, proxyPort);

        } else {
            String srcHost = args [0];
            int srcPort = Integer.parseInt (args [1]);
            String proxyHost = args[2];
            int proxyPort = Integer.parseInt (args [3]);
            runClient(srcHost, srcPort, proxyHost, proxyPort);
        }
    }

    /** Connects to source host:port and re-transmits received data to proxyHost:proxyPort */
    private static void runClient(String srcHost, int srcPort, String proxyHost, int proxyPort) throws IOException {
        Socket input = new Socket (srcHost, srcPort);
        input.setKeepAlive(true);
        input.setTcpNoDelay(true);
        Socket output = new Socket (proxyHost, proxyPort);
        output.setKeepAlive(true);
        output.setTcpNoDelay(true);

        System.out.println ("TCPProxy client is ready to forward traffic from " + input + " to " + output);

        new Proxy(input.getInputStream (), output.getOutputStream ()).start();
    }

    /** Launches server on bindPort and re-transmits received data to proxyHost:proxyPort */
    private static void runServer(int bindPort, String proxyHost, int proxyPort) throws IOException {
        ServerSocket ss = new ServerSocket (bindPort);
        System.out.println ("Proxy is listening on port " + ss.getLocalPort ());
        while (true) {
            Socket input = ss.accept ();
            input.setTcpNoDelay(true);
            System.out.println ("Accepted connection from new source: " + input);
            Socket output = new Socket (proxyHost, proxyPort);
            output.setTcpNoDelay(true);
            new Proxy(input.getInputStream (), output.getOutputStream ()).start();
        }
    }

    /** Connects to proxyHost:proxyPort and re-transmit received data to clients who connect on bindPort */
    private static void runServerReverse(int bindPort, String proxyHost, int proxyPort) throws IOException {
        ServerSocket ss = new ServerSocket (bindPort);
        System.out.println ("ReverseProxy is listening on port " + ss.getLocalPort ());
        while (true) {
            Socket output = ss.accept ();
            output.setTcpNoDelay(true);
            System.out.println ("Accepted connection from new destination: " + output);
            Socket input = new Socket (proxyHost, proxyPort);
            input.setTcpNoDelay(true);
            new Proxy(input.getInputStream (), output.getOutputStream ()).start();
        }
    }
}
