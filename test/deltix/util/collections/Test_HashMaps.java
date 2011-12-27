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
    public void         slidingWindowTest1 () 
        throws LongHashMapBase.KeyNotFoundException 
    {
        slidingWindowTest (1);
    }
    
    @Test
    public void         slidingWindowTest10K () 
        throws LongHashMapBase.KeyNotFoundException 
    {
        slidingWindowTest (10000);
    }
    
    @Test
    public void         slidingWindowTest100 () 
        throws LongHashMapBase.KeyNotFoundException 
    {
        slidingWindowTest (100);
    }
    
    @Test
    public void         testShrink () 
        throws LongHashMapBase.KeyNotFoundException 
    {
        LongToLongHashMap   map = addAllRemoveAllTest (0.25);        
        int                 cap = map.getCapacity ();
        
        assertEquals (
            "Capacity failed to drop to minimum: " + cap,
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
    
    public void         slidingWindowTest (int bufSize) 
        throws LongHashMapBase.KeyNotFoundException 
    {
        //
        // this test maintains at most bufSize keys in the hasmap.
        //  make sure the capacity does not grow infinitely.
        //
        final long          NUM_CYCLES = 8 << 20;
        
        LongToLongHashMap   map = new LongToLongHashMap ();
        
        for (long ii = 0; ii < NUM_CYCLES; ii++) {
            long            old = ii - bufSize;
            
            if (old >= 0) {
                long    v = map.remove (old);
            
                assertEquals (-old, v);
            }
            
            boolean         isNew = map.put (ii, -ii);
            
            assertTrue (isNew);
        }

        assertEquals (bufSize, map.size ());
        
        int                 cap = map.getCapacity ();
        
        assertTrue (
            "Map capacity = " + cap + " for bufSize = " + bufSize, 
            cap <= Math.max (HashMapBase.MIN_CAPACITY, bufSize * 4)
        );                   
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
