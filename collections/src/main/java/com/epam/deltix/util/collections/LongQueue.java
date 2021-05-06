package com.epam.deltix.util.collections;


/**
 * Fixed size circular buffer of long values
 */
public class LongQueue {
    private final int           mCapacity;
    private int                 mSize = 0;
    private int                 mHead = 0;
    private int                 mTail = 0;
    private final long []       mBuffer;
    
    public LongQueue (int capacity) {
        mCapacity = capacity;
        mBuffer = new long [capacity];
    }
    
    public void                 offer (long value) {
        assert mSize < mCapacity;
        
        mBuffer [mTail] = value;
        
        mSize++;
        mTail++;
        
        if (mTail == mCapacity)
            mTail = 0;                
    }
    
    public long               youngest () {
        assert mSize > 0;
        
        return (mBuffer [mTail]);
    }
    
    public long               oldest () {
        assert mSize > 0;
        
        return (mBuffer [mHead]);
    }
    
    public long               poll () {
        assert mSize > 0;
        
        long  value = mBuffer [mHead];
        
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
