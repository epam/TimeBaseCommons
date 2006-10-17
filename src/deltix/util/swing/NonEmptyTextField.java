package deltix.util.swing;

import java.util.*;
import java.text.*;
import javax.swing.*;

/**
 *	A text field that forces the content to be non-empty.
 *  Also, this field can be configured to automatically
 *	trim its value.
 */
public class NonEmptyTextField extends ParsingTextField {
	private String		mValue;
	private boolean		mTrim;
	
    /**
     *	Constructs a NonEmptyTextField with the specified value and size.
     *
     *	@trim		Whether to automatically trim the value.
     */
    public NonEmptyTextField (String value, int columns, boolean trim) {
        super (value, columns);
        mTrim = trim;
    }
    
    /**
     *	Constructs a NonEmptyTextField with the specified value.
     *
     *	@trim		Whether to automatically trim the value.
     */
    public NonEmptyTextField (String value, boolean trim) {
        super (value);
        mTrim = trim;
    }
    
    /**
     *	Constructs a NonEmptyTextField with the specified size.
     *
     *	@trim		Whether to automatically trim the value.
     */
    public NonEmptyTextField (int columns, boolean trim) {
        super (columns);
        mTrim = trim;
    }
    
    /**
     *	Constructs a NonEmptyTextField.
     *
     *	@trim		Whether to automatically trim the value.
     */
    public NonEmptyTextField (boolean trim) {
        super ();
        mTrim = trim;
    }
    
    protected void      doParse (String text) 
        throws ParsingException
    {
    	mValue = text;
    	
    	if (mTrim)
    		mValue = mValue.trim ();
    		
        if (mValue.length () == 0)
            throw new ParsingException ("empty", (String) null);
    }
    
    /**
     *	Returns the text value currently contained in the field.
     *		If the value is empty, the field displays an error message,
     *		waits for the user to acknowledge it, sets focus on itself,
     *		then throws a ParsingException.
     *
     *	@exception ParsingException		When the parsing fails.
     */
    public String       getValue () throws ParsingException {
    	parse ();
    	return (mValue);
    }
    
    public boolean      isEmpty () {
        String  text = getText();
        return (text == null || text.trim().length() == 0);
    }
}
