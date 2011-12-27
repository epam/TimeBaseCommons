package deltix.util.collections;

/*  ##UTILS## */

import deltix.util.lang.Util;
import deltix.util.collections.generated.*;
import deltix.util.collections.generated.LongToLongHashMap;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class Test_HashMaps {
    private static final int           ALL_NUM_KEYS = 4 << 20;

    @Test
    public void         testShrink () 
        throws LongHashMapBase.KeyNotFoundException 
    {
        final double        SHRINK_FACTOR = 0.25;
        
        LongToLongHashMap   map = addAllRemoveAllTest (SHRINK_FACTOR);        
        int                 cap = map.getCapacity ();
        
        assertEquals (
            "Capacity failed to drop to minimum",
            HashMapBase.MIN_CAPACITY,
            cap
        );      
    }   
    
    @Test
    public void         testNoShrink () 
        throws LongHashMapBase.KeyNotFoundException 
    {
        LongToLongHashMap   map = addAllRemoveAllTest (Double.NaN);
        
        assertTrue (
            "Map shrunk without being asked to", 
            map.getCapacity () >= ALL_NUM_KEYS
        );
    } 
    
    @Test
    public void         slidingWindowTest () 
        throws LongHashMapBase.KeyNotFoundException 
    {
        //
        // this test maintains at most BUFSIZE keys in the hasmap.
        //  make sure the capacity does not grow infinitely.
        //
        final int           BUFSIZE = 10000;
        final long          NUM_CYCLES = 8 << 20;
        
        LongToLongHashMap   map = new LongToLongHashMap ();
        
        for (long ii = 0; ii < NUM_CYCLES; ii++) {
            long            old = ii - BUFSIZE;
            
            if (old >= 0) {
                long    v = map.remove (old);
            
                assertEquals (-old, v);
            }
            
            boolean         isNew = map.put (ii, -ii);
            
            assertTrue (isNew);
        }

        assertEquals (BUFSIZE, map.size ());
        
        double      rate = map.getLoadFactor ();
        
        assertTrue (
            "Map utilization is " + rate, 
            rate > 0.25
        );   
        
        if (!Util.QUIET)
            System.out.println ("Hash map utilization is " + rate);
    }
    
    public LongToLongHashMap addAllRemoveAllTest (double shrinkFactor) 
        throws LongHashMapBase.KeyNotFoundException 
    {
        
        Random              rnd = new Random (2009);
        LongToLongHashMap   map = new LongToLongHashMap ();
        
        map.setShrinkFactor (shrinkFactor);
        
        long []             check = new long [ALL_NUM_KEYS]; 
        int                 n = 0;
        
        while (n < ALL_NUM_KEYS) {
            long    key = rnd.nextLong ();
            
            if (map.containsKey (key))
                continue;
            
            check [n++] = key;
            
            boolean         isNew = map.put (key, -key);
            
            assertTrue (isNew);
        }

        assertEquals (ALL_NUM_KEYS, map.size ());
        
        for (int ii = ALL_NUM_KEYS - 1; ii >= 0; ii--) {
            long    key = check [ii];
            long    v = map.remove (key);
            
            assertEquals (-key, v);
        }
                
        return (map);
    }      
}
