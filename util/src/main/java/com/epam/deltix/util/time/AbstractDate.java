package com.epam.deltix.util.time;

/**
 *  A simple record containing a year, a month and a day of month.
 */
public interface AbstractDate {
    /**
     *  The year
     */
    public int          getYear ();
    
    /**
     *  The month, 1 - 12
     */
    public int          getMonth ();
    
    /**
     *  The day of month, 1 - 31
     */
    public int          getDay ();    
}
