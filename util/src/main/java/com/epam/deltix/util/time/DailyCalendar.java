package com.epam.deltix.util.time;

import com.epam.deltix.util.collections.generated.IntegerArrayList;
import com.epam.deltix.util.text.CharSequenceParser;
import java.util.Arrays;
import java.util.Calendar;

/**
 *
 */
public class DailyCalendar {  
    public static enum Adjust {
        NONE,
        FORWARD,
        BACKWARD
    }
    
    public static class EmptyCalendarException extends RuntimeException {
        EmptyCalendarException () {
            super ("Calendar is empty");            
        }
    }
    
    public static class DayOutOfCalendarRangeException extends RuntimeException {
        public final int        day;
        public final int        first;
        public final int        last;
        
        DayOutOfCalendarRangeException (int inDay, int inFirst, int inLast) {
            super (
                "Day " + formatDay (inDay) + " is outside of calendar range: " +
                formatDay (inFirst) + " .. " + formatDay (inLast)
            );
            
            day = inDay;
            first = inFirst;
            last = inLast;
        }
    }
    
    public static class DayNotInCalendarException extends RuntimeException {
        public final int        day;
        
        DayNotInCalendarException (int inDay) {
            super (
                "Day " + formatDay (inDay) + " is not in the calendar"
            );
            
            day = inDay;
        }
    }
    
    public static final int     MONDAY = 0;
    public static final int     TUESDAY = 1;
    public static final int     WEDNESDAY = 2;
    public static final int     THURSDAY = 3;
    public static final int     FRIDAY = 4;
    public static final int     SATURDAY = 5;
    public static final int     SUNDAY = 6;
    
    public static final int     WEEKDAY_MASK = 0x1F;
    public static final int     WEEKEND_MASK = 0x60;    
    
    public static String    formatDay (int day) {
        if (day == Integer.MIN_VALUE)
            return ("<N/A>");
        
        return (GMT.formatDate (dayNumberToGMT (day)));
    }
    
    /**
     *  Converts a ms timestamp to the number of days (using GMT boundary)
     *  since 1/1/1970.
     * 
     *  @param gmt  The timestamp to convert.
     *  @return     The number of days since 1/1/1970 GMT
     */
    public static int       gmtToDayNumber (long gmt) {
        if (gmt >= 0)
            return ((int) (gmt / 86400000L));
        else
            return ((int) ((gmt - 86399999L) / 86400000L));
    }
    
    /**
     *  Returns current day number.
     * 
     *  @return     The number of days since 1/1/1970 GMT
     */
    public static int       currentDayNumber () {
        return (gmtToDayNumber (System.currentTimeMillis ()));
    }
    
    /**
     *  Converts a ms timestamp to the number of days (using GMT boundary)
     *  since 1/1/1970.
     * 
     *  @param day  The number of days since 1/1/1970 GMT
     *  @return     The time at 00:00:00.000 on this day (in GMT).
     */
    public static long      dayNumberToGMT (int day) {
        return (day * 86400000L);
    }
    
    public static int       ANSIDateToDayNumber (String dateText) {
        return (ANSIDateToDayNumber (GMT.getCalendarInstance0 (), dateText));
    }
    
    public static int       ANSIDateToDayNumber (Calendar cal, String dateText) {
        int         year = CharSequenceParser.parseInt (dateText, 0, 4);
        int         month = CharSequenceParser.parseInt (dateText, 5, 7);
        int         day = CharSequenceParser.parseInt (dateText, 8, 10);
            
        cal.set (year, month - 1, day);
        
        return (DailyCalendar.gmtToDayNumber (cal.getTimeInMillis ()));
    }
    
    public static String    dayNumberToANSIDate (int date) {
        return (dayNumberToANSIDate (GMT.getCalendarInstance0 (), date));
    }
    
    public static String    dayNumberToANSIDate (Calendar cal, int date) {
        cal.setTimeInMillis (DailyCalendar.dayNumberToGMT (date));
        
        return (
            String.format (
                "%04d-%02d-%02d", 
                cal.get (Calendar.YEAR),
                cal.get (Calendar.MONTH) + 1,
                cal.get (Calendar.DAY_OF_MONTH)
            )
        );
    }
        
