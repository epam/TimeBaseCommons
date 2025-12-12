package com.epam.deltix.util.csvx;

import com.epam.deltix.util.lang.Util;
import java.util.regex.*;

/**
 *
 */
public abstract class ColumnDescriptor {
    private String                      mHeader;
    private String                      mConstValue;
    private boolean                     mTrim = true;
    private int                         mIdxInCSV = -1;
    private CharSequence                mCell;

    private Pattern                     mGapPattern = null;
    private String []                   mGapValues = null;
    private Pattern                     mNullPattern = null;
    private String []                   mNullValues = null;

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
        if (mHeader == null)
            throw new IllegalStateException ("Column has no header");
        
        return (mHeader);
    }

    public final int            getCSVIdx () {
        return (mIdxInCSV);
    }

    public final void           setConstValue (String value) {
        mConstValue = value;
    }
    
    public final void           setTrimWhiteSpace (boolean flag) {
        mTrim = flag;
    }

    public final boolean        getTrimWhiteSpace () {
        return (mTrim);
    }

    public void                 setGapPattern(String gapPattern) {
        if (gapPattern != null)
            mGapPattern = Pattern.compile(gapPattern);
    }

    public void                 setGapValues(String[] gapValues) {
        this.mGapValues = gapValues;
    }

    public void                 setNullPattern(String nullPattern) {
        if (nullPattern != null)
            mNullPattern = Pattern.compile(nullPattern);
    }

    public void                 setNullValues(String[] nullValues) {
        this.mNullValues = nullValues;
    }

    public final void           fetchCell (CSVXReader csvxrd) {
        if (mConstValue != null)
            mCell = mConstValue;
        else {
            assert mIdxInCSV >= 0 : mHeader + ": index unset";
            mCell = csvxrd.getCell (mIdxInCSV, mTrim);
        }
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

    public final boolean isGAP() {
        boolean result = mCell != null && mGapPattern != null && matches(mGapPattern);
        if (!result && mCell != null && mGapValues != null) {
            for (String s : mGapValues)
                if (Util.equals(s, mCell))
                    return true;
        }
        return result;
    }

    public final boolean isNull() {
        boolean result = mCell != null && mNullPattern != null && matches(mNullPattern);
        if (!result && mCell != null && mNullValues != null) {
            for (String s : mNullValues)
                if (Util.equals(s, mCell))
                    return true;
        }
        return result;
    }

    public final boolean        hasConstantValue () {
        return (mConstValue != null);
    }
    
    public final void           setIndexInCSV (int csvIdx) {
        mIdxInCSV = csvIdx;
    }

    public final boolean        findIndexFromHeaders (CSVXReader csv) {
        return (findIndexFromHeaders (csv.getHeaders ()));
    }
    
    public final boolean        findIndexFromHeaders (String [] headers) {
        mIdxInCSV = Util.indexOf (headers, getHeader ());
        return (mIdxInCSV >= 0);
    }
}
