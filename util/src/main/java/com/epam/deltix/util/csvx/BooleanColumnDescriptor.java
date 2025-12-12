package com.epam.deltix.util.csvx;

import java.util.regex.*;

/**
 *
 */
public class BooleanColumnDescriptor extends ColumnDescriptor {
    static final Pattern        DEFAULT_TRUE_PATTERN = 
        Pattern.compile ("y|Y|true|TRUE");
    
    private Pattern             mCompiledTruePattern = DEFAULT_TRUE_PATTERN;
    
    public BooleanColumnDescriptor () { 
    }
    
    public BooleanColumnDescriptor (String header) {
        super (header);
    }    
    
    public void                 setTruePattern (String pattern) {
        mCompiledTruePattern = Pattern.compile (pattern);
    }
    
    public boolean              getBoolean () {
        return (mCompiledTruePattern.matcher (getCharSequence ()).matches ());
    }
}
