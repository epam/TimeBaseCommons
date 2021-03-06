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

import java.util.List;

/**
 * Fixed-length circular buffer to hold N objects. non-thread-safe.
 * @author Andy
 *         Date: Jan 19, 2011 10:54:00 AM
 */
public class CircularBuffer<E> {
    protected final Object[] values;

    protected int head = 0; // index for next add()
    protected int tail = -1; // only meaningful if NOT empty => contains index of oldest value in this buffer

    protected boolean isEmpty = true;
    protected boolean isFull = false;
    protected final int capacity;


    public CircularBuffer(int capacity) {
        assert capacity > 0;
        this.capacity = capacity;
        this.values = new Object[capacity];
    }

    public void add(E value) {
        values[head] = value;

        if (isEmpty) {
            tail = head;
            isEmpty = false;
        }

        head = (head + 1) % capacity; // =capacity

        if (isFull) {
            tail = head;
        } else {
            isFull = (head == tail);
        }
    }

    @SuppressWarnings({"unchecked"})
    public E tail() {
        if (isEmpty)
            return null;
        return (E)values[tail];
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public boolean isFull() {
        return isFull;
    }


    /** @return currently number values accumulated */
    public final int size() {
        if (isFull)
            return capacity;
        if (isEmpty)
            return 0;

        if (head > tail)
            return head - tail;
        else
            return capacity - tail + head;
    }

    @SuppressWarnings("unchecked")
    public void appendTo(List<E> result) {
        if ( ! isEmpty) {
            if (isFull) {
                for (int i = 0; i < values.length; i++)
                    result.add ((E)values[i]);
            } else {
                if (tail > head) {
                    for (int i = tail; i < values.length; i++)
                        result.add ((E)values[i]);
                    for (int i = 0; i < head; i++)
                        result.add ((E)values[i]);
                } else {
                    for (int i = tail; i < head; i++)
                        result.add ((E)values[i]);
                }
            }
        }
    }

}