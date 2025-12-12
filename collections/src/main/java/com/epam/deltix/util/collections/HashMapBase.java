package com.epam.deltix.util.collections;

import com.epam.deltix.util.memory.MemorySizeEstimator;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Long open addressing hash map with
 * quadratic probing.
 */
public abstract class HashMapBase
    implements Cloneable, Serializable, MemorySizeEstimator 
{    
    public static final int     MIN_TABLE_SIZE = 23;
    public static final int     MIN_CAPACITY = MIN_TABLE_SIZE / 2;
    
    protected static final byte EMPTY = 0;
    protected static final byte DELETED = 1;
    protected static final byte FILLED = 2;

    private byte []         mStatus;
    private int             topThreshold;
    private int             bottomThreshold = -1;
    private double          shrinkFactor = Double.NaN;
    private int             mCount = 0;
    private int             mUsedCells = 0;

    public long getSizeInMemory() {
        return (OBJECT_OVERHEAD + mStatus.length + 3 * SIZE_OF_INT);
    }

    /**
     * Reasonably quick test for prime numbers
     * with 0 memory requirement.
     */
    private static boolean isPrime(int n) {
        for (int i = 3; (i * i) <= n; i += 2)
            if (n % i == 0)
                return (false);

        return (true);
    }

    /**
     * Returns the lowest prime number greater or equal to n.
     */
    private static int nextPrime(int n) {
        if (n % 2 == 0)
            n++;

        while (!isPrime (n))
            n += 2;

        return (n);
    }

    /**
     * Allocates the internal table.
     */
    protected final void alloc(int tabSize) {

        tabSize = nextPrime(tabSize);

        allocValues(tabSize);
        allocKeys(tabSize);
        mStatus = new byte[tabSize];
        topThreshold = tabSize / 2;
        setBottomThreshold ();
    }

    /**
     * Constructs a has map with specified capacity, i.e.
     * number of elements that can be added before it is resized.
     * The actual internal table size is at least 2*capacity.
     */
    public HashMapBase(int initCapacity) {
        alloc(Math.max(MIN_TABLE_SIZE, initCapacity * 2 + 1));
    }

    /**
     * Constructs a has map with default capacity.
     */
    public HashMapBase() {
        this(11);
    }

    /**
     *  Return the load factor value, at which the table will shrink to half its size. 
     *  For good performance, this factor should be significantly less than 0.5.
     *  When the shrink behavior is turned off, return Double.NaN.
     * 
     *  @see #setShrinkFactor
     */
    public final double           getShrinkFactor () {
        return shrinkFactor;
    }

    /**
     *  Configure the load factor value, at which the table will shrink to half its size. 
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
        if (Double.isNaN (shrinkFactor) || topThreshold < 6)
            bottomThreshold = -1;
        else {
            bottomThreshold = (int) (topThreshold * shrinkFactor);
        }
    }
    
    /**
     * Returns the ratio of size to capacity.
     */
    public final double   getLoadFactor () {
        return (((double) mCount) / topThreshold);
    }
    
    /**
     * Returns the number of elements after which the table will get
     * resized.
     */
    public final int      getCapacity() {
        return (topThreshold);
    }

    /**
     * Returns the count of elements currently in the map.
     */
    public final int size() {
        return (mCount);
    }

    /**
     * Returns whether the map is empty.
     */
    public final boolean isEmpty() {
        return (mCount == 0);
    }

    /**
     * Returns the size of the internal arrays.
     */
    protected final int tableSize() {
        return (mStatus.length);
    }

    protected final boolean isFound(int pos) {
        return (mStatus[pos] == FILLED);
    }

    protected final boolean isCellEmpty(int pos) {
        return (mStatus[pos] == EMPTY);
    }

    protected final boolean isCellDeleted(int pos) {
        return (mStatus[pos] == DELETED);
    }

    protected abstract void allocValues(int size);

    protected abstract void allocKeys(int size);

    protected abstract int rehashOne(Object keys, Object values, int idx);

    protected abstract Object keyArray();

    protected abstract Object valueArray();

    /**
     *  This method may be called with any value of newTableSize, including 
     *  the current table size. Even if specified size is the same, this method 
     *  performs internal hash-table garbage collection, and should still be 
     *  executed.
     */
    protected final void rehash (int newTableSize) {
        Object saveKeys = keyArray();
        Object saveValues = valueArray();
        byte[] saveStatus = mStatus;

        int tabSize = tableSize();
        
        alloc (newTableSize);

        for (int ii = 0; ii < tabSize; ii++) {
            if (saveStatus[ii] == FILLED) {
                int pos = rehashOne(saveKeys, saveValues, ii);

                mStatus[pos] = FILLED;
            }
        }
        
        mUsedCells = mCount;
    }

    protected final void onPut(int pos) {

        byte status = mStatus[pos];
        mStatus [pos] = FILLED;
        
        if (status != FILLED) {
            mCount++;

            if (status != DELETED)
                mUsedCells++;
            
            if (mUsedCells >= topThreshold) {
                if (mUsedCells >= mCount * 2)   // Just collect garbage
                    rehash (tableSize ());
                else    // Grow
                    rehash (2 * tableSize());
            }
        }       
    }

    protected void moveEntry (int from, int to) {
        assert mStatus [from] == FILLED;
        assert mStatus [to] == DELETED;
        
        mStatus [to] = FILLED;
        mStatus [from] = DELETED;
    }
    
    protected void onRemove(int pos) {
        mStatus[pos] = DELETED;
        mCount--;
        
        if (mCount < bottomThreshold) {
            int     n = tableSize () / 2;
        
            assert n > mCount * 2; // just checking...
            
            if (n < MIN_TABLE_SIZE)
                n = MIN_TABLE_SIZE;
            
            rehash (n);
        }

        // prevent redundant allocations on putting new elements
        if (mCount == 0)
            clear();
    }

    public void ensureCapacity(int capacity) {
        if (topThreshold < capacity)
            rehash(2 * capacity);
    }

    public void clear() {
        Arrays.fill(mStatus, EMPTY);
        mUsedCells = mCount = 0;
    }

    /// serialization code

    static final long serialVersionUID = 1L;
    static final short mSerialVersion = 1;

    private void writeObject(java.io.ObjectOutputStream out)
            throws java.io.IOException {
        out.writeShort(mSerialVersion);
    }

    private void readObject(java.io.ObjectInputStream in)
            throws java.io.IOException, ClassNotFoundException {
        short readSerialVersion = in.readShort();
    }

}
