package com.epam.deltix.util.collections;

public final class FixedSizeStack<E> {

    private final Object [] elements;
    private int count;
    
    public FixedSizeStack (int capacity) {
        elements = new Object [capacity];
    }
    
    
    public void add (E e) {
        elements[count++] = e;
    }
    
    @SuppressWarnings("unchecked")
    public E remove () {
        assert count > 0;
        return (E) elements [--count];
    }
    
    public int count () {
        return count;
    }
    
    public int capacity () {
        return elements.length;
    }

    public boolean isEmpty () {
        return count == 0;
    }

    public boolean isFull () {
        return count == elements.length;
    }
}
