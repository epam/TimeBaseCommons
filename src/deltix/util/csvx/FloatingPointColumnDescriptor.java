package deltix.util.csvx;

import deltix.util.text.CharSequenceParser;

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
        return (CharSequenceParser.parseDecimal (getCharSequence ()));
    }
    
    public float                getFloat () {
        return ((float) (CharSequenceParser.parseDecimal (getCharSequence ())));
    }
}
