package deltix.util;

import java.io.*;
import java.net.*;

/**
 *  Globally unique identifier generator, based on the fact that on any system at any
 *  given time the same port cannot be bound to multiple sockets. Therefore, 
 *  the port number of a socket combined with the local system time is unique
 *  within the host system. We add the IP address of the system to this and get
 *  an identifier that is globally unique, unless the clock is set back, or 
 *  the system's IP address is non-unique within the scope of interest.
 */
public abstract class GUID {
    /**
     *  Creates a string consisting of digits and underscore characters, suitable for
     *  use in language identifiers. This method does not throw checked exceptons.
     */
    public static String        createNoX (boolean includeIPAddress) {
        try {
            return (create (includeIPAddress));            
        } catch (InterruptedException ix) {
            throw new RuntimeException ("Unexpected " + ix, ix);
        } catch (IOException iox) {
            throw new RuntimeException ("Unexpected " + iox, iox);
        }
    }
    
    /**
     *  Creates a string consisting of digits and underscore characters, suitable for
     *  use in language identifiers.
     */
    public static String        create (boolean includeIPAddress)
        throws IOException, InterruptedException 
    {
        ServerSocket        socket = new ServerSocket ();
        int                 port;
        long                time;
        
        try {
            socket.bind (null);
            //
            //  Sleep for 2 ticks to prevent the (extremely unlikely) situation
            //  where somebody else owned the port for a fraction of the previous tick.
            //
            Thread.sleep (2);

            //  Get a time at which we definitely owned the socket ...
            time = System.currentTimeMillis () - 1;
            port = socket.getLocalPort ();
        } finally {
            socket.close ();
        }
        
        StringBuilder       s = new StringBuilder ();
        
        if (includeIPAddress) {
            InetAddress         addr = InetAddress.getLocalHost ();
            byte []             addressBytes = addr.getAddress ();

            for (byte b : addressBytes) {
                s.append (((int) b) & 0xFF);
                s.append ('_');
            }
        }
        
        s.append (port);
        s.append ('_');
        s.append (time - 1170522000000L);
        
        return (s.toString ());
    }
    
    public static void main (String [] args) throws Exception {
        System.out.println (create (true));
    }
}
