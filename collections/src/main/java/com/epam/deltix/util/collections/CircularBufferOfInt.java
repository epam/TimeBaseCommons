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
