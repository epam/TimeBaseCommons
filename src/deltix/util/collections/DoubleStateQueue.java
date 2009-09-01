package deltix.util.collections;

/**
 *
 * @author PaharelauK
 */
public interface DoubleStateQueue<E> {

    void addEmptyElement(E e);

    E getEmptyElement() throws InterruptedException;

    void addReadyElement(E e);

    E getReadyElement() throws InterruptedException;
    
    int capacity();
    
    int getCountReadyElements();

    int getCountEmptyElements();

    /** Factory method to fill empty elements pool at creation time */
    E newEmptyElement();

}
