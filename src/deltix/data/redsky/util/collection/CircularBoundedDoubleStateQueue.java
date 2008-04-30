package deltix.data.redsky.util.collection;

/**
 *
 * @author PaharelauK
 */
public class CircularBoundedDoubleStateQueue<E> implements DoubleStateQueue<E> {

    private final CircularBoundedQueue<E> mEmptyElements;
    private final CircularBoundedQueue<E> mReadyElements;

    public CircularBoundedDoubleStateQueue(int elementCount) {
        mEmptyElements = new CircularBoundedQueue<E>(elementCount);
        mReadyElements = new CircularBoundedQueue<E>(elementCount);
    }

    public int capacity() {
        assert mEmptyElements.count() == mReadyElements.count();
        return mEmptyElements.count();
    }
    
    public final void addEmptyElement(E e) {
        synchronized (mEmptyElements) {
            mEmptyElements.add(e);
            mEmptyElements.notify();
        }
    }

    public final void addReadyElement(E e) {
        synchronized (mReadyElements) {
            mReadyElements.add(e);
            mReadyElements.notify();
        }
    }

    public final E getEmptyElement() throws InterruptedException {
        synchronized (mEmptyElements) {
            try {
                while (mEmptyElements.count() == 0) {
                    mEmptyElements.wait();
                }
            } catch (InterruptedException ie) {
                notifyAll();
                throw ie;
            }
            return mEmptyElements.remove();
        }
    }
    
    public final int getCountReadyElements() {
        synchronized (mReadyElements) {
            return mReadyElements.count();
        }
    }

    public final int getCountEmptyElements() {
        synchronized (mEmptyElements) {
            return mEmptyElements.count();
        }
    }

    public final E getReadyElement() throws InterruptedException { 
        synchronized (mReadyElements) {
            try {
                while (mReadyElements.count() == 0) {
                    mReadyElements.wait();
                }
            } catch (InterruptedException ie) {
                notifyAll();
                throw ie;
            }
            return mReadyElements.remove();
        }
    }
}
