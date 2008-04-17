package deltix.util.time;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * <h3>Day Numbers</h3>
 *<pre>
 *Mon Tue Wed Thu Fri Sat Sun
 *-3  -2  -1   0   1@  2   3
 * 4   5   6   7   8   9  10
 *11@ 12@ 13  14  15  16  17
 *18  19  20  21  22  23  24
 *</pre>
 * 
 *<h3>Test Holidays</h3>
 * 1,11,12
 */ 
public class Test_DailyCalendar {
    private DailyCalendar       mCal = new DailyCalendar (true);
    
    @Before
    public void     setUp () {
        mCal.addHoliday (1);
        mCal.addHoliday (11);
        mCal.addHoliday (12);
    }
    
    @Test
    public void     testHolidays () {
        assertEquals (0, mCal.nonHolidaysBetween (0, 0));
        assertEquals (1, mCal.nonHolidaysBetween (0, 4));   // 1 is off
        // test outside of calendar
        assertEquals (3, mCal.nonHolidaysBetween (15, 20));
        assertEquals (1, mCal.nonHolidaysBetween (-2, -1));
        // test all of calendar
        assertEquals (7, mCal.nonHolidaysBetween (0, 14));
    }
    
    @Test
    public void     testDayNumbers () {
        assertEquals (0, DailyCalendar.gmtToDayNumber (0));
        assertEquals (0, DailyCalendar.gmtToDayNumber (1));
        assertEquals (0, DailyCalendar.gmtToDayNumber (86399000));
        assertEquals (1, DailyCalendar.gmtToDayNumber (86400000));
        assertEquals (-1, DailyCalendar.gmtToDayNumber (-1));
        assertEquals (-1, DailyCalendar.gmtToDayNumber (-86399000));
        assertEquals (-1, DailyCalendar.gmtToDayNumber (-86400000));
        assertEquals (-2, DailyCalendar.gmtToDayNumber (-86400001));
        
        assertEquals (0L, DailyCalendar.dayNumberToGMT (0));
        assertEquals (86400000L, DailyCalendar.dayNumberToGMT (1));
        assertEquals (-86400000L, DailyCalendar.dayNumberToGMT (-1));
            
        for (int ii = -200; ii < 200; ii++)
            assertEquals (
                ii, 
                DailyCalendar.gmtToDayNumber (DailyCalendar.dayNumberToGMT (ii))
            );
    }
    
    @Test
    public void     testWeekdaysBetween () {        
        //  same weekend
        assertEquals (0, DailyCalendar.weekdaysBetween (2, 3));
        //  exclude Monday
        assertEquals (0, DailyCalendar.weekdaysBetween (2, 4));
        //  same day
        assertEquals (0, DailyCalendar.weekdaysBetween (13, 13));
        //  one day
        assertEquals (1, DailyCalendar.weekdaysBetween (13, 14));
        //  whole week
        assertEquals (5, DailyCalendar.weekdaysBetween (11, 16));
        //  whole week over weekend
        assertEquals (5, DailyCalendar.weekdaysBetween (11, 18));
        //  one day over weekend
        assertEquals (1, DailyCalendar.weekdaysBetween (8, 11));
    }
}
