package deltix.qsrv.hf.blocks;

import deltix.util.collections.DoubleQueue;

/**
 *  Simple Moving Average with Variance
 */
public class SMAV extends DoubleQueue {
    private double      mSum = 0;
    private double      mSumSquares = 0;
    
    public SMAV (int numPeriods) {
        super (numPeriods);
    }

    public double       getSum () {
        return (mSum);
    }
    
    public double       getSumSquares () {
        return (mSumSquares);
    }
    
    public double       getAverage () {
        return (mSum / size ());
    }
    
    public double       getVariance () {
        final double        n = size ();
        final double        mean = mSum / n;
        
        return (mSumSquares / n - mean * mean);
    }
    
    public double       getSqrtVariance () {
        return (Math.sqrt (getVariance ()));
    }
    
    @Override
    public void         offer (double value) {
        if (isFull ()) {
            double  old = super.poll ();
            mSum -= old;
            mSumSquares -= old * old;
        }
        
        super.offer (value);
        mSum += value;
        mSumSquares += value * value;
    }

    @Override
    public void         clear () {
        super.clear ();
        mSum = 0;
    }

    @Override
    public double       poll () {
        throw new UnsupportedOperationException ();
    }    
}
