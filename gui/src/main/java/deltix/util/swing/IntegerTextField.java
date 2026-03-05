package deltix.util.swing;

import java.util.*;
import java.text.*;
import javax.swing.*;

/**
 *	A text field that parses the content as Integer.
 */
public class IntegerTextField extends ParsingTextField {
    private int              mValue;
    private int              mMinValue = Integer.MIN_VALUE;
    private int              mMaxValue = Integer.MAX_VALUE;
    
    /**
     *	Constructs a IntegerTextField with the specified value and
     *		size.
     */
    public IntegerTextField (int value, int columns) {
        super (String.valueOf (value), columns);
    }
    
     /**
     *	Constructs a IntegerTextField with the specified value and
     *		size and min/max constraints.
     */   
    public IntegerTextField (int value, int columns, int min, int max) {
        super (String.valueOf (value), columns);
        mMinValue = min;
        mMaxValue = max;
    }
    
     /**
     *	Constructs a IntegerTextField with the specified String and
     *		size and min/max constraints.
     */   
    public IntegerTextField (String text, int columns, int min, int max) {
        super (text, columns);
        mMinValue = min;
        mMaxValue = max;
    }
    
    protected void      doParse (String text) 
        throws ParsingException
    {
        try {
            mValue = Integer.parseInt (text.trim ());
        } catch (NumberFormatException x) {
            throw new ParsingException ("int", text);
        }
        if (mValue < mMinValue)
                throw new ParsingException("minInt", new Object [] {text,
                         String.valueOf(mMinValue),String.valueOf(mMaxValue)});
        if (mValue > mMaxValue)
                throw new ParsingException("maxInt", new Object [] {text, 
                         String.valueOf(mMinValue),String.valueOf(mMaxValue)});
    }
    
    public void         setMaxValue (int value) {
        mMaxValue = value;
    }
    
    public void         setMinValue (int value) {
        mMinValue = value;
    }
    
    public int          getMaxValue () {
        return (mMaxValue);
    }
    
    public int          getMinValue () {
        return (mMinValue);
    }
    
    /**
     *	Returns the int value currently contained in the field.
     *		If a parsing error occurs, the field displays an error message,
     *		waits for the user to acknowledge it, sets focus on itself,
     *		then throws a ParsingException.
     *
     *	@exception ParsingException		When the parsing fails.
     */
    public int       getIntegerValue () throws ParsingException {
    	parse ();
    	return (mValue);
    }
    
    public void     setIntegerValue (int v) {
        setText (String.valueOf (v));
    }
}
