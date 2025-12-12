package com.epam.deltix.util.time;

public abstract class TimeConstants {
    public static final int     SECOND = 1000;
    public static final int     MINUTE = 60 * SECOND;
    public static final int     HOUR = 60 * MINUTE;
    public static final int     HALF_DAY = MINUTE * 60 * 12;
    public static final int     DAY = MINUTE * 60 * 24;

    public static final long    TIMESTAMP_UNKNOWN = Long.MIN_VALUE;
}
