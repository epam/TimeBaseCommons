package com.epam.deltix.util.collections;

import java.util.*;

/**
 *
 */
public class Sets {
    public static int   getUnionSize (Set a, Set b) {
        int     sizeA = a.size ();
        int     sizeB = b.size ();
        
        if (sizeA < sizeB) {
            for (Object p : a)
                if (b.contains (p))
                    sizeB--;
        }
        else {
            for (Object p : b)
                if (a.contains (p))
                    sizeA--;
        }
        
        return (sizeA + sizeB);
    }    
}
