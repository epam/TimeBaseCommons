package deltix.snmp.script;

import deltix.util.io.ByteArrayOutputStreamEx;
import deltix.util.lang.Util;
import java.net.*;
import java.io.*;

/**
 *
 */
public class NetUtils {
    public static final NetUtils    INSTANCE = new NetUtils ();
    
    private NetUtils () { }
    
    public void             checkUrl (String s) 
        throws IOException, InterruptedException 
    {
        getUrl (s, 1);
    }
    
    public byte []          getUrl (String s) 
        throws IOException, InterruptedException         
    {
        return (getUrl (s, Integer.MAX_VALUE));
    }
    
    public byte []          getUrl (String s, int size) 
        throws IOException, InterruptedException 
    {
        URL                     url = new URL (s);        
        InputStream             is = url.openStream ();
        
        try {            
            ByteArrayOutputStreamEx bos = new ByteArrayOutputStreamEx ();
            
            for (;;) {
                int             cur = bos.size ();
                int             rem = size - cur;
                
                if (rem <= 0)
                    break;
                
                int             n = Math.min (4096, rem);
                
                bos.ensureCapacity (cur + n);
                
                int             numRead = 
                    is.read (bos.getInternalBuffer (), cur, n);
                
                if (numRead < 0)
                    break;
                
                bos.reset (cur + numRead);                                
            }
            
            return (bos.toByteArray ());
        } finally {
            Util.close (is);
        }
    }
    
    public static void      main (String [] args) throws Exception {
        byte []         bytes = new NetUtils ().getUrl ("ftp://ftp.funet.fi/pub/standards/RFC/rfc959.txt", 40);
        
        for (byte b : bytes) 
            System.out.print ((char) b);        
    }
}
