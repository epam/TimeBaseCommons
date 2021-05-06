package com.epam.deltix.util.lang;

/**
 *
 */
public class ExponentialGrowthPolicy implements GrowthPolicy {
    public double   factor;
    public long     minIncrement;
    
    public ExponentialGrowthPolicy () {
        this (1.5);
    }
    
    public ExponentialGrowthPolicy (double factor) {
        if (factor <= 1)
            throw new IllegalArgumentException ("Illegal factor: " + factor);
        
        this.factor = factor;
    }
        
    public long     computeLength (long curLength, long minLength) {
        return (Math.max (minLength, (long) (curLength * factor)));
    }

}
