package deltix.util.csvx;

import java.io.*;

import deltix.util.Util;
import deltix.util.collections.*;
import deltix.util.collections.generated.*;

/**
 *
 */
public class CSVXReader {
    private String []                   mHeaders;
    private Reader                      mReader;
    private final boolean               mCloseReader;
    private String                      mDiagPrefix;
    private boolean                     mEOF = false;
    private int                         mLineNumber = 1;
    private int                         mPosition = 1;
    private StringBuilder               mBuffer = new StringBuilder ();
    private IntegerArrayList            mInclStartIndexes = new IntegerArrayList ();
    private IntegerArrayList            mExclEndIndexes = new IntegerArrayList ();
    private boolean                     mLastCharWasCR = false;    
    private CharSubSequence             mStockCharSequence = new CharSubSequence (mBuffer);
    
    public CSVXReader (Reader rd, boolean closeReader, String diagPrefix) {
        mReader = rd;
        mCloseReader = closeReader;
        mDiagPrefix = diagPrefix;
    }
    
    public CSVXReader (File f) throws IOException {
        mReader = new BufferedReader (new FileReader (f));
        mCloseReader = true;
        mDiagPrefix = f.getPath () + ": ";
    }
    
    private static final int            BEGIN = 1;
    private static final int            QUOTED_CELL = 2;
    private static final int            UNQUOTED_CELL = 3;
    private static final int            QUOTED_QUOTE = 4;
    private static final int            COMMA = 5;
    
    public void                         readHeaders (boolean trim) throws IOException {
        if (!nextLine ())
            throw new EOFException (mDiagPrefix + "No headers");

        int     numHeaders = getNumCells ();
        
        mHeaders = new String [numHeaders];
        
        for (int ii = 0; ii < numHeaders; ii++)
            mHeaders [ii] = getCell (ii, trim).toString ();        
    }
    
    public void                         close () {
        if (mReader != null) {
            if (mCloseReader)
                Util.close (mReader);
            
            mReader = null;
            mInclStartIndexes = null;
            mExclEndIndexes = null;
            mBuffer = null;
            mStockCharSequence = null;
        }
    }
    
    public boolean                      nextLine () throws IOException {
        if (mEOF)
            return (false);
        
        mBuffer.setLength (0);
        mInclStartIndexes.clear ();
        mExclEndIndexes.clear ();
        
        int         state = BEGIN;
        int         start = 0;
                
        reading: for (;;) {
            int                 ch = mReader.read ();
            
            if (mLastCharWasCR && ch == 10)
                continue;
            
            mLastCharWasCR = ch == 13;
            
            switch (ch) {
                case -1:
                    mEOF = true;
                    
                    switch (state) {
                        case BEGIN:
                            return (false);
            
                        case COMMA:
                            mInclStartIndexes.add (0);
                            mExclEndIndexes.add (0);
                            return (true);
                            
                        case QUOTED_CELL:
                            throw new EOFException (
                                mDiagPrefix + "Unterminated cell at end of file"
                            );
                            
                        case UNQUOTED_CELL:
                        case QUOTED_QUOTE:
                            mInclStartIndexes.add (start);
                            mExclEndIndexes.add (mBuffer.length ());
                            return (true);
                    }
                    
                case 10:
                case 13:
                    mLineNumber++;
                    mPosition = 1;
                    
                    switch (state) {
                        case BEGIN:
                            return (true);
            
                        case COMMA:
                            mInclStartIndexes.add (0);
                            mExclEndIndexes.add (0);
                            return (true);
                            
                        case QUOTED_CELL:
                            mBuffer.append ('\n');
                            break;
                            
                        case UNQUOTED_CELL:
                        case QUOTED_QUOTE:
                            mInclStartIndexes.add (start);
                            mExclEndIndexes.add (mBuffer.length ());
                            state = BEGIN;
                            return (true);
                    }
                    
                case ',':
                    switch (state) {
                        case QUOTED_CELL:                    
                            mBuffer.append ((char) ch);
                            break;
                            
                        case COMMA:
                            mInclStartIndexes.add (0);
                            mExclEndIndexes.add (0);
                            break;
                            
                        default:
                            mInclStartIndexes.add (start);
                            mExclEndIndexes.add (mBuffer.length ());
                            state = COMMA;
                            break;
                    }
                    break;

                case '"':
                    switch (state) {
                        case BEGIN:
                        case COMMA:
                            state = QUOTED_CELL;
                            start = mBuffer.length ();
                            break;
                            
                        case QUOTED_CELL:
                            state = QUOTED_QUOTE;
                            break;
                            
                        case QUOTED_QUOTE:
                            mBuffer.append ('"');
                            state = QUOTED_CELL;
                            break;
                            
                        case UNQUOTED_CELL:
                            mBuffer.append ('"');
                            break;
                    }
                    break;

                default:
                    switch (state) {
                        case BEGIN:
                        case COMMA:
                            start = mBuffer.length ();
                            mBuffer.append ((char) ch);
                            state = UNQUOTED_CELL;
                            break;
                            
                        case QUOTED_QUOTE:
                            mBuffer.append ((char) ch);
                            state = UNQUOTED_CELL;
                            break;
                            
                        default:
                            mBuffer.append ((char) ch);
                            break;
                    }
            }            
        }
    }
    
    public String []                    getHeaders () {
        return (mHeaders);
    }
    
    public int                          getNumCells () {
        return (mInclStartIndexes.size ());
    }
    
    public int                          getLineNumber () {
        return (mLineNumber);
    }
    
    public String                       getDiagPrefix () {
        return (mDiagPrefix);
    }
    
    public String                       getDiagPrefixWithLineNumber () {
        return (mDiagPrefix + mLineNumber + ": ");
    }
    
    public CharSequence                 getCell (int idx, boolean trim) {
        if (idx >= mInclStartIndexes.size ())
            return (EmptyCharSequence.INSTANCE);
        
        mStockCharSequence.start = mInclStartIndexes.getIntegerNoRangeCheck (idx);
        mStockCharSequence.end = mExclEndIndexes.getIntegerNoRangeCheck (idx);
        
        if (trim)
            mStockCharSequence.trimWhitespace ();
        
        return (mStockCharSequence);
    }
    
    public void                         setIndexFromHeaders (ColumnDescriptor ... cds) {
        for (ColumnDescriptor cd : cds)
            if (!cd.findIndexFromHeaders (mHeaders))
                throw new RuntimeException (
                    mDiagPrefix + "1: Header '" + cd.getHeader () + "' was not found"
                );
    }
    
    public static void                  main (String [] args) throws IOException {
        CSVXReader   csvp = new CSVXReader (new File (args [0]));
        long        t0 = System.currentTimeMillis ();
        
        while (csvp.nextLine ()) {
            int     num = csvp.getNumCells ();
            
            for (int ii = 0; ii < num; ii++)
                System.out.println ("Cell #" + ii + ": >>" + csvp.getCell (ii, false) + "<<");
        }
        
        csvp.close ();
    }
}
