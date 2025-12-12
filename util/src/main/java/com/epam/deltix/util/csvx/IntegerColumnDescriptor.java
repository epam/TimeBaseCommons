package com.epam.deltix.util.csvx;

import com.epam.deltix.util.text.CharSequenceParser;

/**
 *
 */
public class IntegerColumnDescriptor extends ColumnDescriptor {    
    public IntegerColumnDescriptor () { 
    }
    
    public IntegerColumnDescriptor (String header) {
        super (header);
    }    
    
    public long                 getLong () {
        return (CharSequenceParser.parseLong (getCharSequence ()));
    }
    
    public int                  getInt () {
        return (CharSequenceParser.parseInt (getCharSequence ()));
    }
}
