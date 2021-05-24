/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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
     * <tt>true</tt> upon success and throwing an <tt>IllegalStateException</tt>
     * if no space is currently available.
     *
     * <p>This implementation returns <tt>true</tt> if <tt>offer</tt> succeeds,
     * else throws an <tt>IllegalStateException</tt>.</p>
     *
     * @param e the element to add
     * @return <tt>true</tt> (as specified by {@link java.util.Collection#add})
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
     * returning <tt>true</tt> upon success and <tt>false</tt> if this queue
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