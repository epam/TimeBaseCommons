package com.epam.deltix.util.csvx;

import java.text.*;
import java.util.*;

import com.epam.deltix.util.time.GMT;

/**
 *
 */
public class DateColumnDescriptor extends ColumnDescriptor {
    private DateFormat          mFormat;    
    private String              mFormatString;
    
    public DateColumnDescriptor () { 
        setFormat ("yyyy-MM-dd", GMT.TZ);
    }
    
    public DateColumnDescriptor (String header) {
        super (header);
        setFormat ("yyyy-MM-dd", GMT.TZ);
    }   
        
    public String               formatDate (long time) {
        return (mFormat.format (new Date (time)));
    }
    
    public void                 setFormat (String format, String timeZone) {
        setFormat (format, TimeZone.getTimeZone (timeZone));
    }
    
    public void                 setFormat (String format, TimeZone timeZone) {
        mFormatString = format;
        mFormat = new SimpleDateFormat (format);
        mFormat.setTimeZone (timeZone);
    }
        
    public long                 getDate () {
        String      s = getString ();
        
        try {
            synchronized (mFormat) {
                return (mFormat.parse (s).getTime ());
            }
        } catch (ParseException px) {
            throw new NumberFormatException (
                "Date '" + s + "' does not comply with format '" + mFormatString + "'"
            );
        }
    }   
}
