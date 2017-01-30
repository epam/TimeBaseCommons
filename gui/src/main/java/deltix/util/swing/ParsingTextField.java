package deltix.util.swing;

import javax.swing.*;
import java.util.*;

import deltix.util.*;

/** 
 *  Base class for implementing text fields that parse the text according
 *      to some rules, such as expecting a numeric value. If an exception
 *      is thrown while parsing, this field will display an error message,
 *      select its entire text, and grab focus.
 */
public abstract class ParsingTextField extends FormTextField {
    protected ParsingTextField (String text, int columns) {
        super (text, columns);
    }
    
    protected ParsingTextField (String text) {
        super (text);
    }
    
    protected ParsingTextField (int columns) {
        super (columns);
    }
    
    protected ParsingTextField () {
        super ();
    }
    
    /**
     *  Override to parse the text.
     */
    protected abstract void     doParse (String text) 
        throws ParsingException;
    
    protected final void		parse () throws ParsingException {
    	String		text = getText ();
    	
    	try {
    		doParse (text);
    	} catch (ParsingException x) {
    		complain (x);
    		throw x;
    	}
    }
}
