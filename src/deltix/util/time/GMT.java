package deltix.util.time;

import java.text.*;
import java.util.*;

/**
 *
 */
public abstract class GMT {	
	public static final String DATE_FORMAT_STR = "yyyy-MM-dd";
	
	public static final TimeZone                TZ = TimeZone.getTimeZone ("GMT");
    private static final SimpleDateFormat       DTFX = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss.S");
    private static final SimpleDateFormat       DTF = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat       DF = new SimpleDateFormat (DATE_FORMAT_STR);
    private static final SimpleDateFormat       TF = new SimpleDateFormat ("HH:mm:ss");
    
    static {
        DTF.setTimeZone (TZ);
        DF.setTimeZone (TZ);
        DTFX.setTimeZone (TZ);
        TF.setTimeZone (TZ);
    }
    
    public static Calendar              getCalendarInstance () {
        return (Calendar.getInstance (TZ));
    }
    
    public static String                formatDateTime (long t) {
        synchronized (DTF) {
            return (DTF.format (new Date (t)));
        }
    }
    
    public static String                formatDateTimeMillis (long t) {
        synchronized (DTFX) {
            return (DTFX.format (new Date (t)));
        }
    }
    
    public static String                formatDate (long t) {
        return formatDate (new Date (t));
    }

    public static String                formatDate (Date d) {
        synchronized (DF) {
            return (DF.format (d));
        }
    }
    
    public static Date                  parseDate (String date) 
        throws ParseException
    {
        synchronized (DF) {
            return (DF.parse(date));
        }
    }
    
    public static Date                  parseDateTime (String date) 
        throws ParseException
    {
        synchronized (DTF) {
            return (DTF.parse(date));
        }
    }
    
    public static Date                  parseDateTimeMillis (String date) 
        throws ParseException
    {
        synchronized (DTFX) {
            return (DTFX.parse(date));
        }
    }
    
    public static String                formatTime (long t) {
        synchronized (TF) {
            return (TF.format (new Date (t)));
        }
    }
    
    public static Date 					getTomorrow() {
        Calendar calendar = getCalendarInstance();
        // clear time fields
        for (int i = Calendar.HOUR_OF_DAY; i <= Calendar.MILLISECOND; i++) {
            calendar.set(i, 0);
        }
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }
    
}
