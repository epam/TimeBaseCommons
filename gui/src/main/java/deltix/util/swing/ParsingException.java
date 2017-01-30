package deltix.util.swing;

import deltix.util.lang.*;

public class ParsingException extends LocalizableException {
    public ParsingException (String key, String badText) {
    	super (key, new Object [] { badText });
    }
    	
    public ParsingException (String key, String badText, Exception x) {
    	super (key, new Object [] { badText }, x);
    }
    
    public ParsingException (String key, Object [] badObjects) {
    	super (key, badObjects );
    }
    
    public ParsingException () {
    	super ();
    }
}
