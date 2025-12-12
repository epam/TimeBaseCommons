package com.epam.deltix.util.time;

import java.util.*;

/**
 *  Computes time arithmetic. Methods of this class may use internal 
 *  buffers and, therefore, must by externally protected from concurrent calls.
 */
public class IntervalCalculator {
    private static Calendar        mCalendar = GMT.getCalendarInstance ();
    
    public static long             add (long time, Interval interval) {
        if (interval instanceof FixedInterval) 
            return (time + ((FixedInterval) interval).getSizeInMilliseconds ());
        else {
            mCalendar.setTimeInMillis (time + 1);
            mCalendar.add (Calendar.MONTH, ((MonthlyInterval) interval).getNumberOfMonths ());
            return (mCalendar.getTimeInMillis () - 1);
        }
    }
    
    public static long             subtract (long time, Interval interval) {
        if (interval instanceof FixedInterval) 
            return (time - ((FixedInterval) interval).getSizeInMilliseconds ());
        else {
            mCalendar.setTimeInMillis (time + 1);
            mCalendar.add (Calendar.MONTH, -((MonthlyInterval) interval).getNumberOfMonths ());
            return (mCalendar.getTimeInMillis () - 1);
        }
    }
    
    /**
     *  Normalizes a time by snapping it to the last millisecond of the 
     *  specified period in GMT. For instance, if period == Calendar.DAY,
     *  then any time within March, 12 2007 GMT
     *  will be snapped to the last millisecond of that day.
     * 
     *  @param time          The date/time to normalize
     *  @param unit          The lowest degree of granularity of the period,
     *                          for example, months, days, or minutes. 
     */
    public static long             normalize (long time, TimeUnit unit) {
        switch (unit) {
            case MILLISECOND:   
                return (time);
                
            case SECOND:
                return (time - time % 1000L + 999L);
                
            case MINUTE:
                return (time - time % 60000L + 59999L);
                
            case HOUR:
                return (time - time % 3600000L + 3599999L);
                
            case DAY:
                return (time - time % 86400000L + 86399999L);
            
            case WEEK:                       
                return (time - (time + 259200000L) % 604800000L + 604799999L);
        }
        
        /*
         *  Truncate time of day to 0 
         */
        time -= time % 86400000L;                
        mCalendar.setTimeInMillis (time);
                        
        switch (unit) {
            case MONTH:
                mCalendar.set (Calendar.DAY_OF_MONTH, 1);
                mCalendar.add (Calendar.MONTH, 1);
                return (mCalendar.getTimeInMillis () - 1);
                
            case QUARTER:
                int         month = mCalendar.get (Calendar.MONTH);
                mCalendar.set (Calendar.MONTH, month - month % 3 + 3);
                mCalendar.set (Calendar.DAY_OF_MONTH, 1);
                return (mCalendar.getTimeInMillis () - 1);
                
            case YEAR:
                mCalendar.set (Calendar.MONTH, 11); // 0-based December!!!
                mCalendar.set (Calendar.DAY_OF_MONTH, 31);
                return (mCalendar.getTimeInMillis () + 86399999L);
                                
            default:            
                throw new RuntimeException (unit.name ());
        }                
    }
    
