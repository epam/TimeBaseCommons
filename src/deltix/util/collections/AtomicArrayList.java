package deltix.util.collections;

import java.util.Objects;

import deltix.util.lang.Bits;

/**
 * Writes are blocked. Reads are wait free.
 * Use read operations independently.
 * <p/>
 * Add operation is O(n)
 * Remove operation is O(n)
 * Contains operation is O(n)
 * Visit operation is O(n)
 * Size operation is O(1)
 * Get operation is O(1).
 */
public final class AtomicArrayList<E> implements AtomicContainer<E> {

    private static final int MIN_CAPACITY = 16;
    private static final int NO_FREE_CELL = -1;
    private static final int DUPLICATE = -2;

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

        synchronized (this) {
            UnsafeReferenceArray<E> array = this.array;
            int head = this.head;
            int index = findFreeCell(array);
            add(index, head, e, array);
        }
    }

    @Override
    public boolean addIfAbsent(E e) {
        Objects.requireNonNull(e);

        synchronized (this) {
            UnsafeReferenceArray<E> array = this.array;
            int head = this.head;
            int index = findFreeCellWithoutDuplicate(head, e, array);
            if (index != DUPLICATE) {
                add(index, head, e, array);
                return true;
            }
        }

        return false;
    }

    // TODO: move head left if removed last
    @Override
    public synchronized boolean remove(E e) {
        if (e != null) {
            synchronized (this) {
                UnsafeReferenceArray<E> array = this.array;
                int head = this.head;

                for (int i = 0; i < head; i++) {
                    if (e.equals(array.get(i))) {
                        array.setOrdered(i, null);
                        size--;
                        return true;
                    }
                }
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
        UnsafeReferenceArray<E> array = this.array;
        int head = this.head;

        if (index >= head)
            return null;

        return array.getVolatile(index);
    }

    @Override
    public boolean contains(E e) {
        if (e != null) {
            UnsafeReferenceArray<E> array = this.array;
            int head = this.head;

            for (int i = 0; i < head; i++)
                if (e.equals(array.getVolatile(i)))
                    return true;
        }

        return false;
    }

    @Override
    public void visit(Visitor<E> visitor) {
        Objects.requireNonNull(visitor);

        UnsafeReferenceArray<E> array = this.array;
        int head = this.head;

        for (int i = 0; i < head; i++) {
            E e = array.getVolatile(i);
            if (e != null) {
                boolean stop = !visitor.visit(e);
                if (stop)
                    break;
            }
        }
    }

    private void add(int index, int head, E e, UnsafeReferenceArray<E> array) {
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

    private int findFreeCellWithoutDuplicate(int head, E e, UnsafeReferenceArray<E> array) {
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

}
