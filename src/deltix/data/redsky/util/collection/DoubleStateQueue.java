package deltix.data.redsky.util.collection;

/**
 *
 * @author PaharelauK
 */
public interface DoubleStateQueue<E> {

    void addEmptyElement(E e) throws InterruptedException;

    E getEmptyElement() throws InterruptedException;

    void addReadyElement(E e) throws InterruptedException;

    E getReadyElement() throws InterruptedException;
}
