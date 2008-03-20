package deltix.util.io;

import java.io.*;

/**
 *  Helper for writing correctly formatted CSV files.
 */
public class CSVWriter extends FilterWriter {
    public CSVWriter (File f) throws IOException {
        super (new BufferedWriter (new FileWriter (f)));
    }
    
    public CSVWriter (Writer out) {
        super (out);
    }
    
    public CSVWriter (OutputStream os) {
        this (new OutputStreamWriter (os));
    }

    /**
     *  Writes out the specified CharSequence as a separate cell.
     * 
     * @param unescapedText      The text of a single cell to print.
     * @throws java.io.IOException  If writer fails to write
     */
    public void             writeCell (CharSequence unescapedText) throws IOException {
        if (unescapedText != null)
            synchronized (lock) {
                printCell (unescapedText, out);
            }
    }
    
    /**
     *  Writes out the specified CharSequence as a separate cell.
     * 
     * @param unescapedText      The text of a single cell to print.
     * @throws java.io.IOException  If writer fails to write
     */
    public void             writeCell (Object cell) throws IOException {
        if (cell != null)
            synchronized (lock) {
                printCell (cell.toString (), out);
            }
    }
    
    /**
     *  Method with variable arguments, which writes each object out as
     *  a separate cell, then outputs a line break.
     * @param args      Any objects to be written out (using toString()).
     * @throws java.io.IOException  If writer fails to write
     */
    public void             writeLine (Object ... args) throws IOException {
        synchronized (lock) {
            boolean     first = true;
            
            for (Object arg : args) {
                if (first)
                    first = false;
                else
                    write (',');
                
                if (arg != null)
                    printCell (arg.toString (), out);
            }
            
            write ('\n');
        }
    }
    
    /**
     *  Prints text to CSV cell, escaping it if necessary.
     * 
     *  @param unescapedText         The text of a single cell to print.
     *  @param wr                    The CSV format writer
     *  @throws java.io.IOException  If writer fails to write
     */
    public static void     printCell (CharSequence unescapedText, Writer wr) throws IOException {
        int             len = unescapedText.length ();
        
        if (len == 0)
            return;
        
        boolean         needEscape = false;
        
        search: for (int ii = 0; ii < len; ii++) {
            switch (unescapedText.charAt (ii)) {
                case '"':
                case ',':
                    needEscape = true;
                    break search;
            }
        }
        
        if (needEscape) 
            wr.write ('"');
        
        for (int ii = 0; ii < len; ii++) {
            char        ch = unescapedText.charAt (ii);
            
            if (ch == '"') 
                wr.write ('"');
                    
            wr.write (ch);
        }
        
        if (needEscape) 
            wr.write ('"');
    }
}
