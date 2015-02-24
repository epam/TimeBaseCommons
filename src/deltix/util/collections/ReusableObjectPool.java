package deltix.util.collections;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import deltix.util.lang.Disposable;
import deltix.util.lang.Factory;
import deltix.util.lang.Util;

/**
 * Reusable object pool.
 * Non-Thread Safe.
 */
public abstract class ReusableObjectPool<T> implements Disposable {
    private final List<T> freeItems = new ArrayList<>();
    private int lastItem;

    public ReusableObjectPool(int initialSize) {
        for (int i = 0; i < initialSize; i++)
            freeItems.add(createItem());

        lastItem = freeItems.size() - 1;
    }

    public static <I> ReusableObjectPool<I> create(final Factory<I> factory, int initialSize) {
        return new ReusableObjectPool<I>(initialSize) {
            @Override
            protected I createItem() {
                return factory.create();
            }
        };
    }

    public static <I> ReusableObjectPool<I> synchronizedPool(ReusableObjectPool<I> pool) {
        return new SynchronizedPool<>(pool);
    }

    public T borrow() {
        return (lastItem < 0) ? createItem() : freeItems.get(lastItem--);
    }

    public void release(T item) {
        if (++lastItem >= freeItems.size()) {
            freeItems.add(item);
        } else {
            freeItems.set(lastItem, item);
        }
    }

    @Override
    public void close() {
        // close items in the pool and make the pool unusable
        for (int i = lastItem; i >= 0; i--) {
            final Object item = freeItems.get(i);
            if (item instanceof Closeable)
                Util.close((Closeable) item);
        }
        freeItems.clear();
    }

    protected abstract T createItem();

    //////////////////////// HELPER CLASSES ///////////////////////

    private static final class SynchronizedPool<T> extends ReusableObjectPool<T> {
        private final ReusableObjectPool<T> pool;

        public SynchronizedPool(ReusableObjectPool<T> pool) {
            super(0);
            this.pool = pool;
        }

        @Override
        public synchronized T borrow() {
            return pool.borrow();
        }

        @Override
        public synchronized void release(T item) {
            pool.release(item);
        }

        @Override
        public synchronized void close() {
            pool.close();
        }

        @Override
        protected T createItem() {
            return pool.createItem();
        }
    }
}
