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

import com.epam.deltix.util.collections.generated.ObjectToLongHashMap;
import com.epam.deltix.util.collections.hash.ObjHashCodeComputer;
import com.epam.deltix.util.lang.Util;

import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

/**
 * Reference based map. Caller should care about immutability of keys.
 */
public class CharSeqToLongMap<K extends CharSequence> extends ObjectToLongHashMap<K> {

    protected final BiPredicate<? super K, ? super K> equalityFunction;

    public CharSeqToLongMap() {
        this(MIN_CAPACITY);
    }

    public CharSeqToLongMap(final int initialCapacity) {
        this(initialCapacity, Util::hashCode, Util::equals);
    }

    public CharSeqToLongMap(final int initialCapacity,
                            final ToIntFunction<? super K> hashFunction,
                            final BiPredicate<? super K, ? super K> equalityFunction) {

        super(initialCapacity, new ObjHashCodeComputer<>(hashFunction));

        this.equalityFunction = equalityFunction;
    }

    @Override
    protected boolean keyEquals(final K a, final K b) {
        return equalityFunction.test(a, b);
    }

}