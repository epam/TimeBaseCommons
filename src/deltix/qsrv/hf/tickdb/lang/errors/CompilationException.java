package deltix.qsrv.hf.tickdb.lang.errors;

import deltix.qsrv.hf.tickdb.lang.pub.Element;
import deltix.qsrv.hf.tickdb.lang.pub.Location;

/**
 *
 */
public class CompilationException extends RuntimeException {
    private static final boolean        FILL_STACK_TRACE = 
        Boolean.getBoolean ("deltix.qql.trace");
    
    private final long                  location;
    public final String                 diag;
    
    private static String       formatLocation (long location) {
        int     startLine = Location.getStartLine (location);
        
        if (startLine == Location.NONE)
            return ("");
        
        StringBuilder   sb = new StringBuilder ();
        
        sb.append (startLine + 1);
        
        int     startPos = Location.getStartPosition (location);
        
        if (startPos != Location.NONE) {
            sb.append (".");
            sb.append (startPos + 1);
        }
                
        int     endLine = Location.getEndLine (location);
        int     endPos = Location.getEndPosition (location);
        
        if (endLine != startLine || endPos != startPos) {
            sb.append ("..");
            
            if (endLine != startLine) {
                sb.append (endLine + 1);                
                sb.append (".");
            }
            
            if (endPos != startPos || endLine != startLine)
                sb.append (endPos + 1);
        }
        
        sb.append (": ");
        return (sb.toString ());
    }
    
    public CompilationException (String msg, long location) {
        super (formatLocation (location) + msg);
        
        this.location = location;
        this.diag = msg;
    }

    public CompilationException (String msg, Element [] elems) {
        this (msg, Location.fromTo (elems [0].location, elems [elems.length - 1].location));
    }

    public CompilationException (String msg, Element elem) {
        this (msg, elem.location);
    }

    @Override
    public Throwable            fillInStackTrace () {
        return (FILL_STACK_TRACE ? super.fillInStackTrace () : null);
    }
    
    /**
     *  Returns the 0-based start line of the problem area.
     */
    public final int            getStartLine () {
        return (Location.getStartLine (location));
    }

    /**
     *  Returns the 0-based end line of the problem area.
     */
    public final int            getEndLine () {
        return (Location.getEndLine (location));
    }

    /**
     *  Returns the 0-based start position of the problem area.
     */
    public final int            getStartPosition () {
        return (Location.getStartPosition (location));
    }

    /**
     *  Returns the 0-based end position of the problem area.
     */
    public final int            getEndPosition () {
        return (Location.getEndPosition (location));
    }
}
