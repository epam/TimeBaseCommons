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

    public void addEmptyElement(E e) throws InterruptedException {
        mEmptyElements.put(e);
    }

    public void addReadyElement(E e) throws InterruptedException {
        mReadyElements.put(e);
    }

    public E getEmptyElement() throws InterruptedException {
        return mEmptyElements.take();
    }

    public E getReadyElement() throws InterruptedException {
        return mReadyElements.take();
    }
}
