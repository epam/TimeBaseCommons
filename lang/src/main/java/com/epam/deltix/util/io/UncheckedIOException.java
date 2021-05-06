package com.epam.deltix.util.io;

import java.io.IOException;

/**
 *  Unchecked exception, used to wrap the checked java.io.IOException 
 *  occurring because of system problems.
 */
public class UncheckedIOException extends RuntimeException {
    public UncheckedIOException (String msg, Throwable iox) {
        super (msg, iox);
    }
    
    public UncheckedIOException (Throwable iox) {
        super ("System IO error: " + iox, iox);
    }
    
    public UncheckedIOException (String msg) {
        super (msg);
    }
}
