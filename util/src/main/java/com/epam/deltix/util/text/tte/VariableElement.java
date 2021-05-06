package com.epam.deltix.util.text.tte;

import java.io.*;

/**
 *
 */
public class VariableElement extends Element {
    private final String            key;
    private final Format            format;
        
    public VariableElement (String key, Format format) {
        this.key = key;
        this.format = format;
    }
    
    @Override
    protected void              expand (Values values, Writer out)
        throws IOException
    {
        String                      text = values.getValue (key);
        
        out.write (text);
    }
}
