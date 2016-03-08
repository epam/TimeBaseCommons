package deltix.util.jdbc;

/**
 *
 */
public class SubStringFieldTrf extends StringFieldTrf {
    private int             mStartIdx;
    private int             mEndIdx;
    
    public SubStringFieldTrf (String name, int startIdx, int endIdx) {
        super (name);
        mStartIdx = startIdx;
        mEndIdx = endIdx;
    }
    
    public SubStringFieldTrf (String inName, String outName, int startIdx, int endIdx) {
        super (inName, outName);
        mStartIdx = startIdx;
        mEndIdx = endIdx;
    }

    protected String            transform (String in) {
        if (mEndIdx < 0)
            return (in.substring (mStartIdx));
        else
            return (in.substring (mStartIdx, mEndIdx));
    }
}
