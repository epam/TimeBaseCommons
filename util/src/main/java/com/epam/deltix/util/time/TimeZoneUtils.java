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

import java.util.TimeZone;

/**
 *
 */
public abstract class TimeZoneUtils {
    public static final long            D = 86400000;

    public static int                   getTimeOfDay (TimeZone tz, final long absTime) {
        return ((int) ((absTime + tz.getOffset (absTime)) % D));
    }

    public static long                  getMidnightOn (TimeZone tz, final long absTime) {
        /**
         *  Midnights G in GMT must satisfy     G = D * n
         *      where n is an integer, D = 86400000
         *
         *  Local midnights M must satisfy      M = G - o = D * n - o
         *      where n is an integer, D = 86400000 and o is the TZ offset
         *
         *  Find Mmax = max (M) such that Mmax < absTime
         *
         *  n = (absTime + o) / D
         * 
         *  Mmax =
         *      D * n - o =
         *      (absTime + o) / D * D - o =
         *      (absTime + o) - (absTime + o) % D - o =
         *      absTime - (absTime + o) % D
         */
        long        o = tz.getOffset (absTime);
        
        return (absTime - (absTime + o) % D);
    }
}