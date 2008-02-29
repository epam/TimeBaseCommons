package deltix.util.time;

import deltix.util.text.*;

/**
 * Represents an interval (also known as time span in some systems), which
 * can be added to, or subtracted from, a date. This class is immutable.
 */
public abstract class Interval {
    public static final Interval    ZERO = null;
    
    protected Interval () {
    }
    
    /**
     * Parse a QQL string.
     * @param text  The QQL representation of the interval.
     */
    public static Interval          parseQQL (CharSequence text) {
        int         end = text.length ();
        
        if (end < 2)
            throw new IllegalArgumentException (text.toString ());
        
        long        num = CharSequenceParser.parseLong (text, 0, end - 1);
        TimeUnit    unit = TimeUnit.fromSuffix (text.charAt (end - 1));
        
        if (unit.isFixedSize ())
            return (new FixedInterval (num, unit));
        else {
            int     intNum = (int) num;
            
            if (intNum != num)
                throw new IllegalArgumentException (text.toString ());
            
            return (new MonthlyInterval (intNum, unit));               
        }
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
     *  Returns the size of this interval in in units
     *  returned by {@link #getUnit}.
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
}
