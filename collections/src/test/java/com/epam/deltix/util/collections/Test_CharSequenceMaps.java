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
package com.epam.deltix.util.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class Test_CharSequenceMaps {
    private StringBuilder           sb = new StringBuilder ();

    private CharSequence            wrap (String text) {
        sb.setLength (0);
        sb.append (text);
        return (sb);
    }

    @Test
    public void         testIntegerMap () {
        CharSequenceToIntegerMap    map = new CharSequenceToIntegerMap ();

        assertTrue (map.put (wrap ("MOON"), 456));
        assertTrue (map.put (wrap ("SUN"), 235));
        assertFalse (map.put (wrap ("MOON"), -9998));

        assertEquals (map.size (), 2);

        assertEquals (map.get (wrap ("MOON"), -1), -9998);
        assertEquals (map.get (wrap ("SUN"), -1), 235);

        assertEquals (map.remove (wrap ("MOON"), -1), -9998);
        assertEquals (map.size (), 1);
    }

    @Test
    public void         testObjectMap () {
        Object                      a = new Object ();
        Object                      b = new Object ();
        Object                      c = new Object ();

        CharSequenceToObjectMap <Object>    map = new CharSequenceToObjectMap <> ();

        assertEquals (null, map.put (wrap ("MOON"), a));
        assertEquals (null, map.put (wrap ("SUN"), b));
        assertEquals (a, map.put (wrap ("MOON"), c));
        assertEquals (2, map.size ());
        assertEquals (c, map.get (wrap ("MOON")));
        assertEquals (b, map.get (wrap ("SUN")));
        assertEquals (c, map.remove (wrap ("MOON")));
        assertEquals (1, map.size ());
    }
    
    @Test
    public void         testObjectMapQuick () {
        Object                      a = new Object ();
        Object                      b = new Object ();
        Object                      c = new Object ();

        CharSequenceToObjectMapQuick <Object>    map = new CharSequenceToObjectMapQuick <> ();

        assertEquals (null, map.putAndGet (wrap ("MOON"), a, null));
        assertEquals (null, map.putAndGet (wrap ("SUN"), b, null));
        assertEquals (a, map.putAndGet (wrap ("MOON"), c, null));
        assertEquals (2, map.size ());
        assertEquals (c, map.get (wrap ("MOON"), null));
        assertEquals (b, map.get (wrap ("SUN"), null));
        assertEquals (c, map.remove (wrap ("MOON"), null));
        assertEquals (1, map.size ());
    }
    
    @Test
    public void         bigIntegerMap () {
        int                         n = 8000;
        Map <String, Integer>       check = new HashMap <String, Integer> (n);
        CharSequenceToIntegerMap    map = new CharSequenceToIntegerMap (); // Make it grow
        Random                      rnd = new Random (2009);
        
        for (int ii = 0; ii < n; ii++) {
            int                     u = rnd.nextInt (Integer.MAX_VALUE);
            
            sb.setLength (0);
            
            while (u != 0) {
                sb.append ((char) ('A' + (u % 26)));
                u = u / 26;
            }
            
            map.put (sb, ii);
            check.put (sb.toString (), ii);
        }
        
        assertEquals (map.size (), check.size ());
        assertFalse (map.containsKey (""));
        
        for (Map.Entry <String, Integer> e : check.entrySet ()) {
            int expectedValue = e.getValue ();
            
            assertTrue (map.containsKey (e.getKey ()));
            assertEquals (expectedValue, map.get (e.getKey (), expectedValue - 1));
            assertEquals (expectedValue, map.remove (e.getKey (), expectedValue - 1));
        }
        
        assertEquals (map.size (), 0);
    }
}