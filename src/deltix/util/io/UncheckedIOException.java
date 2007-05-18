package deltix.util.io;

import java.io.IOException;

/**
 *  Unchecked exception, used to wrap the checked java.io.IOException 
 *  occurring because of system problems. 
 */
public class UncheckedIOException extends RuntimeException {
    public UncheckedIOException (String msg, Exception iox) {
        super (msg, iox);
    }
    
    public UncheckedIOException (Exception iox) {
        super ("System IO error: " + iox, iox);
    }
    
    public UncheckedIOException (String msg) {
        super (msg);
    }
}
