package com.epam.deltix.timebase.messages;

import org.apache.commons.lang3.time.FastDateFormat;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

class TicksFormat {
    private static final int                NANOS_PER_MS = 1000000;
    private static final StringBuffer       sb = new StringBuffer();
    private static final Calendar           calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));

    private static final FastDateFormat     TICKS = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.", TimeZone.getTimeZone("GMT"));
    private static final FastDateFormat     MS = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS", TimeZone.getTimeZone("GMT"));
    private static final DecimalFormat      DF = new DecimalFormat("000000000");

    public static synchronized String         format(long milliseconds, int nanosComponent) {
        sb.setLength(0);
        calendar.setTimeInMillis(milliseconds);

        if (nanosComponent == 0 || milliseconds == Long.MIN_VALUE) {
            MS.format(calendar, sb);
        } else {
            TICKS.format(calendar, sb);

            long ms = milliseconds % 1000;
            DF.format(ms * NANOS_PER_MS + nanosComponent, sb, new FieldPosition(0));
        }

        return sb.toString();
    }

}
