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