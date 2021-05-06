package com.epam.deltix.util.collections;

/**
 * Fixed size circular buffer of double values
 */
public class DoubleQueue {
    private final int           mCapacity;
    private int                 mSize = 0;
    private int                 mHead = 0;
    private int                 mTail = 0;
    private final double []     mBuffer;
    
    public DoubleQueue (int capacity) {
        mCapacity = capacity;
        mBuffer = new double [capacity];
    }
    
    public void                 offer (double value) {
        assert mSize < mCapacity;
        
        mBuffer [mTail] = value;
        
        mSize++;
        mTail++;
        
        if (mTail == mCapacity)
            mTail = 0;                
    }
    
    public double               youngest () {
        assert mSize > 0;
        
        return (mBuffer [mTail]);
    }
    
    public double               oldest () {
        assert mSize > 0;
        
        return (mBuffer [mHead]);
    }
    
    public double               poll () {
        assert mSize > 0;
        
        double  value = mBuffer [mHead];
        
        mSize--;
        mHead++;
        
        if (mHead == mCapacity)
            mHead = 0;
        
        return (value);
    }
    
    public void                 clear () {
        mSize = 0;
        mHead = 0;
        mTail = 0;
    }
    
    public final boolean        isEmpty () {
        return (mSize == 0);
    }
    
    public final boolean        isFull () {
        return (mSize == mCapacity);
    }
    
    public final int            size () {
        return (mSize);
    }  
    
    public final int            capacity () {
        return (mCapacity);
    }  
}
