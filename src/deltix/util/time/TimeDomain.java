package deltix.util.time;

import java.util.TimeZone;

/**
 *
 */
public interface TimeDomain {
    public static final long    TIME_OUT_OF_RANGE = Long.MIN_VALUE;
    
    public TimeZone     getTimeZone ();

    public void         setTimeZone (TimeZone tz);
    
    public long         transform (long t, boolean snapOutOfRange);
    
    public long         reverse (long t);
}
