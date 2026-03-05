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
package com.epam.deltix.util.time;

import org.junit.*;
import static org.junit.Assert.*;

public class Test_TimeDomain {
    private TimeDomain     td;

    private static boolean quiet = Boolean.getBoolean("quiet");

    @Before
    public void     setUp () {
        td = new TimeDomain ();

        td.add (10, 20);
        td.add (15, 25);

        td.add (35, 45);

        td.add (35, 45);// repeat

        td.add (35, 46);

        td.add (34, 46);

        if ( ! quiet)
            System.out.println (td);
    }
    
    @Test
    public void     spotCheck () {
        assertTrue (td.includes (34));
        assertTrue (td.includes (10));
        assertTrue (td.includes (19));
        assertFalse (td.includes (25));
    }    


    private final String [] expected = {
            "0 -> X",
            "1 -> X",
            "2 -> X",
            "3 -> X",
            "4 -> X",
            "5 -> X",
            "6 -> X",
            "7 -> X",
            "8 -> X",
            "9 -> X",
            "10 -> 0",
            "11 -> 1",
            "12 -> 2",
            "13 -> 3",
            "14 -> 4",
            "15 -> 5",
            "16 -> 6",
            "17 -> 7",
            "18 -> 8",
            "19 -> 9",
            "20 -> 10",
            "21 -> 11",
            "22 -> 12",
            "23 -> 13",
            "24 -> 14",
            "25 -> X",
            "26 -> X",
            "27 -> X",
            "28 -> X",
            "29 -> X",
            "30 -> X",
            "31 -> X",
            "32 -> X",
            "33 -> X",
            "34 -> 15",
            "35 -> 16",
            "36 -> 17",
            "37 -> 18",
            "38 -> 19",
            "39 -> 20",
            "40 -> 21",
            "41 -> 22",
            "42 -> 23",
            "43 -> 24",
            "44 -> 25",
            "45 -> 26",
            "46 -> X",
            "47 -> X",
            "48 -> X",
            "49 -> X" };
    @Test
    public void     testXForm () {
        td.buildIndex ();

        for (int ii = 0; ii < 50; ii++) {
            long    o = td.g2c (ii, false);

            String actual;
            if (o == TimeDomain.TIME_EXCLUDED) {
                actual = (ii + " -> X");
                continue;
            } else {
                long    t = td.c2g (o);
                assertEquals (ii, t);
                actual = (ii + " -> " + o);
            }

            if ( ! quiet)
                System.out.println (actual);

            assertEquals (expected[ii], actual);

        }
    }
}