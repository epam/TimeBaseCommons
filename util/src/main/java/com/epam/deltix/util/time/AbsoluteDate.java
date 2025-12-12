package com.epam.deltix.util.time;

import java.util.*;
import java.io.*;

/**
 *  A bean holding an absolute (time- and time zone-unrelated) date.
 */
public class AbsoluteDate implements AbstractDate, Comparable <AbstractDate> {
    public static final AbsoluteDate        MIN_VALUE = new AbsoluteDate (0, 1, 1);
    public static final AbsoluteDate        MAX_VALUE = new AbsoluteDate (Short.MAX_VALUE, 12, 31);
    
    static final long           serialVersionUID = 1L;
    private static final short  EXTERNAL_VERSION = 1;

    private short           mYear;
    private byte            mMonth;     //  1-based!
    private byte            mDay;       //  1-based!
    
    public AbsoluteDate () {
        mMonth = 0;
    }
    
    public AbsoluteDate (AbsoluteDate copy) {
        set (copy);
    }
    
    public AbsoluteDate (int year, int month, int day) {
        set (year, month, day);
    }
    
    public AbsoluteDate (Calendar cal) {
        set (cal);
    }
    
    public AbsoluteDate (int num) {
        set (num);           
    }
    
    public AbsoluteDate (String s) {
        set (s);
    }
        
    /**
     *  Returns whether the year is a leap year according to the Gregorian calendar
     */
    public static boolean   isLeapYear (int year) {
        if (year % 400 == 0)
            return (true);
        
        if (year % 100 == 0)
            return (false);
        
        return (year % 4 == 0);
    }
    
    private static final int[]  DAYS_IN_MONTH = { 0, 31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    
    public int              getNumberOfDaysInMonth () {
        if (mMonth == 2)
            return (isLeapYear (mYear) ? 29 : 28);
        else
            return (DAYS_IN_MONTH [mMonth]);
    }
    
    /**
     *  Increments the date by 1 day, according to the Gregorian calendar. 
     *  @return     this
     */
    public AbsoluteDate inc () {
        mDay++;
        
        if (mDay > getNumberOfDaysInMonth ()) {
            mDay = 1;
            mMonth++;
        }
        
        if (mMonth == 13) {
            mMonth = 1;
            mYear++;
        }
        
        return (this);
    }
    
    public boolean      isInitialized () {
        return (mMonth != 0);
    }
    
    public void         setUninitialized () {
        mMonth = 0;
    }
    
    public void         set (AbsoluteDate copy) {
        mYear = copy.mYear;
        mMonth = copy.mMonth;
        mDay = copy.mDay;
    }
    
    public void         set (int year, int month, int day) {
        if (year < 0 || year > Short.MAX_VALUE)
            throw new IllegalArgumentException ("Illegal year value: " + year);

        if (month < 1 || month > 12)
            throw new IllegalArgumentException ("Illegal month value: " + month);

        if (day < 1 || day > 31)
            throw new IllegalArgumentException ("Illegal day value: " + day);

        mYear = (short) year;
        mMonth = (byte) month;
        mDay = (byte) day;
    }
    
    public void         set (Calendar cal) {
        mYear = (short) cal.get (Calendar.YEAR);
        mMonth = (byte) (1 + cal.get (Calendar.MONTH));
        mDay = (byte) cal.get (Calendar.DAY_OF_MONTH);
    }
    
    public void         set (int num) {
        mDay = (byte) (num % 100);
        
        num = num / 100;
        
        mMonth = (byte) (num % 100);
        
        mYear = (short) (num / 100);                
    }
    
    public void         set (String s) {
        StringTokenizer     stk = new StringTokenizer (s, "-");
        
        if (stk.countTokens () == 3) {
            set (
                Short.parseShort (stk.nextToken ()),
                Byte.parseByte (stk.nextToken ()),
                Byte.parseByte (stk.nextToken ())
            );
        }
        else
            throw new NumberFormatException ("Illegal AbsoluteDate: " + s);
    }
    
    public String       toString () {
        return (mYear + "-" + mMonth + "-" + mDay);
    }
    
    public int          toInt () {
        return (mYear * 10000 + mMonth * 100 + mDay);
    }
    
    public int          getYear () {
        return (mYear);
    }
    
    public int          getMonth () {
        return (mMonth);
    }
    
    public int          getDay () {
        return (mDay);
    }
    
    public void         toCalendar (Calendar cal) {
        cal.set (Calendar.YEAR, mYear);
        cal.set (Calendar.MONTH, mMonth - 1);
        cal.set (Calendar.DAY_OF_MONTH, mDay);
    }
    
    public int          hashCode () {
        return (toInt ());
    }
    
    public boolean      equals (Object other) {
        if (!(other instanceof AbsoluteDate))
            return (false);
        
        AbsoluteDate    d = (AbsoluteDate) other;

        return (
            d.mDay == mDay &&
            d.mMonth == mMonth &&
            d.mYear == mYear
        );        
    }

    public int          compareTo (AbstractDate o) {
        int                 dif;
        
        dif = mYear - o.getYear ();
        
        if (dif != 0)
            return (dif);
        
        dif = mMonth - o.getMonth ();
        
        if (dif != 0)
            return (dif);
        
        return (mDay - o.getDay ());               
    }
    
    public void                 readExternal (ObjectInput in)
        throws IOException
    {
        short             version = in.readShort ();
        mYear = in.readShort ();
        mMonth = in.readByte ();
        mDay = in.readByte ();
    }

    public void                 writeExternal (ObjectOutput out)
        throws IOException
    {
        out.writeShort (EXTERNAL_VERSION);
        out.writeShort (mYear);
        out.writeByte (mMonth);
        out.writeByte (mDay);
    }

    
}
