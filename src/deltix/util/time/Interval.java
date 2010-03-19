package deltix.util.time;

import deltix.qsrv.pxml.StringIntervalAdapter;
import deltix.util.text.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Represents an interval (also known as time span in some systems), which
 * can be added to, or subtracted from, a date. This class is immutable.
 */
@XmlJavaTypeAdapter (StringIntervalAdapter.class)
public abstract class Interval {
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
}
