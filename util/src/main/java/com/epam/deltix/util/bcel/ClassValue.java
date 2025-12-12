package com.epam.deltix.util.bcel;

/**
 *
 */
public final class ClassValue {
    public final String         className;

    public ClassValue (String className) {
        this.className = className;
    }
    
    @Override
    public String                       toString () {
        return ("class '" + className + "'");
    }
    
}
