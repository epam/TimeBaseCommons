/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.epam.deltix.util.collections;

/**
 *
 * @author PaharelauK
 */
public interface CircularQueue<E> {

    /**
     * Inserts the specified element into this queue if it is possible to do so
     * immediately without violating capacity restrictions, returning
     * <code>true</code> upon success and throwing an <code>IllegalStateException</code>
     * if no space is currently available.
     *
     * <p>This implementation returns <code>true</code> if <code>offer</code> succeeds,
     * else throws an <code>IllegalStateException</code>.</p>
     *
     * @param e the element to add
     * @return <code>true</code> (as specified by {@link java.util.Collection#add})
     * @throws IllegalStateException if the element cannot be added at this
     *         time due to capacity restrictions
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null and
     *         this queue does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     *         prevents it from being added to this queue
     */
    public boolean add(E e) ;

    public E remove();

}
