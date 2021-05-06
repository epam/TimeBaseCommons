package com.epam.deltix.util.collections;

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

    /** Bluk version of getEmptyElement()
    * @param count maximum number of immediately available elements to retrieve
    * @param result container of empty elements (must have at least 'count' capacity)
    * @return number of elements returned, greater or equal than 1
    * @throws InterruptedException
    */
   int getEmptyElements(int count, E[] result) throws InterruptedException;

    /** Bluk version of getReadyElement()
    * @param count maximum number of immediately available elements to retrieve
    * @param result container of ready elements (must have at least 'count' capacity)
    * @return number of elements returned, greater or equal than 1
    * @throws InterruptedException
    */
   int getReadyElements(int count, E[] result) throws InterruptedException;

   /** Bulk version of addReadyElement */
   void addReadyElements(int count, E[] elems);
    
    /** Bulk version of addReadyElement */
   void addEmptyElements(int count, E[] elems);

   void clearUnsafe();
}
