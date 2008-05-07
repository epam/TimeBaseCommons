package deltix.data.redsky.util.collection;


/**
 *
 * @author PaharelauK
 */
public abstract class CircularBoundedDoubleStateQueue<E> implements DoubleStateQueue<E> {

    private final FixedSizeStack<E> mEmptyElements;
    private final CircularBoundedQueue<E> mReadyElements;
    private final boolean mCannibalizeStaleReadyElements = true;
    
    public CircularBoundedDoubleStateQueue(final int elementCount) {
        mReadyElements = new CircularBoundedQueue<E>(elementCount);
        mEmptyElements = new FixedSizeStack<E>(elementCount); // empty elements have no order, stack is faster than queue
        for (int idx = 0; idx < elementCount; idx++) {
            mEmptyElements.add(newEmptyElement());
        }   
        
    }

    public int capacity() {
        return mEmptyElements.capacity();
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
                    if (mCannibalizeStaleReadyElements) {
                        assert getCountReadyElements() > 0; // implied: when mEmptyElementsis empty, mReadyElements must have at least capacity-numChannels elements
                        return getReadyElement();
                    }
                    
                    mEmptyElements.wait();
                }
            } catch (InterruptedException ie) {
                notifyAll();
                throw ie;
            }
            return mEmptyElements.remove();
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

    public final int getCountReadyElements() {
        synchronized (mReadyElements) {
            return mReadyElements.count();
        }
    }

    public final int getCountEmptyElements() {
        synchronized (mEmptyElements) {
            return mEmptyElements.count();
        }
    }}
