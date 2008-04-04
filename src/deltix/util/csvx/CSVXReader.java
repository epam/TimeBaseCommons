package deltix.util.csvx;

import java.io.*;

import deltix.util.Util;
import deltix.util.collections.*;
import deltix.util.collections.generated.*;
import deltix.util.text.CharSequenceParser;

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
    
    public void                         readHeaders () throws IOException {
        readHeaders (false);
    }
    
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
    
    public CharSequence                 getBuffer () {
        return (mBuffer);
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
                    switch (ch) {
                        case -1:
                            mEOF = true;
                            return (false);
                            
                        case 10:
                        case 13:
                            mLineNumber++;
                            mPosition = 1;                           
                            return (true);
                            
                        case ',':
                            mInclStartIndexes.add (start);
                            mExclEndIndexes.add (mBuffer.length ());
                            state = COMMA;
                            break;
                            
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
                    switch (ch) {
                        case -1:
                            mEOF = true;
                            mInclStartIndexes.add (0);
                            mExclEndIndexes.add (0);
                            return (true);
                            
                        case 10:
                        case 13:
                            mLineNumber++;
                            mPosition = 1;                           
                            mInclStartIndexes.add (0);
                            mExclEndIndexes.add (0);
                            return (true);
                            
                        case ',':
                            mInclStartIndexes.add (0);
                            mExclEndIndexes.add (0);
                            break;
                            
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
                            mPosition = 1;                           
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
                    switch (ch) {
                        case -1:
                            mEOF = true;
                            mInclStartIndexes.add (start);
                            mExclEndIndexes.add (mBuffer.length ());
                            return (true);
                            
                        case 10:
                        case 13:
                            mLineNumber++;
                            mPosition = 1;                           
                            mInclStartIndexes.add (start);
                            mExclEndIndexes.add (mBuffer.length ());
                            state = BEGIN;
                            return (true);
                            
                        case ',':
                            mInclStartIndexes.add (start);
                            mExclEndIndexes.add (mBuffer.length ());
                            state = COMMA;
                            break;
                            
                        default:              
                            mBuffer.append ((char) ch);
                            break;
                    }
                    break;
                    
                case QUOTED_QUOTE:
                    switch (ch) {
                        case -1:
                            mEOF = true;
                            mInclStartIndexes.add (start);
                            mExclEndIndexes.add (mBuffer.length ());
                            return (true);
                            
                        case 10:
                        case 13:
                            mLineNumber++;
                            mPosition = 1;                           
                            mInclStartIndexes.add (start);
                            mExclEndIndexes.add (mBuffer.length ());
                            state = BEGIN;
                            return (true);
                            
                        case ',':
                            mInclStartIndexes.add (start);
                            mExclEndIndexes.add (mBuffer.length ());
                            state = COMMA;
                            break;
                            
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
    
    public String                       getString (int idx, boolean trim) {
        return (getCell (idx, trim).toString ());
    }
    
    public String                       getString (int idx) {
        return (getString (idx, false));
    }
    
    public double                       getDouble (int idx) {
        return (CharSequenceParser.parseDouble (getCell (idx, true)));
    }
    
    public float                        getFloat (int idx) {
        return (CharSequenceParser.parseFloat (getCell (idx, true)));
    }
    
    public int                          getInt (int idx) {
        return (CharSequenceParser.parseInt (getCell (idx, true)));
    }
    
    public long                         getLong (int idx) {
        return (CharSequenceParser.parseLong (getCell (idx, true)));
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
