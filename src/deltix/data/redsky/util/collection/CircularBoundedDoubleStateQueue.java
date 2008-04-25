/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
                while (mEmptyElements.size() == 0) {
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
                while (mReadyElements.size() == 0) {
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
