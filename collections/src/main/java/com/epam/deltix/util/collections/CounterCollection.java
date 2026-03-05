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
 *  Counts added elements, but does not keep track of them
 */
public class CounterCollection <T> implements Collection <T> {
    private int             mCount = 0;
    
    public boolean          add (T o) {
        mCount++;
        return (true);
    }

    public void             clear () {
        mCount = 0;
    }

    public boolean          isEmpty () {
        return (mCount == 0);
    }

    public int              size () {
        return (mCount);
    }

    public boolean          contains (Object o) {
        throw new UnsupportedOperationException ();
    }

    public boolean          remove (Object o) {
        mCount--;
        return (true);
    }

    public <T> T []         toArray (T[] a) {
        throw new UnsupportedOperationException ();
    }

    public boolean          addAll (Collection<? extends T> c) {
        mCount += c.size ();
        return (true);
    }

    public boolean          containsAll (Collection<?> c) {
        throw new UnsupportedOperationException ();
    }

    public boolean          removeAll (Collection<?> c) {
        throw new UnsupportedOperationException ();
    }

    public boolean          retainAll (Collection<?> c) {
        throw new UnsupportedOperationException ();
    }

    public Iterator<T>      iterator () {
        throw new UnsupportedOperationException ();
    }

    public Object[]         toArray () {
        throw new UnsupportedOperationException ();
    }    
}