package deltix.util.collections;

import deltix.util.memory.MemorySizeEstimator;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Long open addressing hash map with
 * quadratic probing.
 */
public abstract class HashMapBase
        implements Cloneable, Serializable, MemorySizeEstimator {
    protected static final byte EMPTY = 0;
    protected static final byte DELETED = 1;
    protected static final byte FILLED = 2;

    private byte[] mStatus;
    private int mThreshold;

    private int mCount = 0;
    private int mUsedCells = 0;

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

        for (; !isPrime(n); n += 2) ;

        return (n);
    }

    /**
     * Allocates the internal table.
     */
    protected void alloc(int tabSize) {
        tabSize = nextPrime(tabSize);

        allocValues(tabSize);
        allocKeys(tabSize);
        mStatus = new byte[tabSize];
        mThreshold = tabSize / 2;
    }

    /**
     * Constructs a has map with specified capacity, i.e.
     * number of elements that can be added before it is resized.
     * The actual internal table size is at least 2*capacity.
     */
    public HashMapBase(int initCapacity) {
        alloc(Math.max(11, initCapacity * 2 + 1));
    }

    /**
     * Constructs a has map with default capacity.
     */
    public HashMapBase() {
        this(11);
    }

    /**
     * Returns the number of elements after which the table will get
     * resized.
     */
    public int getCapacity() {
        return (mThreshold);
    }

    /**
     * Returns the count of elements currently in the map.
     */
    public int size() {
        return (mCount);
    }

    /**
     * Returns whether the map is empty.
     */
    public boolean isEmpty() {
        return (mCount == 0);
    }

    /**
     * Returns the size of the internal arrays.
     */
    protected int tableSize() {
        return (mStatus.length);
    }

    protected boolean isFound(int pos) {
        return (mStatus[pos] == FILLED);
    }

    protected boolean isCellEmpty(int pos) {
        return (mStatus[pos] == EMPTY);
    }

    protected abstract void allocValues(int size);

    protected abstract void allocKeys(int size);

    protected abstract int rehashOne(Object keys, Object values, int idx);

    protected abstract Object keyArray();

    protected abstract Object valueArray();

    protected final void rehash(int newCapacity) {
        Object saveKeys = keyArray();
        Object saveValues = valueArray();
        byte[] saveStatus = mStatus;

        int tabSize = tableSize();

        if (newCapacity < tabSize)
            throw new IllegalArgumentException("Requested " +
                    newCapacity + " elements, but size is already " +
                    tabSize
            );
        else if (newCapacity == tabSize)
            return;

        alloc(newCapacity);

        for (int ii = 0; ii < tabSize; ii++)
            if (saveStatus[ii] == FILLED) {
                int pos = rehashOne(saveKeys, saveValues, ii);
                mStatus[pos] = FILLED;
            }

        mUsedCells = mCount;
    }


    protected final void onPut(int pos, boolean wasNotFound) {
        mStatus[pos] = FILLED;
        if (wasNotFound) {
            mCount++;
            mUsedCells++;
        }

        if (mUsedCells >= mThreshold)
            rehash(2 * tableSize());
    }

    protected final void onRemove(int pos) {
        mStatus[pos] = DELETED;
        mCount--;
    }

    public void ensureCapacity(int capacity) {
        if (mThreshold < capacity)
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
