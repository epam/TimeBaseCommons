package com.epam.deltix.util.bcel;

/**
 *
 */
public class AnnotationElement {
    public String           name;
    public Object           value;
    
    @Override
    public String                       toString () {
        return (name + "=" + value);
    }
}
