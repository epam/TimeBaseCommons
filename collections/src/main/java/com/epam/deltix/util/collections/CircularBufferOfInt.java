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

import java.util.Arrays;

public final class CircularBufferOfInt {
    public static final int EMPTY = -1;

    protected final int[] values;

    protected int head = 0; // index for next add()
    protected int tail = -1; // only meaningful if NOT empty => contains index of oldest value in this buffer

    protected boolean isEmpty = true;
    protected boolean isFull = false;
    protected final int capacity;


    public CircularBufferOfInt(int capacity) {
        assert capacity > 0;
        this.capacity = capacity;
        this.values = new int[capacity];
        Arrays.fill(values, EMPTY);
    }

    public void add(int value) {
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

    public int tail() {
        if (isEmpty)
            return EMPTY;
        return values[tail];
    }

    public void clear() {
        Arrays.fill(values, EMPTY);
        head = 0;
        tail = -1;
        isEmpty = true;
        isFull = false;
    }
}