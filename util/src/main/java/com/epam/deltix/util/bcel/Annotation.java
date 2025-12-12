package com.epam.deltix.util.bcel;

/**
 *
 */
public class Annotation {
    public String                       type;
    public AnnotationElement []         elements;

    @Override
    public String                       toString () {
        StringBuilder   sb = new StringBuilder (type);
        
        sb.append (" {");
        
        for (AnnotationElement e : elements) {
            sb.append (" ");
            sb.append (e);
        }
        
        sb.append (" }");
        return (sb.toString ());
    }        
}
