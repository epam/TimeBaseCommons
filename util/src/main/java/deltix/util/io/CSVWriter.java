package deltix.util.io;


import deltix.util.collections.generated.CharacterHashSet;

import java.io.*;

/**
 *  Helper for writing correctly formatted CSV files.
 */
public class CSVWriter extends FilterWriter {
    
    private static final char   DEFAULT_SEPARATOR = ',';
    public static final char    DEFAULT_QUOTE_CHARACTER = '"';
    private static final char[] DEFAULT_ESCAPE_CHARACTERS = new char[]{'\n', '\r'};

    private boolean             closeDelegate = true;
    private boolean             flushEveryLine = false;
    
    private final char          separator;
    private final char          quoteCharacter;
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

    public CSVWriter (String f, boolean append, char separator, char quoteCharacter, char ... escapeCharacters) throws IOException {
        this (new File (f), append, separator, quoteCharacter, escapeCharacters);
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

    public CSVWriter (File f, boolean append, char separator, char quoteCharacter, char... escapeCharacters) throws IOException {
        this(new BufferedWriter(new FileWriter(f, append)), separator, quoteCharacter, escapeCharacters);
    }

    public CSVWriter (Writer out) {
        this (out, DEFAULT_SEPARATOR);
    }

    public CSVWriter (Writer out, char separator) {
        this (out, separator, DEFAULT_QUOTE_CHARACTER, DEFAULT_ESCAPE_CHARACTERS);
    }

    public CSVWriter (Writer out, char separator, char quoteCharacter, char... escapeCharacters) {
        super (out);
        this.separator = separator;
        this.quoteCharacter = quoteCharacter;
        this.escapeCharacters = new CharacterHashSet(escapeCharacters);
        this.escapeCharacters.add(separator);
        this.escapeCharacters.add(quoteCharacter);
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
                printCell (unescapedText);
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
                    printCell (arg.toString ());
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
                printCell (cell.toString ());
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

    private void printCell(CharSequence unescapedText) throws IOException {
        int len = unescapedText.length();

        if (len == 0)
            return;

        boolean needEscape = false;

        for (int ii = 0; ii < len; ii++) {
            if (escapeCharacters.contains(unescapedText.charAt(ii))) {
                needEscape = true;
                break;
            }
        }

        if (needEscape)
            out.append(quoteCharacter);

        for (int ii = 0; ii < len; ii++) {
            char ch = unescapedText.charAt(ii);

            if (ch == quoteCharacter)
                out.append(quoteCharacter);

            out.append(ch);
        }

        if (needEscape)
            out.append(quoteCharacter);
    }
}
