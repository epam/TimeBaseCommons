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


import com.epam.deltix.util.collections.generated.ObjectArrayList;

/**
 *  Simple Pool of objects. Is Not Thread-Safe.
 */
public class ObjectPool<T> {

    private final ObjectArrayList<T> pool;
    private int availIdx = 0;

    public ObjectPool() {
        this(5);
    }

    public ObjectPool(int capacity) {
        pool = new ObjectArrayList<>(capacity);
    }

    public final T          borrow() {
        if (availIdx < pool.size())
            return pool.get(availIdx++);

        return null;
    }

    public void             add(T used) {

        if (availIdx < pool.size())
            pool.insert(availIdx++, 1, used);
        else
            pool.add(used);
    }

    public final void       reset() {
        availIdx = 0;
    }

    public final void       clear() {
        pool.clear();
    }
}