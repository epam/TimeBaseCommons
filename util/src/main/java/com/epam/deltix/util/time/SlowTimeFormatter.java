package com.epam.deltix.util.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SlowTimeFormatter {
    public static final SlowTimeFormatter   GMT_INSTANCE =
        new SlowTimeFormatter ();

    private DateFormat          mDF;

    public SlowTimeFormatter () {
        this ("GMT");
    }

    public SlowTimeFormatter (String tz) {
        this ("yyyy-MM-dd HH:mm:ss z", tz);
    }

    public SlowTimeFormatter (String formatSpec, String tz) {
        this (formatSpec, TimeZone.getTimeZone (tz));
    }

    public SlowTimeFormatter (String formatSpec, TimeZone tz) {
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
