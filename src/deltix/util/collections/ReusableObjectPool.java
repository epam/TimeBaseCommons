package deltix.util.collections;

import java.util.ArrayList;
import java.util.List;

public class ReusableObjectPool<T> {

    private final Object lock = new Object();
    private final List<T> freeItems = new ArrayList<T>();
    private final ItemFactory<T> factory;
    private int lastItem;

    public ReusableObjectPool(ItemFactory<T> factory) {
        this(factory, 256);
    }

    public ReusableObjectPool(ItemFactory<T> factory, int initialSize) {
        this.factory = factory;

        for (int i = 0; i < initialSize; i++) {
            freeItems.add(factory.createItem());
        }
        
        lastItem = freeItems.size() - 1;
    }

    public T get() {
        synchronized (lock) {
            return (lastItem < 0) ? factory.createItem() : freeItems.get(lastItem--);
        }
    }

    public void release(T item) {
        synchronized (lock) {
            if (++lastItem >= freeItems.size()) {
                freeItems.add(item);
            } else {
                freeItems.set(lastItem, item);
            }
        }
    }

    public interface ItemFactory<T> {
        T createItem();
    }
    
}
