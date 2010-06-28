package deltix.util.io;

import deltix.util.concurrent.UncheckedInterruptedException;
import java.io.*;
import java.net.*;
import java.util.Calendar;

/**
 *  Globally unique identifier generator, based on the fact that on any system at any
 *  given time the same port cannot be bound to multiple sockets. Therefore, 
 *  the port number of a socket combined with the local system time is unique
 *  within the host system. We add the IP address of the system to this and get
 *  an identifier that is globally unique, unless the clock is set back, or 
 *  the system's IP address is non-unique within the scope of interest.
 */
public class GUID {

    private static final long BASE_TIME = 1277741650203L; // 6/28/2010
    private static final Object lock = new Object ();
    private static GUIDSeed seed;
    private static long staticCounter;
    private final String guid;

    public GUID () {
        StringBuilder       s = new StringBuilder ();
        synchronized (lock) {
            if (seed == null)
                seed = new GUIDSeed();

            // first 4 character is port
            // followed by serveral characters that represent local port hold timestamp
            // followed by space as separator
            // followed by process-unique counter
            s.append (String.format ("%04x", seed.port));
            s.append (Long.toString (seed.time - BASE_TIME, Character.MAX_RADIX));
            s.append (' '); // separator
            s.append (Long.toString (++staticCounter, Character.MAX_RADIX));
        }
        guid = s.toString();
    }

    public static void          appendLocalIPAddressToString (StringBuilder s) {
        InetAddress         addr;

        try {
            addr = InetAddress.getLocalHost ();
        } catch (UnknownHostException x) {
            throw new UncheckedIOException (x);
        }

        byte []             addressBytes = addr.getAddress ();

        for (byte b : addressBytes) {
            s.append (String.format ("%02x", b & 0xFF));
        }
    }



    @Override
    public String               toString () {
        return guid;
    }

    public String               toStringWithLocalIPAddress () {
        StringBuilder       s = new StringBuilder ();

        appendLocalIPAddressToString (s);
        s.append (guid);

        return (s.toString ());
    }

    public String               toStringWithPrefix (String prefix) {
        StringBuilder       s = new StringBuilder ();

        s.append(prefix);
        s.append (guid);

        return (s.toString ());
    }
}
