package com.epam.deltix.util.collections;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;
import com.epam.deltix.util.lang.Util;
import java.io.IOException;


/**
 *
 */
public class PrintVisitor implements Visitor <Object> {
    private static final Log LOG = LogFactory.getLog(PrintVisitor.class);
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
            LOG.error ("Error printing %s").with(iox);
            return (false);
        }
    }        
}
