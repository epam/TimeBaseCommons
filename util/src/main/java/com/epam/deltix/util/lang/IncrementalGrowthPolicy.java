package com.epam.deltix.util.lang;

/**
 *
 */
public class IncrementalGrowthPolicy implements GrowthPolicy {
    public long         increment;

    public IncrementalGrowthPolicy (long increment) {
        if (increment < 1)
            throw new IllegalArgumentException ("Illegal increment: " + increment);
        
        this.increment = increment;
    }
        
    public long     computeLength (long curLength, long minLength) {
        return (Math.max (minLength, curLength + increment));
    }
}
