package com.epam.deltix.util.lang;

/**
 *
 */
public interface GrowthPolicy {
    /** @return new capacity based on given current and requested ones */
    public long          computeLength (long curLength, long minLength);
}
