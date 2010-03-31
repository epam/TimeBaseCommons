package deltix.util.io;

import java.io.EOFException;

/**
 * Date: Mar 30, 2010
 */
public class EOQException extends EOFException{
    
    public EOQException() {
    }

    public EOQException(String s) {
        super(s);
    }
}
