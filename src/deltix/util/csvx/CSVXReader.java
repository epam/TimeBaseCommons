package deltix.util.csvx;

import java.io.*;

import deltix.util.lang.*;
import deltix.util.collections.*;
import deltix.util.collections.generated.*;
import deltix.util.io.*;
import deltix.util.text.CharSequenceParser;
import java.util.regex.*;

/**
 *
 */
public class CSVXReader implements Disposable {
    private String []                   mHeaders;
    private SeekCapable                 mSeekCapable = null;
    private BufferedInputStream         mBufferedStream = null;
    private ByteCountingInputStream     mInputStream = null;
    private Reader                      mReader;
    private boolean                     mCloseReader;
    private String                      mDiagPrefix;
    private boolean                     mEOF = false;
    private int                         mLineNumber = 1;
    private long                        mLineStartPosition = 0;
    private StringBuilder               mBuffer = new StringBuilder ();
    private IntegerArrayList            mInclStartIndexes = new IntegerArrayList ();
    private IntegerArrayList            mExclEndIndexes = new IntegerArrayList ();
    private boolean                     mLastCharWasCR = false;    
    private CharSubSequence             mStockCharSequence = new CharSubSequence (mBuffer);    
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
        char                    delimiter, 
        boolean                 closeReader, 
        String                  diagPrefix
    )
    {
        /*
        if (is instanceof SeekCapable) {
            mSeekCapable = (SeekCapable) is;
            mBuffer = new BufferedInputStream (is);
        }
        */
        mInputStream = new ByteCountingInputStream (is);
        mReader = new InputStreamReader (mInputStream);
        mCloseReader = closeReader;
        mDiagPrefix = diagPrefix;
        mDelimiter = delimiter;
    }
    
    public CSVXReader (File f, char delimiter) throws IOException {
        this (
            new BufferedInputStream (new FileInputStream (f)),
            delimiter, 
            true,
            f.getPath () + ": "
        );
    }
    
    public CSVXReader (File f) throws IOException {
        this (f, ',');
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
    
    public String []                    getHeaders () {
        return (mHeaders);
    }
    
    public int                          getHeaderIndex (String hdr) {
        if (mHeaders == null)
            throw new IllegalStateException ("readHeaders () has not been called");
        
        return (Util.indexOf (mHeaders, hdr));
    }
    
    public int                          getHeaderIndexEx (String hdr) 
        throws IOException
    {
        int         idx = getHeaderIndex (hdr);
        
        if (idx < 0)
            throw new IOException ("Required header '" + hdr + "' is missing.");
        
        return (idx);
    }
    
    public int                          getNumCells () {
        return (mInclStartIndexes.size ());
    }
    
    public int                          getLineNumber () {
        return (mLineNumber);
    }

    public boolean                      getCloseReader () {
        return mCloseReader;
    }

    public void                         setCloseReader (boolean closeReader) {
        this.mCloseReader = closeReader;
    }
         
    public long                         getLineOffset () {
        return (mLineStartPosition);
    }
    
    public char                         getDelimiter () {
        return mDelimiter;
    }

    public void                         setDelimiter (char delimiter) {
        this.mDelimiter = delimiter;
    }

    public String                       getDiagPrefix () {
        return mDiagPrefix;
    }

    public void                         setDiagPrefix (String diagPrefix) {
        this.mDiagPrefix = diagPrefix;
    }
            
    
    public String                       getDiagPrefixWithLineNumber () {
        return (mDiagPrefix + mLineNumber + ": ");
    }
    
    public boolean                      cellMatches (int idx, Matcher regex) {
        regex.reset (getCell (idx));
        return (regex.matches ());
    }
    
    public boolean                      cellMatches (int idx, Pattern regex) {
        return (regex.matcher (getCell (idx)).matches ());
    }
    
    public boolean                      cellContains (int idx, String text) {
        return (cellContains (idx, text, false));
    }
    
    public boolean                      cellContains (int idx, String text, boolean trimCell) {
        return (Util.equals (text, getCell (idx, trimCell)));
    }
    
    public CharSequence                 getCell (int idx) {
        return (getCell (idx, false));
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
    
    public double                       getDoubleEx (int idx) {
        return (Double.parseDouble (getString (idx, true)));
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
            if (!cd.hasConstantValue () && !cd.findIndexFromHeaders (mHeaders))
                throw new RuntimeException (
                    mDiagPrefix + "1: Header '" + cd.getHeader () + "' was not found"
                );
    }
    
    public static void                  main (String [] args) throws IOException {
        CSVXReader   csvp = new CSVXReader (new File (args [0]));
        long        t0 = System.currentTimeMillis ();
        
        while (csvp.nextLine ()) {
            System.out.println ("Line " + csvp.getLineNumber () + " @" + csvp.getLineOffset ());
            
            int     num = csvp.getNumCells ();
            
            for (int ii = 0; ii < num; ii++)
                System.out.println ("Cell #" + ii + ": >>" + csvp.getCell (ii, false) + "<<");
        }
        
        csvp.close ();
    }
}
