package deltix.util.io;

import deltix.util.lang.Util;
import deltix.util.concurrent.UncheckedInterruptedException;
import deltix.util.io.UncheckedIOException;
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
public class GUID {
    public final long           time;
    public final int            port;
    
    public GUID () {
        ServerSocket        socket = null;
        
        try {
            socket = new ServerSocket ();
        
            socket.bind (null);
            //
            //  Sleep for 2 ticks to prevent the (extremely unlikely) situation
            //  where somebody else owned the port for a fraction of the previous tick.
            //
            Thread.sleep (2);

            //  Get a time at which we definitely owned the socket ...
            time = System.currentTimeMillis () - 1;
            port = socket.getLocalPort ();
        } catch (InterruptedException x) {
            throw new UncheckedInterruptedException (x);
        } catch (IOException x) {
            throw new UncheckedIOException (x);
        } finally {
            IOUtil.close (socket);
        }
    }
    
    public void                 writeTo (OutputStream out) throws IOException {
        DataOutputStream    dos = new DataOutputStream (out);
        dos.writeLong (time);
        dos.writeShort (port);
        dos.flush ();
    }
    
    public String               toString () {
        return (port + "_" + time);
    }
    
    public static void          writeLocalIPAddress (OutputStream out) 
        throws IOException 
    {        
        InetAddress         addr = InetAddress.getLocalHost ();
        byte []             addressBytes = addr.getAddress ();
        
        out.write (addressBytes);
    }
    
    public String               toStringWithLocalIPAddress () {
        StringBuilder       s = new StringBuilder ();
        
        InetAddress         addr;
        
        try {
            addr = InetAddress.getLocalHost ();
        } catch (UnknownHostException x) {
            throw new UncheckedIOException (x);
        }
        
        byte []             addressBytes = addr.getAddress ();

        for (byte b : addressBytes) {
            s.append (((int) b) & 0xFF);
            s.append ('_');
        }
        
        s.append (port);
        s.append ('_');
        s.append (time);
        
        return (s.toString ());
    }
    

}
