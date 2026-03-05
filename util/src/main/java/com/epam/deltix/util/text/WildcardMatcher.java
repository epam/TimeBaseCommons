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

import java.util.StringTokenizer;

public class WildcardMatcher {

    /**
     * Posted by JemTek in JavaForum
     * @param text text to match (e.g. "WildcardMatcher.java")
     * @param pattern pattern to match (e.g. "*.java)
     * @return true if given text matches the pattern
     */
    public static boolean match (String text, String pattern) {
        if (text == null)
            throw new IllegalArgumentException ("null text");
        if (pattern == null)
            throw new IllegalArgumentException ("null pattern");

        int idx = 0;
        boolean wild = false;

        StringTokenizer tokens = new StringTokenizer (pattern, "*", true);
        while (tokens.hasMoreTokens ()) {
            String token = tokens.nextToken ();

            if (wild == true) {
                wild = false;
                if (text.indexOf (token, idx) > idx)
                    idx = text.indexOf (token, idx);
            }

            if (token.equals ("*"))
                wild = true;
            else
            if (text.indexOf (token, idx) == idx)
                idx += token.length ();
            else
                break;

            if (!tokens.hasMoreTokens ()) {
                if (token.equals ("*") || text.endsWith (token))
                    idx = text.length ();
            }
        }

        return idx == text.length();

    }
}