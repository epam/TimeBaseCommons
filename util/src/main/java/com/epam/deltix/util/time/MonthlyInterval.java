package com.epam.deltix.util.time;

/**
 *
 */
public class MonthlyInterval extends Interval {
    private final int           mNumMonths;
    
    /**
     * Constructs a MonthlyInterval from number of months.
     */
    public MonthlyInterval (int numMonths) {
        mNumMonths = numMonths;
    }
    
    /**
     *  Constructs a MonthlyInterval interval from a number of units.
     */
    public MonthlyInterval (int numUnits, TimeUnit unit) {
        mNumMonths = numUnits * unit.getSizeInMonths ();
    }
    
    /**
     *  Returns the number of months in this interval.
     */
    public int                  getNumberOfMonths () {
        return (mNumMonths);
    }
    
    @Override
    public TimeUnit             getUnit () {
        return (TimeUnit.getUnitForMonths (mNumMonths));
    }
    
    @Override
    public long                  getNumUnits () {
        return (mNumMonths / getUnit ().getSizeInMonths ());
    }

    @Override
    public long                 getNumUnits(TimeUnit unit) {
        return (mNumMonths / unit.getSizeInMonths ());
    }

    public MonthlyInterval       negate () {
        return (new MonthlyInterval (-mNumMonths));
    }
    
    public MonthlyInterval       add (MonthlyInterval interval) {
        return (new MonthlyInterval (mNumMonths + interval.mNumMonths));
    }
    
    public MonthlyInterval       subtract (MonthlyInterval interval) {
        return (new MonthlyInterval (mNumMonths - interval.mNumMonths));
    }
    
    @Override
    public int                  hashCode () {
        return (mNumMonths);
    }
    
    @Override
    public boolean              equals (Object o) {
        return (
            o instanceof MonthlyInterval &&
            ((MonthlyInterval) o).mNumMonths == mNumMonths
        );
    }    
}
