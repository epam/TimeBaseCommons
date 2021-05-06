package com.epam.deltix.util.csvx;

import java.io.*;

import com.epam.deltix.util.io.*;

/**
 *
 */
public class CSVXReader extends TextReader {
 
	private long                    	mLineStartPosition = 0;
    private char                        mDelimiter;
    
    public static CSVXReader            openResource (Class <?> cls, String path, char delimiter) 
        throws IOException
    {
        InputStream         is = cls.getResourceAsStream (path);
        
        if (is == null)
            throw new FileNotFoundException (path);
        
        return (new CSVXReader (new InputStreamReader (is), delimiter, true, path + ": "));
    }
    
    public static CSVXReader            openResource (Class <?> cls, String path) 
        throws IOException
    {
        return (openResource (cls, path, ','));
    }
    
    public CSVXReader (Reader rd, char delimiter, boolean closeReader, String diagPrefix) {
        mReader = rd;
        mCloseReader = closeReader;
        mDiagPrefix = diagPrefix;
        mDelimiter = delimiter;
    }
    
    public CSVXReader (
        InputStream             is,         
        String                  diagPrefix
    )
    {
        this (is, ',', true, diagPrefix);
    }
    
    public CSVXReader (
        InputStream             is, 
        char                    delimiter, 
        boolean                 closeReader, 
        String                  diagPrefix
    )
    {
        mInputStream = new ByteCountingInputStream (is);
        mReader = new BufferedReader (new InputStreamReader (mInputStream));
        mCloseReader = closeReader;
        mDiagPrefix = diagPrefix;
        mDelimiter = delimiter;
    }
    
    public CSVXReader (String f, char delimiter) throws IOException {
        this (new File (f), delimiter);        
    }
    
    public CSVXReader (File f, char delimiter) throws IOException {
        this (
            new BufferedInputStream (new FileInputStream (f)),
            delimiter, 
            true,
            f.getPath () + ": "
        );
    }
    
    public CSVXReader (String f) throws IOException {
        this (new File (f));        
    }
    
    public CSVXReader (File f) throws IOException {
        this (f, ',');
    }
    private static final int        	BEGIN              = 1;
    private static final int            QUOTED_CELL = 2;
    private static final int            UNQUOTED_CELL = 3;
    private static final int            QUOTED_QUOTE = 4;
    private static final int            COMMA = 5;
    
    public String                       getLine () {
        StringWriter        swr = new StringWriter ();

        try {
            writeLineTo (new CSVWriter (swr));
        } catch (IOException iox) {
            throw new RuntimeException ("unexpected", iox);
        }
        
        return (swr.toString ());
    }

    private static final String []      EMPTY = { };

    public String []                    getCells () {
        int         n = getNumCells ();

        if (n == 0)
            return (EMPTY);

        String []   ret = new String [n];

        for (int ii = 0; ii < n; ii++)
            ret [ii] = getString (ii);

        return (ret);
    }

    public void                         writeLineTo (CSVWriter writer) 
        throws IOException
    {
        int         n = getNumCells ();

        if (n != 0) {
            writer.writeCell (getCell (0));

            for (int ii = 1; ii < n; ii++) {
                writer.writeSeparator ();
                writer.writeCell (getCell (ii));
            }
        }

        writer.writeLine ();
    }

    public boolean                      nextLine () throws IOException {
        if (mEOF)
            return (false);
        
        mBuffer.setLength (0);
        mInclStartIndexes.clear ();
        mExclEndIndexes.clear ();
        
        int         state = BEGIN;
        int         start = 0;
                
        for (;;) {
            int                 ch = mReader.read ();
            
            if (mLastCharWasCR && ch == 10)
                continue;
            
            mLastCharWasCR = ch == 13;
            
            switch (state) {
                case BEGIN:
                    mLineStartPosition = 
                        mInputStream == null ? -1 : mInputStream.getNumBytesRead () - 1;
                    
                    if (ch == mDelimiter) {
                        mInclStartIndexes.add (start);
                        mExclEndIndexes.add (mBuffer.length ());
                        state = COMMA;
                    }
                    else
                        switch (ch) {
                            case -1:
                                mEOF = true;
                                return (false);

                            case 10:
                            case 13:
                                mLineNumber++;
                                return (true);

                            case '"':                    
                                start = mBuffer.length ();
                                state = QUOTED_CELL;
                                break;

                            default:
                                start = mBuffer.length ();
                                mBuffer.append ((char) ch);
                                state = UNQUOTED_CELL;
                                break;
                        }
                    break;
            
                case COMMA:
                    if (ch == mDelimiter) {
                        mInclStartIndexes.add (0);
                        mExclEndIndexes.add (0);
                    }
                    else
                        switch (ch) {
                            case -1:
                                mEOF = true;
                                mInclStartIndexes.add (0);
                                mExclEndIndexes.add (0);
                                return (true);

                            case 10:
                            case 13:
                                mLineNumber++;
                                mInclStartIndexes.add (0);
                                mExclEndIndexes.add (0);
                                return (true);

                            case '"':                    
                                start = mBuffer.length ();
                                state = QUOTED_CELL;
                                break;

                            default:
                                start = mBuffer.length ();
                                mBuffer.append ((char) ch);
                                state = UNQUOTED_CELL;
                                break;
                        }
                    break;

                case QUOTED_CELL:
                    switch (ch) {
                        case -1:
                            throw new EOFException (
                                mDiagPrefix + "Unterminated cell at end of file"
                            );
                            
                        case 10:
                        case 13:
                            mLineNumber++;
                            mBuffer.append ('\n');
                            break;
                            
                        case '"':                    
                            state = QUOTED_QUOTE;
                            break;
                            
                        default:
                            mBuffer.append ((char) ch);
                            break;
                    }
                    break;

                case UNQUOTED_CELL:
                    if (ch == mDelimiter) {
                        mInclStartIndexes.add (start);
                        mExclEndIndexes.add (mBuffer.length ());
                        state = COMMA;
                    }
                    else
                        switch (ch) {
                            case -1:
                                mEOF = true;
                                mInclStartIndexes.add (start);
                                mExclEndIndexes.add (mBuffer.length ());
                                return (true);

                            case 10:
                            case 13:
                                mLineNumber++;
                                mInclStartIndexes.add (start);
                                mExclEndIndexes.add (mBuffer.length ());
                                state = BEGIN;
                                return (true);

                            default:              
                                mBuffer.append ((char) ch);
                                break;
                        }
                    break;
                    
                case QUOTED_QUOTE:
                    if (ch == mDelimiter) {
                        mInclStartIndexes.add (start);
                        mExclEndIndexes.add (mBuffer.length ());
                        state = COMMA;
                    }
                    else
                        switch (ch) {
                            case -1:
                                mEOF = true;
                                mInclStartIndexes.add (start);
                                mExclEndIndexes.add (mBuffer.length ());
                                return (true);

                            case 10:
                            case 13:
                                mLineNumber++;
                                mInclStartIndexes.add (start);
                                mExclEndIndexes.add (mBuffer.length ());
                                state = BEGIN;
                                return (true);

                            case '"':                    
                                mBuffer.append ('"');
                                state = QUOTED_CELL;
                                break;

                            default:
                                mBuffer.append ((char) ch);
                                state = UNQUOTED_CELL;
                                break;
                        }
                    break;
            }            
        }
    }
   
    public long 						getLineOffset ( ) {
		return (mLineStartPosition);
	}
    
    public char                         getDelimiter () {
        return mDelimiter;
    }

    public void                         setDelimiter (char delimiter) {
        this.mDelimiter = delimiter;
    }


}
