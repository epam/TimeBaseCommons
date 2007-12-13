package deltix.util.io;

import java.io.*;

/**
 *
 */
public class Dump {
    public static void            dump (byte [] bytes, int offset, int length) {
        for (int ii = 0; ii < length; ii++) {
            if ((ii & 15) == 0)
                System.out.printf ("%04X: ", ii);
            
            System.out.printf ("%02X ", bytes [ii]);
            
            if ((ii & 15) == 15)
                System.out.println ();
        }
        
        System.out.println ();
    }
    
    public static void            dump (long base, byte [] bytes, int offset, int length) {
        for (int ii = 0; ii < length; ii++) {
            if ((ii & 15) == 0)
                System.out.printf ("%020d: ", base + ii);
            
            System.out.printf ("%02X ", bytes [ii]);
            
            if ((ii & 15) == 15)
                System.out.println ();
        }
        
        System.out.println ();
    }    
    
    public static void main (String [] args) throws IOException {
        RandomAccessFile    f = new RandomAccessFile (args [0], "r");
        long                base = Long.parseLong (args [1]);
        f.seek (base);
        byte []             buffer = new byte [Integer.parseInt (args [2])];
        int                 num = f.read (buffer);
        
        dump (base, buffer, 0, num);
    }
}
