package deltix.util.csvx;

import deltix.util.Util;
import java.util.regex.*;

/**
 *
 */
public abstract class ColumnDescriptor {
    private String                      mHeader;
    private boolean                     mTrim = true;
    private Object                      mEmptyCellObject = null;
    private int                         mIdxInCSV = -1;
    private Pattern []                  mCompiledOutOfBandPatterns = null;
    private Object []                   mOutOfBandValues = null;
    
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
    
    public final void           setEmptyCellObject (Object obj) {
        mEmptyCellObject = obj;
    }
    
    public final Object         getEmptyCellObject () {
        return (mEmptyCellObject);
    }
    
    public final void           setOutOfBandPatterns (
        String []                   patterns,
        Object []                   values
    )
    {
        int             num = patterns.length;
        
        if (values.length != num)
            throw new IllegalArgumentException (
                "patterns.length == " + num + 
                    " != values.length == " + values.length
            );
        
        mCompiledOutOfBandPatterns = new Pattern [num];
        
        for (int ii = 0; ii < num; ii++)
            mCompiledOutOfBandPatterns [ii] = Pattern.compile (patterns [ii]);
        
        mOutOfBandValues = values;
    }
    
    protected abstract Object   parseValue (String cell);
        
    public final Object         getValue (String cell) {
        if (cell == null)
            return (mEmptyCellObject);
        
        if (mTrim)
            cell = cell.trim ();
        
        if (cell.length () == 0)
            return (mEmptyCellObject);
        
        if (mCompiledOutOfBandPatterns != null) {
            for (int ii = 0; ii < mCompiledOutOfBandPatterns.length; ii++)
                if (mCompiledOutOfBandPatterns [ii].matcher (cell).matches ())
                    return (mOutOfBandValues [ii]);
        }
        
        return (parseValue (cell));
    }
        
    public final Object         getValue (String [] line) {
        if (line.length <= mIdxInCSV)
            return (null);
        
        return (getValue (line [mIdxInCSV]));
    }
    
    protected static int        parseInt (String s, int startIncl, int endIncl) {
        int     ret = 0;
        
        for (int ii = startIncl; ii <= endIncl; ii++) 
            ret = ret * 10 + (s.charAt (ii) - '0');
           
        return (ret);
    }
    
    public final void           setIndexInCSV (int csvIdx) {
        mIdxInCSV = csvIdx;
    }

    public final boolean        findIndexFromHeaders (String [] headers) {
        mIdxInCSV = Util.indexOf (headers, mHeader);
        
        return (mIdxInCSV >= 0);
    }    
}
