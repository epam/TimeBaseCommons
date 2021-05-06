package com.epam.deltix.util.collections;

import com.epam.deltix.util.collections.generated.*;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class Test_HashMaps {

    private static class HorribleLong {
        public final long  value;

        public HorribleLong (long value) {
            this.value = value;
        }

        @Override
        public boolean  equals (Object obj) {
            return (((HorribleLong) obj).value == value);
        }

        @Override
        public int      hashCode () {
            return (7);
        }     
        
        @Override
        public String   toString () {
            return ("HLONG(" + value + ")");
        }
    }
    
    private static final int           ALL_NUM_KEYS = 4 << 20;

    @Test
    public void         smoke () {
        ObjectToLongHashMap <HorribleLong>  map = 
            new ObjectToLongHashMap <HorribleLong> ();
        
        assertEquals (0, map.size ()); 
        
        assertTrue (map.put (new HorribleLong (100), 100));
        assertEquals (1, map.size ());        
        assertEquals (100, map.get (new HorribleLong (100), -1));
        
        // put 200
        
        assertTrue (map.put (new HorribleLong (200), 200));
        assertEquals (2, map.size ());        
        assertEquals (100, map.get (new HorribleLong (100), -1));
        assertEquals (200, map.get (new HorribleLong (200), -1));
        
        // put 300
        
        assertTrue (map.put (new HorribleLong (300), 300));
        assertEquals (3, map.size ());        
        assertEquals (300, map.get (new HorribleLong (300), -1));
        assertEquals (100, map.get (new HorribleLong (100), -1));
        assertEquals (200, map.get (new HorribleLong (200), -1));
        
        // remove 100 (creates a hole)
        assertEquals (100, map.remove (new HorribleLong (100), -1));
        assertEquals (2, map.size ());        
        assertEquals (-1, map.get (new HorribleLong (100), -1));
        // finding 200 should moves it to first spot in the (single) chain
        assertEquals (200, map.get (new HorribleLong (200), -1));
        assertEquals (300, map.get (new HorribleLong (300), -1));
        // find 200 again, just in case
        assertEquals (200, map.get (new HorribleLong (200), -1));
        
        // put 400 - should fill the hole left by 200
        assertTrue (map.put (new HorribleLong (400), 400));
        assertEquals (3, map.size ());        
        assertEquals (300, map.get (new HorribleLong (300), -1));
        assertEquals (400, map.get (new HorribleLong (400), -1));
        assertEquals (200, map.get (new HorribleLong (200), -1));
    }
    
    @Test
    public void         testBadHash () {
        ObjectToLongHashMap <HorribleLong>  unfortunateMap = 
            new ObjectToLongHashMap <HorribleLong> ();
        
        final int           NUM = 10000;    // keep it even
        
        HorribleLong []     keys = new HorribleLong [NUM];
        
        for (int ii = 0; ii < NUM; ii++) {
            HorribleLong    key = keys [ii] = new HorribleLong (ii);
            
            boolean     b = unfortunateMap.put (key, ii);
            
            assertTrue (b);
            assertEquals (ii + 1, unfortunateMap.size ());
        }
                
        for (int ii = 0; ii < NUM; ii++) {
            long        value = unfortunateMap.get (keys [ii], -1);
            
            assertEquals (ii, value);
        }
        
        //  Remove top half
        for (int ii = NUM / 2; ii < NUM; ii++) {
            long        value = unfortunateMap.remove (keys [ii], -1);
            
            assertEquals (ii, value);
        }
        
        assertEquals (NUM / 2, unfortunateMap.size ());
    }
    
    @Test
    public void         testAgainstJavaMapNoShrink () {
        testAgainstJavaMap (Double.NaN);
    }
        
    @Test
    public void         testAgainstJavaMapWithShrink () {
        testAgainstJavaMap (0.25);
    }
        
    @Test
    public void         slidingWindowTest1 () {
        slidingWindowTest (1);
    }
    
    @Test
    public void         slidingWindowTest10K () {
        slidingWindowTest (10000);
    }
    
    @Test
    public void         slidingWindowTest100 () {
        slidingWindowTest (100);
    }
    
    // shrink disabled for now
    //@Test
    public void         testShrink () {
        LongToLongHashMap   map = addAllRemoveAllTest (0.25);        
        int                 cap = map.getCapacity ();
        
        assertEquals (
            "Capacity failed to drop to minimum: " + cap,
            HashMapBase.MIN_CAPACITY,
            cap
        );      
    }   
    
    @Test
    public void         testNoShrink () {
        LongToLongHashMap   map = addAllRemoveAllTest (Double.NaN);
        
        assertTrue (
            "Map shrunk without being asked to", 
            map.getCapacity () >= ALL_NUM_KEYS
        );
    } 
    
    public void         slidingWindowTest (int bufSize) {
        //
        // this test maintains at most bufSize keys in the hasmap.
        //  make sure the capacity does not grow infinitely.
        //
        final long          NUM_CYCLES = 10000000;
        
        LongToLongHashMap   map = new LongToLongHashMap ();
        
        for (long ii = 0; ii < NUM_CYCLES; ii++) {
            long            old = ii - bufSize;
            
            if (old >= 0) {
                long        v = map.remove (old, Integer.MAX_VALUE);
            
                assertEquals (-old, v);
            }
            
            boolean         isNew = map.put (ii, -ii);
            
            assertTrue (isNew);
        }

        assertEquals (bufSize, map.size ());
        
        int                 cap = map.getCapacity ();
        
        assertTrue (
            "Map capacity = " + cap + " for bufSize = " + bufSize, 
            cap <= Math.max (VLinkHashMapBase.MIN_CAPACITY, bufSize * 4)
        );                   
    }
    
    public LongToLongHashMap addAllRemoveAllTest (double shrinkFactor) {
        
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
            long    v = map.remove (key, -key - 1 /* unexpected value */);
            
            assertEquals (-key, v);
        }
                
        return (map);
    }     
    
    public void         testAgainstJavaMap (double shrinkFactor) {        
        Random                  rnd = new Random (2009);
        IntegerToIntegerHashMap map = new IntegerToIntegerHashMap ();
        
        map.setShrinkFactor (shrinkFactor);
        
        HashMap <Integer, Integer> check = new HashMap <Integer, Integer> ();
                      
        for (int ii = 0; ii < 1000000; ii++) {
            {
                int             key = rnd.nextInt (100000);
                int             value = rnd.nextInt ();

                Integer         out = check.put (key, value);
                int             actual = map.putAndGet (key, value, -1);

                if (out == null)
                    assertEquals (-1, actual);
                else
                    assertEquals (out, (Object) actual);
            }
            
            if (ii % 1000 == 999) {
                int         n = map.size ();
                
                assertEquals (check.size (), n);
                
                for (Map.Entry <Integer, Integer> e : check.entrySet ()) {
                    int     expectedValue = e.getValue ();
                    
                    assertEquals (
                        expectedValue, 
                        map.get (e.getKey (), expectedValue - 1 /* unexpected */)
                    );
                }
                
                // Remove 5/6 of all elements
                int         m = 0;
                int []      keys = new int [n * 5 / 6];
                
                for (Integer key : check.keySet ()) {
                    keys [m++] = key;
                    
                    if (m == keys.length)
                        break;
                }
                        
                for (int key : keys)
                    assertEquals (check.remove (key), (Object) map.remove (key, Integer.MIN_VALUE));

// shrink disabled for now
//                if (!Double.isNaN (shrinkFactor)) 
//                    assertTrue (map.getCapacity () < check.size () / shrinkFactor);        
            }
        }                        
    }

    @Test
    public void testReset() {
        LongToObjectHashMap<String> map = new LongToObjectHashMap<String>();

        for (long i = 0; i < 10; i++)
            map.put(i, String.valueOf(i));

        ArrayList<String> dump = new ArrayList<String>();

        ElementsEnumeration<String> e = map.elements();

        while (e.hasMoreElements())
            dump.add(e.nextElement());

        for (int i = 0; i < map.size(); i++)
            assertTrue(dump.contains(String.valueOf(i)));

        dump.clear();

        for (long i = 10; i < 20; i++)
            map.put(i, String.valueOf(i));

        e.reset();

        assertTrue(e.hasMoreElements());

        while (e.hasMoreElements())
            dump.add(e.nextElement());

        for (int i = 0; i < map.size(); i++)
            assertTrue(dump.contains(String.valueOf(i)));
    }
}
