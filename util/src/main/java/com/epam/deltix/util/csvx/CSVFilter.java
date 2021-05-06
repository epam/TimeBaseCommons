package com.epam.deltix.util.csvx;

import com.epam.deltix.util.cmdline.DefaultApplication;
import com.epam.deltix.util.collections.CharSequenceSet;
import com.epam.deltix.util.io.*;
import java.io.*;

public class CSVFilter extends DefaultApplication {
    private Writer          outWriter;        
    private StringWriter    swr = new StringWriter (1000);   
    private StringBuffer    sb = swr.getBuffer ();
    private CSVWriter       out = new CSVWriter (swr);;
    private CharSequenceSet uniq = null;
    
    public CSVFilter (String [] args) {
        super (args);
    }
        
    private void        flushLine () throws IOException {
        if (uniq != null && !uniq.addCharSequence (sb))
            return;
        
        int                 n = sb.length ();

        for (int ii = 0; ii < n; ii++)
            outWriter.write (sb.charAt (ii));

        outWriter.write (IOUtil.CR);            
    }
    
    public void         run () throws Exception {
        File                inFile = getFileArg ("-in");
        File                outFile = getFileArg ("-out");
        char                delimiter = getArgValue ("-d", ",").charAt (0);
        boolean             headers = !isArgSpecified ("-nh");
        boolean             trim = isArgSpecified ("-trim");
        String []           filterHeaders = getArgValues ("-c");
        String              encoding = getArgValue ("-e", "UTF-8");
        int                 numLines = getIntArgValue ("-first", Integer.MAX_VALUE);
            
        if (isArgSpecified ("-u"))
            uniq = new CharSequenceSet (1000);
        
        CSVXReader          in = 
            inFile == null ?
                new CSVXReader (System.in, delimiter, true, "<stdin>") :
                new CSVXReader (inFile, delimiter);
        
        if (headers)
            in.readHeaders ();
        
        int []              sequence;
        
        if (filterHeaders == null)
            sequence = null;
        else {
            sequence = new int [filterHeaders.length];
            
            for (int ii = 0; ii < filterHeaders.length; ii++) {
                String      spec = filterHeaders [ii];
                
                if (headers)
                    sequence [ii] = in.getHeaderIndexEx (spec);
                else
                    sequence [ii] = Integer.parseInt (spec);
            }
        }
        
        OutputStream        os =
            outFile == null ? 
                System.out : 
                new BufferedOutputStream (new FileOutputStream (outFile));
                
        outWriter = new OutputStreamWriter (os, encoding);        
        
        if (headers) {
            if (sequence == null)
                out.writeCells ((Object []) in.getHeaders ());
            else 
                out.writeCells ((Object []) filterHeaders);
            
            outWriter.write (sb.toString ());
            outWriter.write (IOUtil.CR);
        }
        
        while (in.nextLine () && in.getLineNumber () < numLines) {
            sb.setLength (0);
            
            int             num = 
                sequence == null ? in.getNumCells () : sequence.length;
            
            for (int ii = 0; ii < num; ii++) {
                if (ii > 0)
                    out.writeSeparator ();

                int     colIdx = 
                    sequence == null ? ii : sequence [ii];
                
                out.writeCell (in.getCell (colIdx, trim));
            }
            
            flushLine ();
        }
        
        outWriter.close ();
        in.close ();
    }
    
    public static void main (String [] args) throws Exception {
        new CSVFilter (args).start ();
    }

}
