package deltix.util.io;

import java.io.*;

/**
 *
 */
public class EncodingConverter {
    public static void main (String [] args) throws Exception {
        if (args.length == 1) {
            FileInputStream     fis = new FileInputStream (args [0]);

            for (int ii = 0; ii < 40; ii++) {
                int     b = fis.read ();

                if (b < 0)
                    break;
                
                System.out.printf ("#%2d: %x [%c]\n", ii, b, (char) b);
            }

            fis.close ();
        }
        else if (args.length == 4) {
            long            numDone = 0;
            long            nextRep = 1 << 20;
            BufferedReader  from = new BufferedReader (new InputStreamReader (new FileInputStream (args [0]), args [1]));
            BufferedWriter  to = new BufferedWriter (new OutputStreamWriter (new FileOutputStream (args [2]), args [3]));
            char []         cbuf = new char [64000];
            
            for (;;) {
                int         n = from.read (cbuf);

                if (n < 0)
                    break;

                to.write (cbuf, 0, n);
                
                numDone += n;

                if (numDone > nextRep) {
                    System.out.printf ("%,d chars done...\r", numDone);
                    nextRep = numDone + 1 << 20;
                }
            }

            System.out.printf ("Finished; %,d chars.\n", numDone);

            from.close ();
            to.close ();
        }
        else
            throw new IllegalArgumentException ("Need 1 or 4 args");
    }
}
