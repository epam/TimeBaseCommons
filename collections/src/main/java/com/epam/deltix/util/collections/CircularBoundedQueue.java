package com.epam.deltix.util.collections;

/**
 *
 * @author PaharelauK
 */
public class CircularBoundedQueue<E> implements CircularQueue<E>{


    /** The queued items  */
    private final E[] mItems;

    /** items index for next take, poll or remove */
    private int mTakeIndex = 0;

    /** items index for next put, offer, or add. */
    private int mPutIndex = 0;

    /** Number of items in the queue */
    private int mCount;

    private final int mCapacity;


    @SuppressWarnings("unchecked")
    public CircularBoundedQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        mCapacity = capacity;
        this.mItems = (E[]) new Object[capacity];
    }

    // Internal helper methods

    /**
     * Circularly increment i.
     */
    private final int inc(int i) {
        return (++i == mCapacity) ? 0 : i;
    }

    /**
     * Inserts element at current put position.
     */
    private final void insert(E x) {
        mItems[mPutIndex] = x;
        mPutIndex = inc(mPutIndex);
        ++mCount;
    }

    /**
     * Extracts element at current take position.
     */
    private final E extract() {
        E x = mItems[mTakeIndex];
        mItems[mTakeIndex] = null;
        mTakeIndex = inc(mTakeIndex);
        --mCount;
        return x;
    }

    /**
     * Inserts the specified element into this queue if it is possible to do so
     * immediately without violating capacity restrictions, returning
     * <code>true</code> upon success and throwing an <code>IllegalStateException</code>
     * if no space is currently available.
     *
     * <p>This implementation returns <code>true</code> if <code>offer</code> succeeds,
     * else throws an <code>IllegalStateException</code>.</p>
     *
     * @param e the element to add
     * @return <code>true</code> (as specified by {@link java.util.Collection#add})
     * @throws IllegalStateException if the element cannot be added at this
     *         time due to capacity restrictions
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null and
     *         this queue does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     *         prevents it from being added to this queue
     */
    public final boolean add(E e) {
        if (offer(e)) {
            return true;
        } else {
            throw new IllegalStateException("Queue full");
        }
    }

    /**
     * Inserts the specified element at the tail of this queue if it is
     * possible to do so immediately without exceeding the queue's capacity,
     * returning <code>true</code> upon success and <code>false</code> if this queue
     * is full.  This method is generally preferable to method {@link #add},
     * which can fail to insert an element only by throwing an exception.
     *
     * @throws NullPointerException if the specified element is null
     */
    private final boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        if (mCount == mCapacity) {
            return false;
        } else {
            insert(e);
            return true;
        }
    }


    public final E remove(){
        if (mCount == 0)
             return null;
        E x = extract();
        return x;
    }

    /**
     * Returns the number of elements in this queue.
     *
     * @return the number of elements in this queue
     */
    public final int count() {
        return mCount;
    }

    public int capacity() {
        return mCapacity;
    }


}
