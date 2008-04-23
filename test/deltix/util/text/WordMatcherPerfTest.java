package deltix.util.text;

import deltix.util.csvx.*;
import deltix.util.io.IOUtil;
import java.io.*;
import java.util.*;
import java.util.regex.*;
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
        
        WordMatcher         code32 = wm.compile (true);
        WordMatcher         code16 = wm.compile (false);
        
        test ("Empty", null);
        test ("Compiled 32-bit", code32);
        test ("Compiled 16-bit", code16);
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
