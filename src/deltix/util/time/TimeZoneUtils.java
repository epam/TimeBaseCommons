package deltix.util.time;

import java.util.TimeZone;

/**
 *
 */
public abstract class TimeZoneUtils {
    public static long                  getMidnightOn (TimeZone tz, final long absTime) {
        return (absTime - tz.getOffset (absTime) - absTime % 86400000);
    }
}
