/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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
        words = IOUtil.readLinesFromClassPath ("com/epam/deltix/util/text/tickers.txt");
        
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