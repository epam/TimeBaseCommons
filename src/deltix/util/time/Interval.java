package deltix.util.time;

/**
 * Represents an interval (also known as time span in some systems), which
 * can be added to, or subtracted from, a date. This class is immutable.
 */
public class Interval {
    /**
     * Create a zero interval.
     */
    public static Interval      createZeroInterval () {    
        throw new RuntimeException ();
    }
    
    /**
     * Parse a QQL string.
     * @param text  The QQL representation of the interval.
     */
    public static Interval      parseQQL (CharSequence text) {
        throw new RuntimeException ();
    }
    
    /**
     * 
     * @return  The QQL representation of this interval.
     */
    public String           toString () {
        throw new RuntimeException ();
    }
    
    /**
     * Returns whether this interval is zero.
     */
    public boolean          isZero () {
        throw new RuntimeException ();
        
    }
    
    /**
     * Returns whether this interval is positive.
     */
    public boolean          isPositive () {
        throw new RuntimeException ();
        
    }
    
    /**
     * Returns whether this interval is negative.
     */
    public boolean          isNegative () {
        throw new RuntimeException ();
        
    }
    
    /**
     * Returns the negated value of this Interval.
     */
    public Interval         negate () {
        throw new RuntimeException ();
    }
}
