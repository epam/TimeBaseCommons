package deltix.util.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPNull {


    public static void main (String [] args) throws Exception {
        if (args.length != 1) {
            System.out.println ("Command line args: <bind port>");
            return;
        }

        int                 bindPort = Integer.parseInt (args [0]);

        ServerSocket ss = new ServerSocket (bindPort);

        System.out.println ("Null is listening on port " + ss.getLocalPort ());

       while (true) {
            Socket s = ss.accept ();
            final InputStream is = s.getInputStream();
            new Thread() {
                @Override
                public void         run () {
                    byte []     buf = new byte [8192];

                    try {
                        while (true) {
                            int     numRead = is.read (buf);

                            if (numRead < 0)
                                break;
                        }
                    } catch (IOException iox) {
                        iox.printStackTrace ();
                    }
                }
            }.start ();
        }
    }
}
