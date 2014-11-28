package deltix.util.collections;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import deltix.util.lang.Util;

public class ReusableObjectPool<T> implements Closeable {

    private final List<T>           freeItems = new ArrayList<>();
    private final ItemFactory<T>    factory;
    private int                     lastItem;

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

    public synchronized T get() {
        return (lastItem < 0) ? factory.createItem() : freeItems.get(lastItem--);
    }

    public synchronized void release(T item) {
        if (++lastItem >= freeItems.size()) {
            freeItems.add(item);
        } else {
            freeItems.set(lastItem, item);
        }
    }

    @Override
    public synchronized void close() throws IOException {
        // close items in the pool and make the pool unusable
        for (int i = lastItem; i >= 0; i--) {
            final Object item = freeItems.get(i);
            if (item instanceof Closeable) {
                Util.close((Closeable) item);
            }
        }
        freeItems.clear();
    }
    
    public interface ItemFactory<T> {
        T createItem();
    }    
}
