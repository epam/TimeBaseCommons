package deltix.util.time;

/**
 *
 */
public final class FixedInterval extends Interval {
    private final long      mSize;
    
    /**
     *  Constructs a fixed interval from number of milliseconds. Guesses the
     *  underlying unit.
     */
    public FixedInterval (long size) {
        mSize = size;
    }
    
    /**
     *  Constructs a fixed interval from a number of units.
     */
    public FixedInterval (long numUnits, TimeUnit unit) {
        mSize = numUnits * unit.getSizeInMilliseconds ();
    }
    
    /**
     *  Returns the size of this interval in milliseconds.
     */
    public long                 getSizeInMilliseconds () {
        return (mSize);
    }
    
    @Override
    public TimeUnit             getUnit () {
        return (TimeUnit.getUnitForMilliseconds (mSize));
    }
    
    @Override
    public long                 getNumUnits () {
        return (mSize / getUnit ().getSizeInMilliseconds ());
    }
    
    public FixedInterval        negate () {
        return (new FixedInterval (-mSize));
    }
}
