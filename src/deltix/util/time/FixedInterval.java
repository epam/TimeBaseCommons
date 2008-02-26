package deltix.util.time;

/**
 *
 */
public class FixedInterval extends Interval {
    /**
     * Constructs a fixed interval from number of milliseconds.
     */
    public FixedInterval (long numMilliseconds) {
        throw new RuntimeException ();
    }
    
    /**
     * Constructs a fixed interval from a number of units.
     */
    public FixedInterval (long numUnits, TimeUnit unit) {
        throw new RuntimeException ();
    }
    
    /**
     * Returns the fixed size of this interval in milliseconds.
     */
    public long             getFixedSize () {
        throw new RuntimeException ();
    }
}
