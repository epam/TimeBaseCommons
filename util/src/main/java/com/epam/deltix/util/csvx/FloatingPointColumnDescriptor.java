package com.epam.deltix.util.csvx;

import com.epam.deltix.util.text.CharSequenceParser;

/**
 *
 */
public class FloatingPointColumnDescriptor extends ColumnDescriptor {    
    public FloatingPointColumnDescriptor () { 
    }
    
    public FloatingPointColumnDescriptor (String header) {
        super (header);
    }    
    
    public double               getDouble () {
        return (CharSequenceParser.parseDouble (getCharSequence ()));
    }
    
    public float                getFloat () {
        return (CharSequenceParser.parseFloat (getCharSequence ()));
    }
}
