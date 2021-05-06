package com.epam.deltix.util.collections;

import com.epam.deltix.util.lang.Util;
import java.util.*;

/**
 *
 */
public class SingleElementList <T>
    extends AbstractList <T>
{
    private T           mObject;
    
    public SingleElementList (T object) {
        mObject = object;
    }
    
    public int      lastIndexOf (Object o) {
        return (indexOf (o));             
    }

    public int      indexOf (Object o) {
        return (Util.xequals (mObject, o) ? 0 : -1);  
    }

    public T        get (int index) {
        if (index != 0)
            throw new ArrayIndexOutOfBoundsException (index);
        
        return (mObject);
    }

    public int      size () {
        return (1);
    }    
}
