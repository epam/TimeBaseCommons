package deltix.util.io;

import deltix.util.lang.Util;
import java.io.*;
import java.util.zip.*;

/**
 *
 */
public class Flater {
    public static void      deflateUnbuffered (InputStream is, OutputStream os) 
        throws IOException, InterruptedException    
    {
        StreamPump.pump (
            is,
            new GZIPOutputStream (os, 1 << 16)
        );
    }
    
    public static void      deflate (File from, File to) 
        throws IOException, InterruptedException
    {
        FileInputStream         fis = null;
        FileOutputStream        fos = null;
        
        try {
            fis = new FileInputStream (from);
            fos = new FileOutputStream (to);
            
            deflateUnbuffered (fis, fos);
            
            fis.close ();
            fis = null;
            fos.close ();
            fos = null;
        } finally {
            Util.close (fis);
            Util.close (fos);
        }
    }
    
    public static void      inflateUnbuffered (InputStream is, OutputStream os) 
        throws IOException, InterruptedException    
    {
        StreamPump.pump (
            new GZIPInputStream (is, 1 << 16),
            os
        );
    }
    
    public static void      inflate (File from, File to) 
        throws IOException, InterruptedException
    {
        FileInputStream         fis = null;
        FileOutputStream        fos = null;
        
        try {
            fis = new FileInputStream (from);
            fos = new FileOutputStream (to);
            
            inflateUnbuffered (fis, fos);
            
            fis.close ();
            fis = null;
            fos.close ();
            fos = null;
        } finally {
            Util.close (fis);
            Util.close (fos);
        }
    }
    
    public static void main (String [] args) throws Exception {
        String  fromName = args [0];
        boolean fromGZ = fromName.toLowerCase ().endsWith (".gz");
        String  toName;
        File    to;
        boolean toGZ;
        
        if (args.length == 1) {
            toGZ = !fromGZ;
            
            if (fromGZ)
                toName = fromName.substring (0, fromName.length () - 3);
            else
                toName = fromName + ".gz";
            
            
        }
        else {
            toName = args [1];
            toGZ = toName.toLowerCase ().endsWith (".gz");
        
            if (fromGZ == toGZ)
                throw new IllegalArgumentException ("One (and only one) file must end with .gz");
        }
                
        File    from = new File (fromName);
        to = new File (toName);
        long    t0 = System.currentTimeMillis ();
        
        if (fromGZ) {
            System.out.println ("Inflating " + fromName + " ==> " + toName + " ...");
            inflate (from, to);
        }
        else {
            System.out.println ("Deflating " + fromName + " ==> " + toName + " ...");
            deflate (from, to);
        }
        
        long    t1 = System.currentTimeMillis ();
        System.out.println ("Done in " + (t1 - t0) * 0.001 + " seconds.");
            
    }
}
