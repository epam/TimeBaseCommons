package deltix.util.io;

import java.io.*;

/**
 *  Helper for writing correctly formatted CSV files.
 */
public class CSVWriter extends FilterWriter {
    private boolean             closeDelegate = true;
    private boolean             flushEveryLine = false;
    
    public CSVWriter (String f) throws IOException {
        this (new File (f));
    }

    public CSVWriter (String f, boolean append) throws IOException {
        this (new File (f), append);
    }
    
    public CSVWriter (File f) throws IOException {
        this (f, false);
    }

    public CSVWriter (File f, boolean append) throws IOException {
        super (new BufferedWriter (new FileWriter (f, append)));
    }
    
    public CSVWriter (Writer out) {
        super (out);
    }
    
    public CSVWriter (OutputStream os) {
        this (new OutputStreamWriter (os));
    }

    public boolean          getCloseDelegate () {
        return closeDelegate;
    }

    public void             setCloseDelegate (boolean closeDelegate) {
        this.closeDelegate = closeDelegate;
    }

    public boolean          getFlushEveryLine () {
        return flushEveryLine;
    }

    public void             setFlushEveryLine (boolean flushEveryLine) {
        this.flushEveryLine = flushEveryLine;
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
     */
    public void             writeCells (Object ... args) throws IOException {
        writeCells (true, args);
    }
    
    /**
     */
    public void             writeCells (boolean first, Object ... args) throws IOException {
        synchronized (lock) {
            for (Object arg : args) {
                if (first)
                    first = false;
                else
                    write (',');

                if (arg != null)
                    printCell (arg.toString (), out);
            }
        }
    }
    
    /**
     *  Writes out the specified CharSequence as a separate cell.
     * 
     * @param cell      The text of a single cell to print.
     * @throws java.io.IOException  If writer fails to write
     */
    public void             writeCell (Object cell) throws IOException {
        if (cell != null)
            synchronized (lock) {
                printCell (cell.toString (), out);
            }
    }
    
    public void             writeLine () throws IOException {
        synchronized (lock) {
            write ('\n');
            
            if (flushEveryLine)
                flush ();
        }
    }
    
    public void             writeSeparator () throws IOException {
        synchronized (lock) {
            write (',');
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
            writeCells (args);            
            write ('\n');
            
            if (flushEveryLine)
                flush ();
        }
    }

    @Override
    public void             close () throws IOException {
        if (closeDelegate)
            super.close ();
        else
            super.flush ();
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
