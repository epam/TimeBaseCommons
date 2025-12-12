package com.epam.deltix.qsrv.hf.pub;

public interface TimeInterval {

    /*
        Returns start time is measured in nanoseconds that passed since January 1, 1970 UTC
     */
    public long         getFromTime();

    /*
        Returns end time is measured in nanoseconds that passed since January 1, 1970 UTC
     */
    public long         getToTime();

    /*
        Returns true, if interval is undefined.
     */
    public boolean      isUndefined();
}
