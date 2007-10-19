package deltix.util.time;

import java.text.*;
import java.util.*;

/**
 *
 */
public abstract class GMT {
    public static final TimeZone                TZ = TimeZone.getTimeZone ("GMT");
    private static final SimpleDateFormat       DTF = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat       DF = new SimpleDateFormat ("yyyy-MM-dd");
    private static final SimpleDateFormat       TF = new SimpleDateFormat ("HH:mm:ss");
    
    static {
        DTF.setTimeZone (TZ);
        DF.setTimeZone (TZ);
    }
    
    public static Calendar              getCalendarInstance () {
        return (Calendar.getInstance (TZ));
    }
    
    public static String                formatDateTime (long t) {
        synchronized (DTF) {
            return (DTF.format (new Date (t)));
        }
    }
    
    public static String                formatDate (long t) {
        synchronized (DF) {
            return (DF.format (new Date (t)));
        }
    }
    
    public static String                formatTime (long t) {
        synchronized (DF) {
            return (TF.format (new Date (t)));
        }
    }
}
