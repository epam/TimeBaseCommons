package com.epam.deltix.util.collections;

import java.util.Objects;

import com.epam.deltix.util.lang.Bits;

/**
 * Single writer with many readers. If you want to use with many writers you should synchronize writes.
 * Use read operations independently.
 * <pre>
 * Add operation is O(n)       - Write
 * Remove operation is O(n)    - Write
 * Contains operation is O(n)  - Read
 * Visit operation is O(n)     - Read
 * Size operation is O(1)      - Read
 * Get operation is O(1).      - Read
 * </pre>
 */
public final class AtomicArrayList<E> implements AtomicContainer<E> {

    private static final int MIN_CAPACITY = 16;
    private static final int NO_FREE_CELL = -1;
    private static final int DUPLICATE = -2;
    private static final int NO_ELEMENT = -3;

    private volatile UnsafeReferenceArray<E> array;
    private volatile int head = 0;
    private volatile int size = 0;

    public AtomicArrayList(int initialCapacity) {
        array = new UnsafeReferenceArray<>(Math.max(Bits.nextPowerOfTwo(initialCapacity), MIN_CAPACITY));
    }

    /**
     * Tries to find a free cell in array. If found then sets e to it otherwise creates new double sized array.
     */
    @Override
    public void add(E e) {
        Objects.requireNonNull(e);

        int head = this.head;
        UnsafeReferenceArray<E> array = this.array;
        int index = findFreeCell(array);
        add(index, e, head, array);
    }

    @Override
    public boolean addIfAbsent(E e) {
        Objects.requireNonNull(e);

        int head = this.head;
        UnsafeReferenceArray<E> array = this.array;
        int index = findFreeCellWithoutDuplicate(e, head, array);
        if (index != DUPLICATE) {
            add(index, e, head, array);
            return true;
        }

        return false;
    }

    @Override
    public boolean remove(E e) {
        if (e != null) {
            int head = this.head;
            UnsafeReferenceArray<E> array = this.array;

            int index = findElement(e, head, array);
            if (index != NO_ELEMENT) {
                array.setOrdered(index, null);
                size--;
            }
        }

        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E get(int index) {
        int head = this.head;
        UnsafeReferenceArray<E> array = this.array;

        if (index >= head)
            return null;

        return array.getVolatile(index);
    }

    @Override
    public boolean contains(E e) {
        if (e != null) {
            int head = this.head;
            UnsafeReferenceArray<E> array = this.array;

            for (int i = 0; i < head; i++) {
                E obj = array.getVolatile(i);
                if (e.equals(obj))
                    return true;
            }
        }

        return false;
    }

    @Override
    public void visit(Visitor<E> visitor) {
        Objects.requireNonNull(visitor);

        int head = this.head;
        UnsafeReferenceArray<E> array = this.array;

        for (int i = 0; i < head; i++) {
            E e = array.getVolatile(i);
            if (e != null) {
                boolean stop = !visitor.visit(e);
                if (stop)
                    break;
            }
        }
    }

    private void add(int index, E e, int head, UnsafeReferenceArray<E> array) {
        if (index == NO_FREE_CELL) {
            UnsafeReferenceArray<E> newArray = new UnsafeReferenceArray<>(array.length() << 1, array);
            newArray.set(head, e);
            this.array = newArray;
            this.head = head + 1;
        } else {
            array.setOrdered(index, e);
            if (index == head)
                this.head = head + 1;
        }

        size++;
    }

    private int findFreeCell(UnsafeReferenceArray<E> array) {
        for (int i = 0; i < array.length(); i++)
            if (array.get(i) == null)
                return i;

        return NO_FREE_CELL;
    }

    private int findFreeCellWithoutDuplicate(E e, int head, UnsafeReferenceArray<E> array) {
        int index = array.length() == head ? NO_FREE_CELL : head;
        for (int i = head - 1; i >= 0; i--) {
            E cell = array.get(i);
            if (cell == null)
                index = i;
            else if (e.equals(cell))
                return DUPLICATE;
        }

        return index;
    }

    private int findElement(E e, int head, UnsafeReferenceArray<E> array) {
        for (int i = 0; i < head; i++) {
            E obj = array.get(i);
            if (e.equals(obj))
                return i;
        }

        return NO_ELEMENT;
    }

}
