package deltix.util.csvx;

import deltix.util.csvx.*;
import deltix.util.io.CSV;
import java.io.*;

//TMP
/**
 *
 */
public class CSVFilter {
    
    public static void main (String [] args) throws Exception {
        CSVXReader  in = new CSVXReader (new File (args [0]));
        Writer      out = new BufferedWriter (new FileWriter (args [1]));
        int []      pass = { 0, 1, 2, 3, 4, 5, 6, 9, 10, 15 };
        int         npass = pass.length;
        
        while (in.nextLine ()) {
            for (int ii = 0; ii < npass; ii++) {
                if (ii > 0)
                    out.write (',');

                int     colIdx = pass [ii];
                
                CSV.printCell (in.getCell (colIdx, false), out);
            }
            
            out.write ('\n');
        }
        
        out.close ();
        in.close ();
    }
}
