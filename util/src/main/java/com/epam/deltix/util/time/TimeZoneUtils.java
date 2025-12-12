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