    /**
     *  Converts a day number to day of week. 
     * 
     *  @param dayNumber    The number of days since 1/1/1970 GMT
     *  @return             Week day number. Monday is 0, Sunday is 6.
     */
    public static int       dayOfWeek (int dayNumber) {
        //  day 0 was a Thursday; shift to compensate   
        int     mod7 = (dayNumber + THURSDAY) % 7;
        
        if (mod7 < 0)
            mod7 += 7;
        
        return (mod7);
    }
    
    public static boolean   isWeekend (int day) {
        switch (dayOfWeek (day)) {
            case SATURDAY:
            case SUNDAY:
                return (true);
                
            default:
                return (false);
        }
    }
    
    private IntegerArrayList        mDays = new IntegerArrayList ();
    
    public DailyCalendar () {
    }
    
    private int                     find (int day) {
        final int         size = mDays.size ();
        final int []      data = mDays.getInternalBuffer ();
        
        //  A few very quick checks before we commit to Arrays.binarySearch
        if (size == 0)
            return (-1);
                
        if (day < data [0])
            return (-1);
        
        if (day > data [size - 1])
            return (-size - 1);
        
        return (Arrays.binarySearch (data, 0, size, day));
    }
    
    private void                    checkDay (int day) {
        int         num = mDays.size ();
        
        if (num == 0)
            throw new EmptyCalendarException ();
                
        int         first = mDays.get (0);
        int         last = mDays.get (num - 1);
       
        if (day < first || day > last)
            throw new DayOutOfCalendarRangeException (day, first, last);
    }
    
    public int                      getIndex (int day) {
        return (getIndex (day, Adjust.NONE));
    }
    
    public boolean                  dayInRange (int day) {
        return (day >= mDays.get (0) && day <= mDays.get (mDays.size () - 1));
    }

    public int                      getIndex (int day, Adjust adj) {
        checkDay (day);
        
        int     idx = find (day);
        
        if (idx < 0) {
            switch (adj) {
                case NONE:
                    throw new DayNotInCalendarException (day);
                    
                case FORWARD:
                    return (-idx - 1);
                    
                case BACKWARD:
                    return (-idx - 2);
            }
        }
        
        return (idx);
    }
    
    public int                      getDayByIndex (int idx) {
        return (mDays.get (idx));
    }
    
    public int                      getDistance (
        int                             from, 
        Adjust                          fromAdj, 
        int                             to,
        Adjust                          toAdj
    )
    {
        return (getIndex (to, fromAdj) - getIndex (from, toAdj));
    }
    
    public int                      getDistance (
        int                             from, 
        int                             to
    )
    {
        return (getIndex (to, Adjust.FORWARD) - getIndex (from, Adjust.FORWARD));
    }
    
    public int                      shift (int day, int offset) {
        return (getDayByIndex (getIndex (day) + offset));
    }
    
    public int                      shift (int day, Adjust adj, int offset) {
        return (getDayByIndex (getIndex (day, adj) + offset));
    }
    
    public boolean                  add (int day) {
        int         idx = find (day);
        
        if (idx < 0) {
            mDays.add (-idx - 1, day);
            return (true);
        }
        else
            return (false);
    }
    
    public boolean                  clear (int day) {
        int         idx = find (day);
        
        if (idx >= 0) {
            mDays.remove (idx);
            return (true);
        }
        else
            return (false); 
    }
    
    private int                     countBits (int mask) {
        int     count = 0;
        
        for (int ii = MONDAY; ii <= SUNDAY; ii++)
            if ((mask & (1 << ii)) != 0)
                count++;
        
        return (count);
    }
    
    public void                     reset (int from, int to) {       
        reset (from, to, WEEKDAY_MASK);
    }
    
    public void                     reset (int from, int to, int mask) {        
        mDays.ensureCapacity ((from + 6 - to) / 7 * countBits (mask));
        
        int     bit = 1 << dayOfWeek (from);
       
        for (int ii = from; ii < to; ii++) {
            if ((mask & bit) != 0)
                mDays.add (ii);
            
            bit <<= 1;
            
            if (bit == 0x80)
                bit = 0x01;
        }
    }
    
    @Override
    public String                   toString () {
        StringBuilder       sb = new StringBuilder ();
        int                 num = mDays.size ();
        
        for (int ii = 0; ii < num; ii++) {
            if (ii > 0)
                sb.append (",");
            
            sb.append (mDays.get (ii));            
        }
            
        return (sb.toString ());
    }
    
    public static void              main (String [] args) {
        DailyCalendar   dc = new DailyCalendar ();
        
        dc.reset (0, 100);
        
        System.out.println (dc);
    }
}
