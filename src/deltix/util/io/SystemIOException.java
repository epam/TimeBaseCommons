package deltix.util.io;

import java.io.IOException;

/**
 *  Unchecked exception, used to wrap the checked java.io.IOException 
 *  occurring because of system problems. 
 */
public class SystemIOException extends RuntimeException {
    public SystemIOException (String msg, IOException iox) {
        super (msg, iox);
    }
    
    public SystemIOException (IOException iox) {
        super ("System IO error: " + iox, iox);
    }
    
    public SystemIOException (String msg) {
        super (msg);
    }
}
