/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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