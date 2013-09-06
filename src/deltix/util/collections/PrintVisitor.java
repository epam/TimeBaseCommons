package deltix.util.collections;

import deltix.util.lang.Util;
import java.io.IOException;
import java.util.logging.*;

/**
 *
 */
public class PrintVisitor implements Visitor <Object> {
    private final Appendable            out;
    private final String                prefix;
    private final String                postfix;
    
    public PrintVisitor (Appendable out) {
        this (out, "", "\n");
    }

    public PrintVisitor (Appendable out, String prefix, String postfix) {
        this.out = out;
        this.prefix = prefix;
        this.postfix = postfix;
    }
    
    @Override
    public boolean                  visit (Object object) {
        try {
            out.append (prefix);
            out.append (object == null ? "null" : object.toString ());
            out.append (postfix);
            return (true);
        } catch (IOException iox) {
            Util.LOGGER.log (Level.SEVERE, null, iox);
            return (false);
        }
    }        
}
