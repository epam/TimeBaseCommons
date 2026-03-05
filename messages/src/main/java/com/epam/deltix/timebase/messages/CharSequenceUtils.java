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
package com.epam.deltix.timebase.messages;

/**
 *  Utils for comparing CharSequence
 */
class CharSequenceUtils {
    /**
     *  Compare two CharSequences for equality. A null equals null.
     */
    public static boolean       equals (CharSequence s1, CharSequence s2) {
        return (compare (s1, s2, true) == 0);
    }

    /**
     *  Compare two CharSequences for equality. A null equals null.
     */
    public static boolean       equals (CharSequence s1, CharSequence s2, int maxLength) {
        return (compare (s1, s2, maxLength, true) == 0);
    }

    /**
     *  Compare two CharSequences. A null argument is always less than a non-null argument
     *  and is equal to another null argument.
     *
     *  @param fast     When true, use a fast algorithm, which makes a
     *                  char sequence greater than another if it is longer.
     *                  When false, performs lexicographic comparison.
     */
    public static int           compare (CharSequence s1, CharSequence s2, boolean fast) {
        return (compare (s1, s2, 0, fast));
    }

    /**
     *  Compare two CharSequences. A null argument is always less than a non-null argument
     *  and is equal to another null argument.
     *
     *  @param maxLength Only compare the first <tt>maxLength</tt> characters.
     *                      Send 0 to unlimit.
     *  @param fast     When true, use a fast algorithm, which makes a
     *                  char sequence greater than another if it is longer.
     *                  When false, performs lexicographic comparison.
     */
    public static int           compare (
            CharSequence                s1,
            CharSequence                s2,
            int                         maxLength,
            boolean                     fast
    )
    {
        if (s1 == null)
            if (s2 == null)
                return (0);
            else
                return (-1);
        else if (s2 == null)
            return (1);
        else if (s1 == s2)
            return (0);
        else {
            int         len1 = s1.length ();
            int         len2 = s2.length ();

            if (maxLength > 0) {
                if (maxLength < len1)
                    len1 = maxLength;

                if (maxLength < len2)
                    len2 = maxLength;
            }

            int         diff = len1 - len2;

            if (fast && diff != 0)
                return (diff);

            int         minLength = diff > 0 ? len2 : len1;

            for (int ii = 0; ii < minLength; ii++) {
                int     cdiff = s1.charAt (ii) - s2.charAt (ii);

                if (cdiff != 0)
                    return (cdiff);
            }

            return (diff);
        }
    }

    public static int           fastCompare (CharSequence s1, CharSequence s2) {

        int         len1 = s1.length ();
        int         len2 = s2.length ();

        int         diff = len1 - len2;

        if (diff != 0)
            return (diff);

        for (int ii = 0; ii < len1; ii++) {
            int     cdiff = s1.charAt (ii) - s2.charAt (ii);

            if (cdiff != 0)
                return (cdiff);
        }

        return (diff);
    }

    public static int               hashCode (CharSequence cs) {
        if (cs == null)
            return (0);

        if (cs.getClass () == String.class) // String caches hash code
            return (cs.hashCode ());

        int         len = cs.length ();
        int         hc = 0;

        for (int i = 0; i < len; i++)
            hc = 31 * hc + cs.charAt (i);

        return (hc);
    }
}