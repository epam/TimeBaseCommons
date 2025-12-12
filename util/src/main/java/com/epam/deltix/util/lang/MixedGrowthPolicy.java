package com.epam.deltix.util.lang;

/**
 *
 */
public class MixedGrowthPolicy implements GrowthPolicy {
    public double               incFactor;
    public long                 minIncrement;
    public long                 maxIncrement;
    
    public MixedGrowthPolicy (
        double               factor,
        long                 minIncrement,
        long                 maxIncrement
    )
    {
        if (factor <= 1)
            throw new IllegalArgumentException ("Illegal factor: " + factor);
        
        if (minIncrement < 0)
            throw new IllegalArgumentException ("Illegal minIncrement: " + minIncrement);
        
        if (maxIncrement < 2)
            throw new IllegalArgumentException ("Illegal maxIncrement: " + maxIncrement);
        
        this.incFactor = factor - 1;
        this.minIncrement = minIncrement;
        this.maxIncrement = maxIncrement;
    }
        
    public long     computeLength (long curLength, long minLength) {
        long                mixedIncrement =
            Math.max (
                minIncrement, 
                Math.min (
                    maxIncrement,
                    (long) (incFactor * curLength)
                )
            );
        
        return (Math.max (minLength, curLength + mixedIncrement));
    }

}
