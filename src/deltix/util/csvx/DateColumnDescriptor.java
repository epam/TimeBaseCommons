package deltix.util.csvx;

import java.text.*;
import java.util.*;

import deltix.util.time.GMT;

/**
 *
 */
public class DateColumnDescriptor extends ColumnDescriptor {
    private DateFormat          mFormat;    
    
    public DateColumnDescriptor () {
        setFormat ("yyyy-MM-dd", GMT.TZ);
    }
    
    public void                 setFormat (String format, String timeZone) {
        setFormat (format, TimeZone.getTimeZone (timeZone));
    }
    
    public void                 setFormat (String format, TimeZone timeZone) {
        mFormat = new SimpleDateFormat (format);
        mFormat.setTimeZone (timeZone);
    }
        
    protected Object            parseValue (CharSequence cell) {
        return (getDate (cell));
    }
    
    public Date                 getDate (CharSequence s) {
        try {
            synchronized (mFormat) {
                return (mFormat.parse (s.toString ()));
            }
        } catch (ParseException px) {
            throw new NumberFormatException ("Illegal date: '" + s + "': " + px.toString ());
        }
    }   
}
