package deltix.util.csvx;

import deltix.util.Util;
import deltix.util.collections.CharSubSequence;
import java.util.regex.*;

/**
 *
 */
public abstract class ColumnDescriptor {
    private String                      mHeader;
    private boolean                     mTrim = true;
    private int                         mIdxInCSV = -1;
    private CharSubSequence             mTrimmed = new CharSubSequence ();
    private CharSequence                mCell;
    
    protected ColumnDescriptor () { 
    }
    
    protected ColumnDescriptor (String header) {
        setHeader (header);
    }
    
    public final void           setHeader (String header) {        
        if (header == null)
            throw new IllegalArgumentException ("header == null");

        mHeader = header;
    }
    
    public final String         getHeader () {
        return (mHeader);
    }
    
    public final int            getCSVIdx () {
        return (mIdxInCSV);
    }

    public final void           setTrimWhiteSpace (boolean flag) {
        mTrim = flag;
    }
    
    public final boolean        getTrimWhiteSpace () {
        return (mTrim);
    }
    
    public final void           fetchCell (CSVXReader csvxrd) {
        assert mIdxInCSV >= 0 : mHeader + ": index unset - call CSVXReader.setIndexFromHeaders (this)!";
        
        mCell = csvxrd.getCell (mIdxInCSV, mTrim);
    }

    public final boolean        isEmpty () {
        return (mCell.length () == 0);
    }
    
    public final boolean        equals (CharSequence cs) {
        return (Util.equals (mCell, cs));
    }
    
    public final boolean        matches (Pattern pat) {
        return (pat.matcher (mCell).matches ());
    }
    
    public final CharSequence   getCharSequence () {
        return (mCell);
    }

    public final String         getString () {
        return (mCell.toString ());
    }
      
    public final String         getInternedString () {
        return (mCell.toString ().intern ());
    }
    
    public final void           setIndexInCSV (int csvIdx) {
        mIdxInCSV = csvIdx;
    }

    public final boolean        findIndexFromHeaders (String [] headers) {
        mIdxInCSV = Util.indexOf (headers, mHeader);
        
        return (mIdxInCSV >= 0);
    }    
}
