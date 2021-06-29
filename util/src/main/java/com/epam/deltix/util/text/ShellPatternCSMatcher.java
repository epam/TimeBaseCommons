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

/**
 *
 */
public final class ShellPatternCSMatcher implements CSMatcher {
    public static CSMatcher     INSTANCE = new ShellPatternCSMatcher ();
    
    public static boolean       isPattern (CharSequence pattern) {
        final int n = pattern.length ();
        
        for (int ii = 0; ii < n; ii++) {
            switch (pattern.charAt (ii)) {
                case '?':
                case '*':
                    return (true);                    
            }
        }
        
        return (false);
    }
    
    private ShellPatternCSMatcher () { }
    
    public boolean  matches (CharSequence text, CharSequence pattern) {
        return (matches (text, 0, pattern, 0));
    }
    
    private boolean matches (
        CharSequence    text, 
        int             tpos,
        CharSequence    pattern,
        int             ppos
    )
    {
        final int       tlen = text.length ();
        final int       plen = pattern.length ();
        
        for (;;) {
            if (tpos == tlen) 
                return (ppos == plen);
            
            if (ppos == plen)
                return (false);
                   
            char        pc = pattern.charAt (ppos++);
            
            switch (pc) {
                case '?':   
                    tpos++;     
                    break;
                    
                case '*':
                    for (int n = tpos; n <= tlen; n++)
                        if (matches (text, n, pattern, ppos))
                            return (true);
                    
                    return (false);
                    
                default:
                    if (text.charAt (tpos++) != pc)
                        return (false);
                    break;
            }
        }
    }    
}