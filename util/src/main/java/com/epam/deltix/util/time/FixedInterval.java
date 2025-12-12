package com.epam.deltix.util.time;

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

    @Override
    public long                 getNumUnits(TimeUnit unit) {
        return (mSize / unit.getSizeInMilliseconds ());
    }

    public FixedInterval        negate () {
        return (new FixedInterval (-mSize));
    }
    
    public FixedInterval        add (FixedInterval interval) {
        return (new FixedInterval (mSize + interval.mSize));
    }
    
    public FixedInterval        subtract (FixedInterval interval) {
        return (new FixedInterval (mSize - interval.mSize));
    }    
    
    @Override
    public int                  hashCode () {
        return ((int) (mSize ^ (mSize >>> 32)));
    }
    
    @Override
    public boolean              equals (Object o) {
        return (
            o instanceof FixedInterval &&
            ((FixedInterval) o).mSize == mSize
        );
    }
}
