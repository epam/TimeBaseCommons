package deltix.util.time;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import deltix.util.text.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.logging.Logger;

/**
 * Represents an interval (also known as time span in some systems), which
 * can be added to, or subtracted from, a date. This class is immutable.
 */
@XmlJavaTypeAdapter (Interval.StringIntervalAdapter.class)
public abstract class Interval {
    public static class StringIntervalAdapter extends XmlAdapter<String, Interval> {
        public Interval unmarshal(String v) throws Exception {
            return Interval.valueOf(v);
        }

        public String marshal(Interval v) throws Exception {
            return v.toString();
        }
    }
    
    private static final Logger LOGGER = Logger.getLogger (Interval.class.getName());


    public static final Interval    ZERO = null;
    public static final Interval DAY = Interval.valueOf("1D");
    public static final Interval HOUR = Interval.valueOf("1H");
    public static final Interval MINUTE = Interval.valueOf("1I");
    public static final Interval SECOND = Interval.valueOf("1S");

    protected Interval () {
    }

    public static Interval          create (long num, TimeUnit unit) {
        if (unit.isFixedSize ())
            return (new FixedInterval (num, unit));
        else {
            int     intNum = (int) num;

            if (intNum != num)
                throw new IllegalArgumentException (num + ": too large");

            return (new MonthlyInterval (intNum, unit));
        }
    }

    public static Interval          valueOf (String text) {
        return (valueOf ((CharSequence) text));
    }

    /**
     * Parse a QQL string.
     * @param text  The QQL representation of the interval.
     */
    public static Interval          valueOf (CharSequence text) {
        if (text == null)
            return (null);
        
        int         end = text.length ();
        if (end == 0)
            return null;
        
        if (end < 2)
            throw new IllegalArgumentException ("Interval must consist of at least two characters. For example: \"1D\" or \"5Y\", got: " + text.toString ());
        
        long        num = CharSequenceParser.parseLong (text, 0, end - 1);
        TimeUnit    unit = TimeUnit.fromSuffix (text.charAt (end - 1));
        
        return (create (num, unit));
    }
    
    /**
     * Returns whether this interval is zero.
     */
    public final boolean            isZero () {
        return (getNumUnits () == 0);
    }
    
    /**
     * Returns whether this interval is positive.
     */
    public final boolean            isPositive () {
        return (getNumUnits () > 0);
    }
    
    /**
     * Returns whether this interval is negative.
     */
    public final boolean            isNegative () {
        return (getNumUnits () < 0);
    }
    
    /**
     * Returns the negated value of this Interval.
     */
    public abstract Interval        negate ();
    
    /**
     *  Returns the underlying time unit.
     */    
    public abstract TimeUnit        getUnit ();
    
    /**
     *  Returns the duration of this interval in in units
     *  returned by {@link #getUnit}. Result can be negative.
     * 
     *  @see #getUnit
     */
    public abstract long            getNumUnits ();

     /**
     *  Returns the duration of this interval in in given units.
     *  Result can be negative.       
     */
    public abstract long            getNumUnits (TimeUnit unit);

    /**
     *  Returns the short representation of this interval, such as
     *  <tt>-4Q</tt>
     */
    @Override
    public String                   toString () {
        StringBuilder   sb = new StringBuilder ();
        sb.append (getNumUnits ());
        sb.append (getUnit ().getSuffix ());
        return (sb.toString ());
    }

    public String toHumanString() {
        StringBuilder   sb = new StringBuilder ();
        sb.append (getNumUnits ());
        sb.append (' ');
        sb.append (getUnit ().name ());
        return (sb.toString ());
    }


    public long toMilliseconds() {
        return toMilliseconds(this);
    }

    public static long toMilliseconds(Interval interval) {
        if (interval.getUnit().isVariableSize()) {
            return interval.getNumUnits() * interval.getUnit().getSizeInMonths() *
                   deltix.util.time.TimeUnit.DAY.getSizeInMilliseconds() * 30;
        }
        return interval.getNumUnits() * interval.getUnit().getSizeInMilliseconds();
    }

    // Used in .NET code (dotnet/talclient/Deltix/Data/RealTick\Cursor.cs) 
    public static FixedInterval     parse(long size) {

        FixedInterval result = new FixedInterval(size, TimeUnit.MILLISECOND);

        if (size % toMilliseconds(Interval.DAY) == 0)
            result = new FixedInterval(size / toMilliseconds(Interval.DAY), TimeUnit.DAY);
        if (size % toMilliseconds(Interval.HOUR) == 0)
            result = new FixedInterval(size / toMilliseconds(Interval.HOUR), TimeUnit.HOUR);
        if (size % toMilliseconds(Interval.MINUTE) == 0)
            result = new FixedInterval(size / toMilliseconds(Interval.MINUTE), TimeUnit.MINUTE);
        if (size % toMilliseconds(Interval.SECOND) == 0)
            result = new FixedInterval(size / toMilliseconds(Interval.SECOND), TimeUnit.SECOND);

        return result;
    }

    public static long getSystemProperty(String propName, long defaultValue, long minValue, long maxValue) {
        String intervalValue = System.getProperty (propName);
        if (intervalValue == null)
            return defaultValue;
        intervalValue = intervalValue.trim();
        if (intervalValue.isEmpty())
            return defaultValue;

        Interval interval = Interval.valueOf(intervalValue);
        long result = interval.toMilliseconds();
        if (result < minValue) {
            LOGGER.severe("Property \"" + propName + "\" cannot be less than " + minValue);
            result = minValue;
}
        if (result > maxValue) {
            LOGGER.severe("Property \"" + propName + "\" cannot be more than " + maxValue);
            result = maxValue;
        }
        return result;
    }
}
