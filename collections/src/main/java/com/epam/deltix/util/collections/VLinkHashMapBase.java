package com.epam.deltix.util.collections;

import com.epam.deltix.util.collections.hash.*;
import com.epam.deltix.util.memory.MemorySizeEstimator;
import java.io.Serializable;
import java.util.Arrays;

/**
 *  
 */
public abstract class VLinkHashMapBase 
    implements Cloneable, Serializable, MemorySizeEstimator 
{
    protected static final int  NULL = Integer.MIN_VALUE;
    public static final int     MIN_CAPACITY = 16;
    
    private double              shrinkFactor = Double.NaN;
    private int                 bottomThreshold = -1;
    protected int               count = 0;
    protected int               freeHead;
    protected int []            hashIndex;
    protected int []            next;
    protected int []            prev;
    protected final HashCodeComputer hashCodeComputer;

    protected VLinkHashMapBase () {
        this (16);
    }
    
    protected VLinkHashMapBase (int cap) {
        this (cap, SimpleHashCodeComputer.INSTANCE);
    }
    
    protected VLinkHashMapBase (int cap, HashCodeComputer hashCodeComputer) {
        this.hashCodeComputer = hashCodeComputer;
        
        if (cap < MIN_CAPACITY)
            cap = MIN_CAPACITY;
        
        allocTable (cap);
    }
    
    @Override
    public long                 getSizeInMemory () {
        return (
            (MemorySizeEstimator.OBJECT_OVERHEAD + 2 * MemorySizeEstimator.SIZE_OF_INT +
                3 * MemorySizeEstimator.SIZE_OF_POINTER + 3 * MemorySizeEstimator.ARRAY_OVERHEAD) +
            hashIndex.length * (MemorySizeEstimator.SIZE_OF_INT * 3)
        );
    }
    
    /**
     *  Return the ratio of size to capacity, at which the table will shrink. 
     *  For good performance, this factor should be significantly less than 0.5.
     *  When the shrink behavior is turned off, return Double.NaN.
     * 
     *  @see #setShrinkFactor
     */
    public final double           getShrinkFactor () {
        return shrinkFactor;
    }

    /**
     *  Configure the ratio of size to capacity, at which the table will shrink. 
     *  For good performance, this factor should be significantly less than 0.5.
     *  To turn off the shrink behavior, set to Double.NaN.
     * 
     *  @see #getShrinkFactor
     */
    public final void             setShrinkFactor (double shrinkFactor) {
        boolean     off = Double.isNaN (shrinkFactor);
        
        if (!off && (shrinkFactor >= 0.5 || shrinkFactor < 0))
            throw new IllegalArgumentException ("Illegal shrinkFactor (must be [0 .. 0.5): " + shrinkFactor);
        
        this.shrinkFactor = shrinkFactor;
        setBottomThreshold ();          
    }
    
    private final void            setBottomThreshold () {
        int     cap = next.length;
        
        if (Double.isNaN (shrinkFactor) || cap < MIN_CAPACITY)
            bottomThreshold = -1;
        else {
            bottomThreshold = (int) (cap * shrinkFactor);
        }
    }
    
    public final int            size () {
        return (count);
    }
    
    public final int            getCapacity () {
        return (next.length);
    }
    
    public void                 clear () {
        format ();
    }
    
    private void                format () {
        count = 0;
            
        Arrays.fill (hashIndex, NULL);       
        Arrays.fill (prev, NULL);       
        
        int         cap = prev.length;
        
        freeHead = cap - 1;  
        
        next [0] = NULL;   
        
        for (int ii = 1; ii < cap; ii++) 
            next [ii] = ii - 1; 
    }
    
    protected void              allocTable (int cap) {
        hashIndex = new int [cap];
        next = new int [cap];
        prev = new int [cap];
        
        format ();
        
        setBottomThreshold ();
    }
    
    protected void              free (int idx) {
        //
        // Remove [idx] from chain
        //
        int         nx = next [idx];
        int         pv = prev [idx];
        
        if (nx != NULL)
            prev [nx] = pv;
        
        if (pv < 0)
            hashIndex [-pv - 1] = nx;
        else
            next [pv] = nx;
        //
        // Link [idx] to free list
        //
        next [idx] = freeHead;
        prev [idx] = NULL;  // prev must be NULL in free list
        freeHead = idx;
        count--;
    }
    
    protected int               allocEntry (int hidx) {
        assert freeHead != NULL : "Free list is empty. Cannot expand here.";        
        assert isEmpty (freeHead) : "Element [freeHead=" + freeHead + "] is not free";
        
        int             newChainHeadIdx = freeHead;
        
        freeHead = next [newChainHeadIdx];    
        
        int             oldChainHeadIdx = hashIndex [hidx];
        
        next [newChainHeadIdx] = oldChainHeadIdx;
        
        if (oldChainHeadIdx != NULL)
            prev [oldChainHeadIdx] = newChainHeadIdx;
        
        prev [newChainHeadIdx] = -hidx - 1;
        hashIndex [hidx] = newChainHeadIdx;
        
        count++;
        return (newChainHeadIdx);
    }
    
    protected final boolean     isFilled (int idx) {
        return (prev [idx] != NULL);
    }
    
    protected final boolean     isEmpty (int idx) {
        return (prev [idx] == NULL);
    }
}
