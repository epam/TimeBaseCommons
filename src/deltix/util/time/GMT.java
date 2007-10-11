package deltix.util.time;

import java.util.*;

/**
 *
 */
public abstract class GMT {
    public static final TimeZone        TZ = TimeZone.getTimeZone ("GMT");
    
    public static Calendar              getCalendarInstance () {
        return (Calendar.getInstance (TZ));
    }
}
