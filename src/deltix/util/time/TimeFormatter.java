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
}
