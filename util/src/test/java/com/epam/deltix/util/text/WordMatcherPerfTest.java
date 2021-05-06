package com.epam.deltix.util.text;

import com.epam.deltix.util.io.IOUtil;
import org.junit.*;
   
public class WordMatcherPerfTest {    
    static String []    words;
    static long         base = 0;
    
    public static void      main (String [] args) throws Exception {
        new WordMatcherPerfTest ().runTest ();
    }
    
    @Test
    public void      runTest () throws Exception {
        words = IOUtil.readLinesFromClassPath ("deltix/util/text/tickers.txt");
        
        WordMatcherBuilder      wm = new WordMatcherBuilder ();
        
        for (String s : words) {
            wm.add (s);
        }
        
        WordMatcher         compiled = wm.compile ();
        
        test ("Empty", null);
        test ("Compiled", compiled);
        test ("Interpreted", wm);        
    }

    private static void     test (String tag, WordMatcher m) {
        System.out.println (tag);
        
        long            t0 = System.currentTimeMillis ();
        final int       n = 100000000;

        for (int ii = 0; ii < n; ii++) {
            String      s = words [ii % words.length];
            
            if (m != null) {
                if (!m.matches (s))
                    throw new AssertionError ();
            }
        }
        
        long            t1 = System.currentTimeMillis ();
        long            t = t1 - t0;
        
        if (m == null) {
            base = t;
            System.out.println (t + "ms baseline.");
        }
        else {
            t -= base;                
            System.out.println (t + "ms; Duration of one test: " + t * 0.001 / n); 
        }
    }
 }
