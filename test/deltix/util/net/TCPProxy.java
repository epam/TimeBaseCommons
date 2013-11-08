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
            runServer(bindPort, proxyHost, proxyPort);
        } else {
            String srcHost = args [0];
            int srcPort = Integer.parseInt (args [1]);
            String proxyHost = args[2];
            int proxyPort = Integer.parseInt (args [3]);
            runClient(srcHost, srcPort, proxyHost, proxyPort);
        }
    }

    private static void runClient(String srcHost, int srcPort, String proxyHost, int proxyPort) throws IOException {
        Socket input = new Socket (srcHost, srcPort);
        input.setKeepAlive(true);
        input.setTcpNoDelay(true);
        Socket output = new Socket (proxyHost, proxyPort);
        output.setKeepAlive(true);
        output.setTcpNoDelay(true);

        System.out.println ("Connected and ready");

        new Proxy(input.getInputStream (), output.getOutputStream ()).start();
    }

    private static void runServer(int bindPort, String proxyHost, int proxyPort) throws IOException {
        ServerSocket ss = new ServerSocket (bindPort);
        System.out.println ("Proxy is listening on port " + ss.getLocalPort ());
        while (true) {
            Socket input = ss.accept ();
            Socket output = new Socket (proxyHost, proxyPort);
            System.out.println ("Accepted " + output);
            new Proxy(input.getInputStream (), output.getOutputStream ()).start();
        }
    }

}
