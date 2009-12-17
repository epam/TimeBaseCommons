package deltix.qsrv.hf.tickdb.lang.compiler;

import deltix.qsrv.hf.tickdb.lang.pub.Element;
import deltix.qsrv.hf.tickdb.lang.pub.Location;

/**
 *
 */
public class CompilationException extends RuntimeException {
    private final long                  location;

    public CompilationException (String msg, long location) {
        super (
            Location.getStartLine (location) + "." +
            Location.getStartPosition (location) + ":" + 
            msg
        );
        
        this.location = location;
    }

    public CompilationException (String msg, Element elem) {
        this (msg, elem.location);
    }

    public final int            getStartLine () {
        return (Location.getStartLine (location));
    }

    public final int            getEndLine () {
        return (Location.getEndLine (location));
    }

    public final int            getStartPosition () {
        return (Location.getStartPosition (location));
    }

    public final int            getEndPosition () {
        return (Location.getEndPosition (location));
    }
}
