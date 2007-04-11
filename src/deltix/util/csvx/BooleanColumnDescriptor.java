package deltix.util.csvx;

import java.util.regex.*;

/**
 *
 */
public class BooleanColumnDescriptor extends ColumnDescriptor {
    static final Pattern        DEFAULT_TRUE_PATTERN = 
        Pattern.compile ("y|Y|true|TRUE");
    
    private Pattern             mCompiledTruePattern = DEFAULT_TRUE_PATTERN;
    
    public void                 setTruePattern (String pattern) {
        mCompiledTruePattern = Pattern.compile (pattern);
    }
    
    protected Object            parseValue (String cell) {
        return (mCompiledTruePattern.matcher (cell).matches ());
    }
}
