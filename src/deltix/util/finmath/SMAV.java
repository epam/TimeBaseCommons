package deltix.util.finmath;

import deltix.util.collections.DoubleQueue;

/**
 *  Simple Moving Average with Variance
 */
public class SMAV implements MovingAverage {
    private final DoubleQueue   q;
    private double              mSum = 0;
    private double              mSumSquares = 0;
    
    public SMAV (int numPeriods) {
        q = new DoubleQueue (numPeriods);
    }

    public boolean      isFull () {
        return (q.isFull ());
    }
    
    public double       getSum () {
        return (mSum);
    }
    
    public double       getSumSquares () {
        return (mSumSquares);
    }
    
    @Override
    public double       getLastRegisteredValue () {
        return (q.youngest ());
    }
    
    @Override
    public double       getAverage () {
        return (mSum / q.size ());
    }
    
    public double       getVariance () {
        final double        n = q.size ();
        final double        mean = mSum / n;
        
        return (mSumSquares / n - mean * mean);
    }
    
    public double       getStdDev () {
        return (Math.sqrt (getVariance ()));
    }

    @Override
    public double       update (double value) {
        register (value);
        return (getAverage ());
    }
        
    @Override
    public void         register (double value) {
        if (q.isFull ()) {
            double  old = q.poll ();
            mSum -= old;
            mSumSquares -= old * old;
        }
        
        q.offer (value);
        mSum += value;
        mSumSquares += value * value;
    }

    @Override
    public void         clear () {
        q.clear ();
        mSum = 0;
    }
}
