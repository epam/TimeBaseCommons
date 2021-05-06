package com.epam.deltix.util.bcel;

/**
 *
 */
public class EnumValue {
    public final String         className;
    public final String         valueName;

    public EnumValue (String className, String valueName) {
        this.className = className;
        this.valueName = valueName;
    }
    
    @Override
    public String                       toString () {
        return ("enum '" + className + "' value '" + valueName + "'");
    }
    
}
