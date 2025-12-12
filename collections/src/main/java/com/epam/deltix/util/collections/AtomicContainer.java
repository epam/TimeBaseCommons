package com.epam.deltix.util.collections;


public interface AtomicContainer<E> {

    /**
     * Added not nullable element in container
     */
    void add(E e);

    /**
     * Added not nullable element in container if it is not there
     * @return true if added
     */
    boolean addIfAbsent(E e);

    /**
     * @param e
     * @return true if e was in container
     */
    boolean remove(E e);

    int size();

    /**
     * @param index
     * @return element at index or null if absent
     */
    E get(int index);

    /**
     * @return true if e was found
     */
    boolean contains(E e);

    void visit(Visitor<E> visitor);

}
