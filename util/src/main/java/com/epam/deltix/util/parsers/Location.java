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
package com.epam.deltix.util.parsers;

/**
 *
 */
public abstract class Location {
    public static final int     NONE = 0xFFFF;
    
    public static int           getStartLine (long location) {
        return ((int) (location >>> 48));
    }

    public static int           getEndLine (long location) {
        return ((int) ((location >>> 16) & 0xFFFF));
    }

    public static int           getStartPosition (long location) {
        return ((int) ((location >>> 32) & 0xFFFF));
    }

    public static int           getEndPosition (long location) {
        return ((int) (location & 0xFFFF));
    }

    public static long          getStart (long location) {
        return (location >>> 32);
    }

    public static long          getEnd (long location) {
        return (location & 0xFFFFFFFFL);
    }

    public static long          combine (long fromPos, long toPos) {
        return (fromPos << 32 | toPos);
    }
    
    public static long          fromTo (long fromLocation, long toLocation) {
        return ((fromLocation & 0xFFFFFFFF00000000L) | (toLocation & 0xFFFFFFFF));
    }
    
    public static String        toString (long location) {
        int     startLine = Location.getStartLine (location);
        
        if (startLine == Location.NONE)
            return ("");
        
        StringBuilder   sb = new StringBuilder ();
        
        sb.append (startLine + 1);
        
        int     startPos = Location.getStartPosition (location);
        
        if (startPos != Location.NONE) {
            sb.append (".");
            sb.append (startPos + 1);
        }
                
        int     endLine = Location.getEndLine (location);
        int     endPos = Location.getEndPosition (location);
        
        if (endLine != startLine || endPos != startPos) {
            sb.append ("..");
            
            if (endLine != startLine) {
                sb.append (endLine + 1);                
                sb.append (".");
            }
            
            if (endPos != startPos || endLine != startLine)
                sb.append (endPos + 1);
        }
        
        sb.append (": ");
        return (sb.toString ());
    }    
}