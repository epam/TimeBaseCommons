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

/**
 * For durations less than 24h use TimeFormatter
 */
@Deprecated
public class DurationFormat {
    private static void        f2 (StringBuffer sb, int value) {
        if (value < 10)
            sb.append ("0");
        
        sb.append (value);
    }
    
    public static String       format (double seconds) {
        int         isec = (int) seconds;
        
        int         secComp = isec % 60;
        
        isec /= 60;
        
        int         minComp = isec % 60;
        
        isec /= 60;
        
        int         hourComp = isec;
        
        StringBuffer    sb = new StringBuffer ();
        
        sb.append (hourComp);
        sb.append (":");
        f2 (sb, minComp);
        sb.append (":");
        f2 (sb, secComp);
        
        return (sb.toString ());
    }
}