    /**
     *  Builds a normalized time stamp using a GMT calendar.
     * 
     *  @param year         The year (mandatory)
     *  @param month        The month of year, mandatory. Note: this is 1-based,
     *                          unlike the confusing Calendar convention.
     *  @param day          1-based day, if unit is smaller than month.
     *  @param unit         The granularity of the period,
     *                          for example, months, days, or minutes. 
     */
    public static long             getNormalizedTime (
        TimeUnit                unit,
        int                     year, 
        int                     month, 
        int                     day,
        int                     hour,
        int                     minute,
        int                     second,
        int                     millisecond        
    )
    {
        /*
         *  Assert argument ranges
         */
        switch (unit) {
            case MILLISECOND:
                assert millisecond >= 0 && millisecond <= 999 :
                    "Illegal millisecond: " + millisecond;
                
            case SECOND:
                assert second >= 0 && second <= 59 :
                    "Illegal second: " + second;
                
            case MINUTE:
                assert minute >= 0 && minute <= 59 :
                    "Illegal minute: " + minute;
                
            case HOUR:
                assert hour >= 0 && hour <= 23 :
                    "Illegal hour: " + hour;
                
            case DAY:
            case WEEK:                    
                assert day >= 1 && day <= 31 :
                    "Illegal day: " + day;
            
            case MONTH:
                assert month >= 1 && month <= 12 : 
                    "Illegal month: " + month;                        
        }
        
        //  Set ignored values
        switch (unit) {
            case YEAR:
                month = 11; // 0-based December!!!
                day = 31;
                hour = 23;
                minute = second = 59;
                millisecond = 999;
                break;
                
            case QUARTER:   // Set to beginning of next quarter, correction required
                month--;    // Translate to 0-based for java.util.Calendar
                month = month - month % 3 + 3;
                day = 1;
                hour = minute = second = millisecond = 0;
                break;
                
            case MONTH:     // Set to beginning of next month, correction required
                // month = month - 1 + 1 :)
                day = 1;  
                hour = minute = second = millisecond = 0;
                break;
                
            default:
                month--;    // Translate to 0-based for java.util.Calendar
                break;
        }
        
        switch (unit) {
            case WEEK:          // Leave day alone, post-processing required!
            case DAY:
                hour = 23; 
                
            case HOUR:
                minute = 59;
                
            case MINUTE:
                second = 59;
                
            case SECOND:
                millisecond = 999;
                
            case MILLISECOND:
                break;
        }         
        
        mCalendar.set (year, month, day, hour, minute, second);
        mCalendar.set (Calendar.MILLISECOND, millisecond);
        
        switch (unit) {
            case QUARTER:
            case MONTH:
                return (mCalendar.getTimeInMillis () - 1);
                
            case WEEK:  // Normalize to end of Sunday
                long        time = mCalendar.getTimeInMillis ();
                return (time - (time + 259200000L) % 604800000L + 604799999L);
                
            default:
                return (mCalendar.getTimeInMillis ());
        }                
    }
    
    public static void main (String [] args) throws Exception {
        String              cmd = args [0];
        //IntervalCalculator  ic = new IntervalCalculator ();
        
        if (cmd.equalsIgnoreCase ("n")) {
            Date                dt = GMT.parseDateTimeMillis (args [1]);
            TimeUnit            unit = TimeUnit.valueOf (args [2]);
            
            long                t = dt.getTime ();

            System.out.println (
                GMT.formatDateTimeMillis (t) + " normalized on " + unit +
                " =\n" + GMT.formatDateTimeMillis (IntervalCalculator.normalize (t, unit)) );
        }
        else if (cmd.equalsIgnoreCase ("t")) {
            TimeUnit            unit = TimeUnit.valueOf (args [1]);
            int                 y = Integer.parseInt (args [2]);
            int                 m = 
                args.length > 3 ? Integer.parseInt (args [3]) : -1;
            int                 d =
                args.length > 4 ? Integer.parseInt (args [4]) : -1;
            int                 h =
                args.length > 5 ? Integer.parseInt (args [5]) : -1;
            int                 mi =
                args.length > 6 ? Integer.parseInt (args [6]) : -1;
            int                 s =
                args.length > 7 ? Integer.parseInt (args [7]) : -1;
            int                 x =
                args.length > 8 ? Integer.parseInt (args [8]) : -1;
            
            System.out.println (
                GMT.formatDateTimeMillis (
                    IntervalCalculator.getNormalizedTime (unit, y, m, d, h, mi, s, x)
                )
            );
        }
        else if (cmd.equals ("+")) {
            long            t = GMT.parseDateTimeMillis (args [1]).getTime ();
            Interval        interval = Interval.valueOf (args [2]);
            
            t = IntervalCalculator.normalize (t, interval.getUnit ());
            
            System.out.println (
                GMT.formatDateTimeMillis (t) + " + " + interval.toString () +
                " = \n" +
                GMT.formatDateTimeMillis (IntervalCalculator.add (t, interval))
            );
        }
        else if (cmd.equals ("-")) {
            long            t = GMT.parseDateTimeMillis (args [1]).getTime ();
            Interval        interval = Interval.valueOf (args [2]);
            
            t = IntervalCalculator.normalize (t, interval.getUnit ());
            
            System.out.println (
                GMT.formatDateTimeMillis (t) + " - " + interval.toString () +
                " = \n" +
                GMT.formatDateTimeMillis (IntervalCalculator.subtract (t, interval))
            );
        }
        else
            throw new IllegalArgumentException (cmd);
    }
    
    
}
