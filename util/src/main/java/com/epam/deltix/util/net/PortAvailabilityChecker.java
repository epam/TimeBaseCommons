package com.epam.deltix.util.net;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 */
public class PortAvailabilityChecker {
    public static boolean       isPortAvailable (int n) {
        try {
            ServerSocket    ss = new ServerSocket (n);

            ss.close ();

            return (true);
        } catch (java.net.BindException x) {
            return (false);
        } catch (IOException iox) {
            throw new com.epam.deltix.util.io.UncheckedIOException(iox);
        }
    }

    public static void main (String [] args) {
        System.out.println (isPortAvailable (Integer.parseInt (args [0])));
    }
}
