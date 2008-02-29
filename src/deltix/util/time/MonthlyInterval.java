package deltix.util.time;

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
        throw new RuntimeException ();
    }
    
    @Override
    public TimeUnit             getUnit () {
        return (TimeUnit.getUnitForMonths (mNumMonths));
    }
    
    @Override
    public long                  getNumUnits () {
        return (mNumMonths / getUnit ().getSizeInMonths ());
    }    
    
    public MonthlyInterval       negate () {
        return (new MonthlyInterval (-mNumMonths));
    }
}
