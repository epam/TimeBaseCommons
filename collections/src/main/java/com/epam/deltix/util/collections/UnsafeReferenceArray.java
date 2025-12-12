package com.epam.deltix.util.collections;

import java.util.Arrays;

import sun.misc.Unsafe;

import com.epam.deltix.util.lang.Bits;
import com.epam.deltix.util.memory.UnsafeAccess;

/**
 * Use with care. Access without bounds check.
 */
public final class UnsafeReferenceArray<E> {

    private static final Unsafe UNSAFE = UnsafeAccess.UNSAFE;
    private static final int BASE = UnsafeAccess.arrayBaseOffset(Object[].class);
    private static final int SHIFT = Bits.computeShift(UnsafeAccess.arrayIndexScale(Object[].class));

    private final Object[] array;

    public UnsafeReferenceArray(int length) {
        array = new Object[length];
    }

    public UnsafeReferenceArray(UnsafeReferenceArray<E> array) {
        this(array.length(), array);
    }

    public UnsafeReferenceArray(int length, UnsafeReferenceArray<E> array) {
        this.array = Arrays.copyOf(array.array, length);
    }

    public UnsafeReferenceArray(E[] array) {
        this(array.length, array);
    }

    public UnsafeReferenceArray(int length, E[] array) {
        this.array = Arrays.copyOf(array, length, Object[].class);
    }

    public int length() {
        return array.length;
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        return (E) UNSAFE.getObject(array, offset(index));
    }

    @SuppressWarnings("unchecked")
    public E getVolatile(int index) {
        return (E) UNSAFE.getObjectVolatile(array, offset(index));
    }

    public void set(int index, E value) {
        UNSAFE.putObject(array, offset(index), value);
    }

    public void setVolatile(int index, E value) {
        UNSAFE.putObjectVolatile(array, offset(index), value);
    }

    public void setOrdered(int index, E value) {
        UNSAFE.putOrderedObject(array, offset(index), value);
    }

    public boolean compareAndSet(int index, E expect, E update) {
        return UNSAFE.compareAndSwapObject(array, offset(index), expect, update);
    }

    private static long offset(int i) {
        return ((long) i << SHIFT) + BASE;
    }

}
