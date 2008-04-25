package deltix.data.redsky.util.collection;

/**
 *
 * @author PaharelauK
 */
public interface DoubleStateQueue<E> {

    void addEmptyElement(E e);

    E getEmptyElement() throws InterruptedException;

    void addReadyElement(E e);

    E getReadyElement() throws InterruptedException;
}
