package com.epam.deltix.util.collections;

import java.util.Collection;

/**
 *
 * @author PaharelauK
 */
public class SynchronizedCircularBoundedQueue<E> implements CircularQueue<E>{ 
    
    
    /** The queued items  */
    private final E[] mItems;
    
    /** items index for next take, poll or remove */
    private int mTakeIndex = 0;
    
    /** items index for next put, offer, or add. */
    private int mPutIndex = 0;
    
    /** Number of items in the queue */
    private int mCount;
    
  
    @SuppressWarnings("unchecked")
    public SynchronizedCircularBoundedQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.mItems = (E[]) new Object[capacity];
    }
    
    // Internal helper methods

    /**
     * Circularly increment i.
     */
    final int inc(int i) {
        return (++i == mItems.length) ? 0 : i;
    }

    /**
     * Inserts element at current put position.
     */
    private void insert(E x) {
        mItems[mPutIndex] = x;
        mPutIndex = inc(mPutIndex);
        ++mCount;
        notifyAll();
    }

    /**
     * Extracts element at current take position.
     */
    private E extract() {
        E x = mItems[mTakeIndex];
        mItems[mTakeIndex] = null;
        mTakeIndex = inc(mTakeIndex);
        --mCount;
        notifyAll();
        return x;
    }
    
    /**
     * Inserts the specified element into this queue if it is possible to do so
     * immediately without violating capacity restrictions, returning
     * <tt>true</tt> upon success and throwing an <tt>IllegalStateException</tt>
     * if no space is currently available.
     *
     * <p>This implementation returns <tt>true</tt> if <tt>offer</tt> succeeds,
     * else throws an <tt>IllegalStateException</tt>.</p>
     *
     * @param e the element to add
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     * @throws IllegalStateException if the element cannot be added at this
     *         time due to capacity restrictions
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null and
     *         this queue does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     *         prevents it from being added to this queue
     */
    public boolean add(E e) {
        if (offer(e)) {
            return true;
        } else {
            throw new IllegalStateException("Queue full");
        }
    }

    /**
     * Inserts the specified element at the tail of this queue if it is
     * possible to do so immediately without exceeding the queue's capacity,
     * returning <tt>true</tt> upon success and <tt>false</tt> if this queue
     * is full.  This method is generally preferable to method {@link #add},
     * which can fail to insert an element only by throwing an exception.
     *
     * @throws NullPointerException if the specified element is null
     */
    public synchronized boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        if (mCount == mItems.length) {
            return false;
        } else {
            insert(e);
            return true;
        }
    }
    
    /**
     * Inserts the specified element at the tail of this queue, waiting
     * for space to become available if the queue is full.
     */
    public synchronized void put(E e) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        try {
            while (mCount == mItems.length) {
                wait();
            }
        } catch (InterruptedException ie) {
            notifyAll();
            throw ie;
        }
        insert(e);
    }
    

    public synchronized E take() throws InterruptedException {
        try {
            while (mCount == 0) {
                wait();
            }
        } catch (InterruptedException ie) {
            notifyAll();
            throw ie;
        }
        E x = extract();
        return x;
    }

    public E remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    

    /**
     * Returns the number of elements in this queue.
     *
     * @return the number of elements in this queue
     */
    public synchronized int size() {
        return mCount;
    }
}
