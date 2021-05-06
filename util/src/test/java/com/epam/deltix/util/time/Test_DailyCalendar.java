package com.epam.deltix.util.time;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 *<h3>Day Numbers (true)</h3>
 *<pre>
 *Mon Tue Wed Thu Fri Sat Sun
 *-3  -2  -1   0   1@  2   3
 * 4   5   6   7   8   9  10
 *11@ 12@ 13  14  15  16  17
 *18  19  20  21  22  23  24
 *</pre>
 * 
 *<h3>Test Holidays (fake)</h3>
 * 1,11,12
 */
public class Test_DailyCalendar {
    private DailyCalendar       mCal;
    
    @Before
    public void     setUp () {
        mCal = new DailyCalendar ();
        mCal.reset (-3, 25);
        
        mCal.clear (1);
        mCal.clear (11);
        mCal.clear (12);
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
    public void     testGetDistance () {
        assertEquals (0, mCal.getDistance (0, 0));
        assertEquals (1, mCal.getDistance (0, 4));   // 1 is off
        // test outside of calendar
        assertEquals (3, mCal.getDistance (15, 20));
        assertEquals (1, mCal.getDistance (-2, -1));
        // test all of calendar
        assertEquals (7, mCal.getDistance (0, 14));
    }
    
    @Test
    public void     testShift () {
        assertEquals (13, mCal.shift (-2, 8));
        assertEquals (4, mCal.shift (0, 1));
        //  No gaps
        assertEquals (15, mCal.shift (13, 2));
    }    
}
