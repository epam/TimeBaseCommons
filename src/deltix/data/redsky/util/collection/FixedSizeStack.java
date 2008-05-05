package deltix.data.redsky.util.collection;

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

}
