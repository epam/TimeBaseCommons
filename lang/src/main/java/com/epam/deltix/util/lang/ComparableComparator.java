package com.epam.deltix.util.lang;

import java.util.Comparator;

/**
 *
 */
public class ComparableComparator <T> implements Comparator <T> {    
    @SuppressWarnings ("unchecked")
    public int compare (T o1, T o2) {
        if (o1 == null)
            if (o2 == null)
                return (0);
            else
                return (-1);
        else 
            if (o2 == null)
                return (1);
        else                
            return (((Comparable) o1).compareTo (o2));
    }    
}
