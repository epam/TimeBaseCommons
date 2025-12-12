package com.epam.deltix.util.text.tte;

import java.io.*;

/**
 *
 */
public class TextElement extends Element {
    private final String            text;

    public TextElement (String text) {
        this.text = text;
    }
    
    @Override
    protected void     expand (Values values, Writer out)
        throws IOException
    {
        out.write (text);
    }
}
