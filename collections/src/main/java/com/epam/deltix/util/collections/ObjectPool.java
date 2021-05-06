package com.epam.deltix.util.collections;


import com.epam.deltix.util.collections.generated.ObjectArrayList;

/**
 *  Simple Pool of objects. Is Not Thread-Safe.
 */
public class ObjectPool<T> {

    private final ObjectArrayList<T> pool;
    private int availIdx = 0;

    public ObjectPool() {
        this(5);
    }

    public ObjectPool(int capacity) {
        pool = new ObjectArrayList<>(capacity);
    }

    public final T          borrow() {
        if (availIdx < pool.size())
            return pool.get(availIdx++);

        return null;
    }

    public void             add(T used) {

        if (availIdx < pool.size())
            pool.insert(availIdx++, 1, used);
        else
            pool.add(used);
    }

    public final void       reset() {
        availIdx = 0;
    }

    public final void       clear() {
        pool.clear();
    }
}