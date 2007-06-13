package deltix.util.io;

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
}
