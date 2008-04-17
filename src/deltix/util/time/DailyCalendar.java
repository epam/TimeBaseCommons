package deltix.util.time;

import deltix.util.collections.generated.IntegerArrayList;
import java.util.Arrays;

/**
 *
 */
public class DailyCalendar {
    public static final int     MONDAY = 0;
    public static final int     TUESDAY = 1;
    public static final int     WEDNESDAY = 2;
    public static final int     THURSDAY = 3;
    public static final int     FRIDAY = 4;
    public static final int     SATURDAY = 5;
    public static final int     SUNDAY = 6;
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
     *  Converts a ms timestamp to the number of days (using GMT boundary)
     *  since 1/1/1970.
     * 
     *  @param day  The number of days since 1/1/1970 GMT
     *  @return     The time at 00:00:00.000 on this day (in GMT).
     */
    public static long      dayNumberToGMT (int day) {
        return (day * 86400000L);
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
    
    /**
     *  Return the number of weekdays between two specified dates.
     * 
     *  @param from     From date as day number, inclusive.
     *  @param to       To date as day number, exclusive.
     *  @return         Number of weekdays between from and to.
     */
    public static int       weekdaysBetween (int from, int to) {
        assert to >= from : 
            "Illegal ordering: from = " + from + "; to = " + to;
        
        /*
         *  Adjust weekend dates to next Monday
         */
        int         fromDoW = dayOfWeek (from);
        int         toDoW = dayOfWeek (to);
        
        switch (fromDoW) {
            case SATURDAY:  
                from += 2;
                fromDoW = MONDAY;
                break;
                
            case SUNDAY:    
                from++;
                fromDoW = MONDAY;
                break;
        }
        
        switch (toDoW) {
            case SATURDAY:  
                to += 2;
                toDoW = MONDAY;
                break;
                
            case SUNDAY:    
                to++;
                toDoW = MONDAY;
                break;
        }
        
        int         fromWeeksMonday = from - fromDoW;
        int         toWeeksMonday = to - toDoW;
        
        //  If same week, just subtract
        if (fromWeeksMonday == toWeeksMonday)
            return (to - from);
        
        /*
         *  We have established that there is at least one weekend between
         *  from and to.
         */
        return (
            SATURDAY - fromDoW +                // weekdays included in from week
            toDoW +                             // weekdays included in to week
            ((toWeeksMonday - fromWeeksMonday) / 7 - 1) * 5
        );
    }
    
    /**
     *  Return the number of holidays between two specified dates, according
     *  to the supplied list.
     * 
     *  @param fromDay  From date as day number, inclusive.
     *  @param toDay    To date as day number, exclusive.
     *  @param holidays A sorted array of holidays as day numbers, which 
     *                  must not contain any weekend days.
     *  @param fromIdx  The inclusive start offset in the holidays array.
     *  @param toIdx    The exclusive end offset in the holidays array.
     *  @return         Number of holidays between from and to.
     */
    public static int       holidaysBetween (
        int                     fromDay, 
        int                     toDay, 
        int []                  holidays, 
        int                     fromIdx,
        int                     toIdx
    )
    {
        int         fromPos = Arrays.binarySearch (holidays, fromIdx, toIdx, fromDay);
        
        if (fromPos < 0)
            fromPos = -fromPos - 1;
        
        int         toPos = Arrays.binarySearch (holidays, fromIdx, toIdx, toDay - 1);
        
        if (toPos < 0)
            toPos = -toPos - 1;
        
        return (toPos - fromPos);
    }   
    
    private IntegerArrayList        mHolidays = null;
    private final boolean           mWeekendsAreHolidays;
    
    public DailyCalendar (boolean weekendsAreHolidays) {
        mWeekendsAreHolidays = weekendsAreHolidays;
    }
    
    public boolean          addHoliday (int day) {
        if (mHolidays == null)
            mHolidays = new IntegerArrayList (1000);
        
        if (mWeekendsAreHolidays && isWeekend (day))
            return (false);
        
        int         fromPos = 
            Arrays.binarySearch (
                mHolidays.getInternalBuffer (),
                0,
                mHolidays.size (),
                day
            );

        if (fromPos >= 0)
            return (false);
        
        mHolidays.add (-fromPos - 1, day);
        return (true);
    }
    
    public int              nonHolidaysBetween (
        int                     fromDay, 
        int                     toDay
    )
    {
        int         n;
        
        if (mWeekendsAreHolidays)
            n = weekdaysBetween (fromDay, toDay);
        else
            n = toDay - fromDay;
        
        if (mHolidays != null)
            n -= 
                holidaysBetween (
                    fromDay, 
                    toDay, 
                    mHolidays.getInternalBuffer (),
                    0,
                    mHolidays.size ()
                );
        
        return (n);
    }
}
