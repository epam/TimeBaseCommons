package deltix.util.time;

/**
 *  Computes time arithmetic. Methods of this class may use internal 
 *  buffers and, therefore, must by externally protected from concurrent calls.
 */
public class IntervalCalculator {
    public long             add (long time, Interval interval) {
        if (interval instanceof FixedInterval) 
            return (time + ((FixedInterval) interval).getSizeInMilliseconds ());
        else
            throw new UnsupportedOperationException ();
    }
    
    public long             subtract (long time, Interval interval) {
        if (interval instanceof FixedInterval) 
            return (time - ((FixedInterval) interval).getSizeInMilliseconds ());
        else
            throw new UnsupportedOperationException ();
    }
    
    /**
     *  Normalizes a time by snapping it to the last millisecond of the 
     *  specified period in GMT. For instance, if period == Calendar.DAY,
     *  then any time within March, 12 2007 GMT
     *  will be snapped to the last millisecond of that day.
     * 
     *  @param time          The date/time to normalize
     *  @param unit          The lowest degree of granularity of the period,
     *                          for example, months, days, or minutes. 
     */
    public static long      normalize (long time, TimeUnit unit) {
        if (unit.isFixedSize ()) {            
            long    unitSize = unit.getSizeInMilliseconds ();
            //  Use the fact that 0 is the beginning of all fixed periods in GMT
            return (time / unitSize + unitSize - 1);
        }
        else
            throw new UnsupportedOperationException ();
    }
}
