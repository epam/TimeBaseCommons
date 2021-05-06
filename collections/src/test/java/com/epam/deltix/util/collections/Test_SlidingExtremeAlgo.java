package com.epam.deltix.util.collections;

import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class Test_SlidingExtremeAlgo {
    public static final int     WIDTH = 47; // prime!
    public static final int     RESET_PERIOD = 30691;
    public static final int     RANGE = 129;
    public static final int     TOTAL = 1000000;
    
    static final int []         UNIT_TEST_SEQ = { 
        1,  2,  3,  3,  2,  2,  1,  4,  0,  4,  4,  4
    };
    
    static final int []         UNIT_TEST_EXP = { 
        1,  1,  1,  2,  2,  2,  1,  1,  0,  0,  0,  4
    };            
        
    @Test
    public void     unitTestSlidingIntMinimum () {        
        SlidingIntMinimum   sim = new SlidingIntMinimum (3);
        
        for (int ii = 0; ii < UNIT_TEST_SEQ.length; ii++) {
            int     actualMinimum = sim.offer (UNIT_TEST_SEQ [ii]);
                
            //System.out.println ("Added " + UNIT_TEST_SEQ [ii] + ":");
            //sim.dump ();
            
            assertEquals ("At " + ii, UNIT_TEST_EXP [ii], actualMinimum);
        }
    }
    
    @Test
    public void     stressTestSlidingIntMinimum () {
        Random          r = new Random (2011);        
        int             count = 0;
        int []          window = new int [WIDTH];
        int             expectedMinimum = Integer.MAX_VALUE;
     
        SlidingIntMinimum   sim = new SlidingIntMinimum (WIDTH);
        
        assertTrue (sim.isEmpty ());
        
        for (int ii = 0; ii < TOTAL; ii++) {
            if (ii % RESET_PERIOD == (RESET_PERIOD - 1)) {
                sim.clear ();
                assertTrue (sim.isEmpty ());
                
                count = 0;
                expectedMinimum = Integer.MAX_VALUE;
            }
            
            int         value = r.nextInt (RANGE);
            
            if (count == WIDTH) {
                if (expectedMinimum == window [0]) {
                    expectedMinimum = window [1];
                    
                    for (int jj = 2; jj < WIDTH; jj++)
                        if (window [jj] < expectedMinimum)
                            expectedMinimum = window [jj];
                }
                
                System.arraycopy (window, 1, window, 0, WIDTH - 1);
                window [WIDTH - 1] = value;
            }
            else
                window [count++] = value;
            
            if (value < expectedMinimum)
                expectedMinimum = value;
            
            int     actualMinimum = sim.offer (value);
            
            assertEquals (expectedMinimum, actualMinimum);
        }
    }
}
