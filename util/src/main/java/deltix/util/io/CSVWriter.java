package deltix.util.io;


import deltix.util.collections.generated.CharacterHashSet;

import java.io.*;

/**
 *  Helper for writing correctly formatted CSV files.
 */
public class CSVWriter extends FilterWriter {
    
    private static final char   DEFAULT_SEPARATOR = ',';
    private static final char[] DEFAULT_ESCAPE_CHARACTERS = new char[]{'"', ','};
    private static final CharacterHashSet DEFAULT_ESCAPE_CHARACTERS_SET = new CharacterHashSet(DEFAULT_ESCAPE_CHARACTERS);

    private boolean             closeDelegate = true;
    private boolean             flushEveryLine = false;
    
    private final char          separator;
    private final CharacterHashSet escapeCharacters;

    public CSVWriter (String f) throws IOException {
        this (new File (f));
    }
    
    public CSVWriter (String f, char separator) throws IOException {
        this (new File (f), separator);
    }

    public CSVWriter (String f, boolean append) throws IOException {
        this (new File (f), append);
    }
    
    public CSVWriter (String f, boolean append, char separator ) throws IOException {
        this (new File (f), append, separator);
    }

    public CSVWriter (String f, boolean append, char separator, char ... escapeCharacters) throws IOException {
        this (new File (f), append, separator, escapeCharacters);
    }

    public CSVWriter (File f) throws IOException {
        this (f, false);
    }
    
    public CSVWriter (File f, char separator) throws IOException {
        this (f, false, separator);
    }

    public CSVWriter (File f, boolean append) throws IOException {
        this (new BufferedWriter (new FileWriter (f, append)));
    }
    
    public CSVWriter (File f, boolean append, char separator) throws IOException {
        this (new BufferedWriter (new FileWriter (f, append)), separator);
    }

    public CSVWriter (File f, boolean append, char separator, char... escapeCharacters) throws IOException {
        this(new BufferedWriter(new FileWriter(f, append)), separator, escapeCharacters);
    }

    public CSVWriter (Writer out) {
        this (out, DEFAULT_SEPARATOR);
    }

    public CSVWriter (Writer out, char separator) {
        this (out, separator, DEFAULT_ESCAPE_CHARACTERS);
    }

    public CSVWriter (Writer out, char separator, char... escapeCharacters) {
        super (out);
        this.separator = separator;
        this.escapeCharacters = new CharacterHashSet(escapeCharacters);
        this.escapeCharacters.add('"');
        this.escapeCharacters.add(',');
        this.escapeCharacters.add(separator);
    }
    
    public CSVWriter (OutputStream os) {
        this (new OutputStreamWriter (os));
    }
    
    public CSVWriter (OutputStream os, char separator, String charsetName) throws UnsupportedEncodingException {
        this (new BufferedWriter (new OutputStreamWriter (os,
                                                          charsetName)),
              separator);
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
                printCell (unescapedText, out, escapeCharacters);
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
                    write (separator);

                if (arg != null)
                    printCell (arg.toString (), out, escapeCharacters);
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
                printCell (cell.toString (), out, escapeCharacters);
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
            write (separator);
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
    public static void     printCell (CharSequence unescapedText, Appendable wr) throws IOException {
        printCell(unescapedText, wr, DEFAULT_ESCAPE_CHARACTERS_SET);
    }

    /**
     *  Prints text to CSV cell, escaping it if necessary.
     *
     *  @param unescapedText         The text of a single cell to print.
     *  @param wr                    The CSV format writer
     *  @param escapes             List of characters to escape
     *  @throws IOException  If writer fails to write
     */
    public static void printCell(CharSequence unescapedText, Appendable wr, CharacterHashSet escapes) throws IOException {
        int len = unescapedText.length();

        if (len == 0)
            return;

        boolean needEscape = false;

        for (int ii = 0; ii < len; ii++) {
            if (escapes.contains(unescapedText.charAt(ii))) {
                needEscape = true;
                break;
            }
        }

        if (needEscape)
            wr.append('"');

        for (int ii = 0; ii < len; ii++) {
            char ch = unescapedText.charAt(ii);

            if (ch == '"')
                wr.append('"');

            wr.append(ch);
        }

        if (needEscape)
            wr.append('"');
    }
}
