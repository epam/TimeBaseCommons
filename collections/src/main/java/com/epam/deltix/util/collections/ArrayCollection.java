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

import java.util.*;

/**
 *  Adapts array to Collection API.
 */
public class ArrayCollection <T> extends AbstractCollection <T> {
    private final T []        array;

    @SafeVarargs
    public ArrayCollection (T ... array) {
        this.array = array;
    }

    @Override
    public boolean          addAll (Collection<? extends T> c) {
        throw new UnsupportedOperationException ("Immutable.");
    }

    @Override
    public void             clear () {
        throw new UnsupportedOperationException ("Immutable.");
    }

    public Iterator <T>     iterator () {
        return (new ArrayIterator<T> (array));
    }

    @Override
    public boolean          remove (Object o) {
        throw new UnsupportedOperationException ("Immutable.");
    }

    @Override
    public boolean          removeAll (Collection <?> c) {
        throw new UnsupportedOperationException ("Immutable.");
    }

    @Override
    public boolean          retainAll (Collection<?> c) {
        throw new UnsupportedOperationException ("Immutable.");
    }

    public int              size () {
        return (array.length);
    }

    @Override
    public Object []        toArray () {
        return (array.clone ());
    }
}