package deltix.util.csvx;

import java.text.*;
import java.util.*;

/**
 *
 */
public class DateColumnDescriptor extends ColumnDescriptor {
    private DateFormat          mFormat;    
    
    public DateColumnDescriptor () {
        setFormat ("yyyy-MM-dd");
        setTimeZone ("GMT");
    }
    
    public void                 setFormat (String format) {
        setFormat (new SimpleDateFormat (format));
    }
    
    public void                 setFormat (DateFormat format) {
        mFormat = format;
    }
    
    public void                 setTimeZone (TimeZone timeZone) {
        mFormat.setTimeZone (timeZone);
    }
    
    public void                 setTimeZone (String timeZone) {
        setTimeZone (TimeZone.getTimeZone (timeZone));
    }
    
    protected Object            parseValue (String cell) {
        return (getDate (cell));
    }
    
    public Date                 getDate (String s) {
        try {
            synchronized (mFormat) {
                return (mFormat.parse (s));
            }
        } catch (ParseException px) {
            throw new NumberFormatException ("Illegal date: '" + s + "': " + px.toString ());
        }
    }   
}
