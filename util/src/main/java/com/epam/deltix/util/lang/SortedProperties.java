package com.epam.deltix.util.lang;

import com.epam.deltix.util.collections.ArrayEnumeration;
import java.util.*;

/**
 *  Same as java.util.Properties but stores itself in sorted order.
 */
public class SortedProperties extends Properties {
    public SortedProperties (Properties defaults) {
        super (defaults);
    }

    public SortedProperties () {
    }

    @Override
    public synchronized Enumeration <Object>    keys () {
        Object []   a = keySet ().toArray ();
        Arrays.sort (a);
        return (new ArrayEnumeration <Object> (a));
    }
}
