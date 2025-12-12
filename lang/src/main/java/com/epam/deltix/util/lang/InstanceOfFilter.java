package com.epam.deltix.util.lang;

/**
 *  Filters objects based on whether they are convertible to the specified type.
 */
public class InstanceOfFilter implements Filter <Object> {
    protected final Class <?>           cls;

    public InstanceOfFilter (Class <?> cls) {
        this.cls = cls;
    }

    @Override
    public boolean                      accept (Object value) {
        return (value != null && cls.isAssignableFrom (value.getClass ()));
    }        
}
