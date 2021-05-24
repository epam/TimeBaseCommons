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

import org.junit.*;
import static org.junit.Assert.*;

public class Test_CharSequenceParser {
    private static final String     PREFIX = " %%$";
    private static final String     POSTFIX = "&&$$#";
    private static final int        OFFSET = PREFIX.length ();

    private void    testInt (long expected, String s) {
        String          mangled = PREFIX + s + POSTFIX;
        int             end = OFFSET + s.length ();

        {
            long        actual = CharSequenceParser.parseLong (mangled, OFFSET, end);

            assertEquals (expected, actual);
        }

        try {
            int         actual = CharSequenceParser.parseInt (mangled, OFFSET, end);
            assertEquals (expected, actual);
        } catch (NumberFormatException x) {
            assertFalse (
                "parseInt (" + expected + ") generated unexpected NumberFormatException",
                expected >= Integer.MIN_VALUE && expected <= Integer.MAX_VALUE
            );
        }

        try {
            short        actual = CharSequenceParser.parseShort (mangled, OFFSET, end);
            assertEquals (expected, actual);
        } catch (NumberFormatException x) {
            assertFalse (
                "parseShort (" + expected + ") generated unexpected NumberFormatException",
                expected >= Short.MIN_VALUE && expected <= Short.MAX_VALUE
            );
        }

        try {
            byte        actual = CharSequenceParser.parseByte (mangled, OFFSET, end);
            assertEquals (expected, actual);
        } catch (NumberFormatException x) {
            assertFalse (
                "parseByte (" + expected + ") generated unexpected NumberFormatException",
                expected >= Byte.MIN_VALUE && expected <= Byte.MAX_VALUE
            );
        }
    }

    private void        testInt (long expected) {
        testInt (expected, String.valueOf (expected));

        if (expected >= 0)
            testInt (expected, "+" + expected);

        testInt (expected, String.format ("%,d", expected));
    }

    private void        testFail (String s) {
        try {
            CharSequenceParser.parseLong ("-12357657612376516571623577623");
            assertFalse ("parseLong (" + s + ") did not generate an exception", true);
        } catch (NumberFormatException x) {
            // GOOD
       }
    }

    @Test
    public void         testInts () {
        testInt (0);
        testInt (123687);
        testInt (-234687);
        
        testInt (Byte.MIN_VALUE);
        testInt (Byte.MIN_VALUE - 1);
        testInt (Short.MIN_VALUE);
        testInt (Short.MIN_VALUE - 1);
        testInt (Integer.MIN_VALUE);
        testInt (Integer.MIN_VALUE - 1L);
        testInt (Long.MIN_VALUE);

        testInt (Byte.MAX_VALUE);
        testInt (Byte.MAX_VALUE + 1);
        testInt (Short.MAX_VALUE);
        testInt (Short.MAX_VALUE + 1);
        testInt (Integer.MAX_VALUE);
        testInt (Integer.MAX_VALUE + 1L);
        testInt (Long.MAX_VALUE);

        testFail ("-12357657612376516571623577623");
        testFail ("12357657612376516571623577623");
        testFail ("9223372036854775808");   // 1 over MAX
        testFail ("-9223372036854775809");   // 1 under MIN
    }    
    
    
}