package com.epam.deltix.util.text.tte;

import java.io.*;
import java.util.*;

/**
 *
 */
public class TinyTemplateEngine {
    private List <Element>      elements = new ArrayList <> ();
    
    private static char         readChar (Reader rd) throws IOException {
        int     c = rd.read ();
        
        if (c < 0)
            throw new EOFException ();
        
        return ((char) c);
    }
    
    public TinyTemplateEngine () {
    }
    
    public void         parse (File f) throws IOException {
        try (Reader rd = new BufferedReader (new FileReader (f))) {
            parse (rd);
        }
    }
    
    public void         parseNoX (String s) {
        try {
            parse (s);
        } catch (IOException x) {
            throw new RuntimeException (x);
        }
    }
    
    public void         parse (String s) throws IOException {
        try (Reader rd = new StringReader (s)) {
            parse (rd);
        } 
    }
    
    public void         parse (InputStream is) throws IOException {
        parse (new InputStreamReader (is, "UTF-8"));
    }
    
    public void         parse (Reader rd) throws IOException {
        StringBuilder   sb = new StringBuilder ();
        
        for (;;) {
            int         c = rd.read ();
            
            if (c < 0)
                break;
            
            if (c == '#') {
                char   c2 = readChar (rd);
                
                if (c2 == '#') {
                    sb.append (c2);
                    continue;
                }
                
                flushStaticText (sb);
                //
                //  Read special element
                //
                sb.append (c2);
                
                for (;;) {
                    c2 = readChar (rd);
                    
                    if (c2 == '#') {
                        addSpecial (sb);
                        break;
                    }
                    
                    sb.append (c2);
                }
            }
            else 
                sb.append ((char) c);            
        }       
        
        flushStaticText (sb);
    }

    private void                flushStaticText (StringBuilder sb) {
        if (sb.length () == 0)
            return;
        
        elements.add (new TextElement (sb.toString ()));
        sb.setLength (0);
    }
    
    private void                addSpecial (StringBuilder sb) {
        String      name = sb.toString ();
        Format      fmt = Format.NONE;
        
        elements.add (new VariableElement (name, fmt));
        sb.setLength (0);
    }
    
    public void                 expand (Values values, Writer out) 
        throws IOException
    {
        for (Element e : elements)
            e.expand (values, out);
    }
    
    public String               expand (Values values) {
        StringWriter    swr = new StringWriter ();
        
        try {
            expand (values, swr);
        } catch (IOException x) {
            throw new RuntimeException (x);
        } 
        
        return (swr.toString ());
    }
}
