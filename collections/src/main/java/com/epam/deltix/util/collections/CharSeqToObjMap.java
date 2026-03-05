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

import com.epam.deltix.util.collections.generated.ObjectToObjectHashMap;
import com.epam.deltix.util.collections.hash.ObjHashCodeComputer;
import com.epam.deltix.util.lang.Util;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

/**
 * Reference based map. Caller should care about immutability of keys.
 * <p>
 * This map is recommended for cases when you set of stored keys changes over time (keys get removed and added over time).
 * Needs extra care when keys are mutable (common case).
 * <p>
 * Pros:
 * <ul>
 *     <li>No implicit allocation on key insertion (except map resize) or key lookup</li>
 * </ul>
 * <p>
 * Cons:
 * <ul>
 *     <li>User have to care about immutability of keys. Either by converting them to immutable or by pooling them</li>
 *     <li>Default {@link #equalityFunction} may be suboptimal with keys different from {@link String} and {@link StringBuilder}</li>
 * </ul>
 * <p>
 * If you use classes different from {@link String} and {@link StringBuilder} as keys,
 * it is strongly recommended to provide custom {@link #equalityFunction} and {@link ObjHashCodeComputer}
 * with dedicated logic for actually used key type(s) to get better performance.
 */
public class CharSeqToObjMap<K extends CharSequence, V> extends ObjectToObjectHashMap<K, V> {

    protected final BiPredicate<? super K, ? super K> equalityFunction;

    public CharSeqToObjMap() {
        this(MIN_CAPACITY);
    }

    public CharSeqToObjMap(final int initialCapacity) {
        this(initialCapacity, Util::hashCode, Util::equals);
    }

    public CharSeqToObjMap(final int initialCapacity,
                           final ToIntFunction<? super K> hashFunction,
                           final BiPredicate<? super K, ? super K> equalityFunction) {
        this(initialCapacity, new ObjHashCodeComputer<>(hashFunction), equalityFunction);
    }

    public CharSeqToObjMap(final int initialCapacity,
                           final ObjHashCodeComputer<? super K> hashCodeComputer,
                           final BiPredicate<? super K, ? super K> equalityFunction) {
        super(initialCapacity, hashCodeComputer);

        this.equalityFunction = equalityFunction;
    }

    @Override
    protected boolean keyEquals(final K a, final K b) {
        return equalityFunction.test(a, b);
    }

    /** Iterate over key-value pairs */
    @SuppressWarnings("unchecked")
    public void forEach(BiConsumer<K, ? super V> consumer) {
        for (int i = 0; i < values.length; i++) {
            if (isFilled (i)) {
                // Stored keys are always String
                consumer.accept((K) keys[i], (V) values[i]);
            }
        }
    }
}