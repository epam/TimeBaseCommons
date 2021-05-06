package com.epam.deltix.util.time;

import java.text.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class Test_TimeZoneUtils {
    public static final TimeZone    NYTZ = TimeZone.getTimeZone ("America/New_York");
    public static final DateFormat  DF = new SimpleDateFormat ("yyyy-MM-dd HH:mm");

    static {
        DF.setTimeZone (NYTZ);
    }
    
    /**
     *  Check TimeZoneUtils.getMidnightOn against DateFormat/Calendar
     */
    private void            testGetMidnightOn (long midnight, int offset) {
        long        actual = TimeZoneUtils.getMidnightOn (NYTZ, midnight + offset);
        assertEquals (midnight, actual);
    }

    @Test
    public void             testGetMidnightOn () throws ParseException {
        long                    start = DF.parse ("2009-05-19 00:00").getTime ();

        testGetMidnightOn (start, 0);
        testGetMidnightOn (start, 1);

        for (int h = 1; h < 23; h++) {
            testGetMidnightOn (start, h * 3600000 - 1);
            testGetMidnightOn (start, h * 3600000);
            testGetMidnightOn (start, h * 3600000 + 1);
        }
            
        testGetMidnightOn (start, 85399);
    }
}
