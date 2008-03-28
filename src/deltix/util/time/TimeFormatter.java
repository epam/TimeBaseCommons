package deltix.util.time;

import java.util.*;
import java.text.*;

/**
 *  Thread-safe wrapper around SimpleDateFormat.
 */
public class TimeFormatter {
    public static final TimeFormatter   GMT_INSTANCE = 
        new TimeFormatter ();
    
    private DateFormat          mDF;
    
    public TimeFormatter () {
        this ("GMT");
    }
    
    public TimeFormatter (String tz) {
        this ("yyyy-MM-dd HH:mm:ss z", tz);
    }
    
    public TimeFormatter (String formatSpec, String tz) {
        this (formatSpec, TimeZone.getTimeZone (tz));
    }
    
    public TimeFormatter (String formatSpec, TimeZone tz) {
        mDF = new SimpleDateFormat (formatSpec);
        mDF.setTimeZone (tz);
    }

    public String                   format (long t) {
        return (format (new Date (t)));
    }
    
    public synchronized String      format (Date t) {
        return (mDF.format (t));
    }

    private final static long MILLIS_IN_DAY = 24*60*60*1000;

    private final static long MILLIS_GMT_OFFSET;
    static {
        Calendar c = Calendar.getInstance();
        MILLIS_GMT_OFFSET = c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET);
    }
    
    /** 
     * Fast method for printing current time of day in local time zone.
     * Thread safe.  
     */
    public static String formatTimeOfDay (long timeGMT) {


        int seconds = (int) (((timeGMT + MILLIS_GMT_OFFSET) % MILLIS_IN_DAY) / 1000);

        int     s = seconds % 60;
        int     m = (seconds / 60) % 60;
        int     h = seconds / 3600;

        //                               0    1    2    3    4    5    6    7
        char [] timebuf = new char [] { '0', '0', ':', '0', '0', ':', '0', '0' };

        // H low
        int foo = h % 10;
        if (foo > 0)
            timebuf [1] += foo;

        // H high
        foo = h / 10;
        if (foo > 0)
            timebuf [0] += foo;

        // M low
        foo = m % 10;
        if (foo > 0)
            timebuf [4] += foo;

        // M high
        foo = m / 10;
        if (foo > 0)
            timebuf [3] += foo;

        // S low
        foo = s % 10;
        if (foo > 0)
            timebuf [7] += foo;

        // S high
        foo = s  / 10;
        if (foo > 0)
            timebuf [6] += foo;


        return new String (timebuf);

    }
    
}
