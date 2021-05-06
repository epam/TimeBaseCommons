package com.epam.deltix.util.collections;

import com.epam.deltix.util.collections.generated.ObjectToObjectHashMap;
import com.epam.deltix.util.collections.hash.ObjHashCodeComputer;
import com.epam.deltix.util.lang.Util;

import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

/**
 * Reference based map. Caller should care about immutability of keys.
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

        super(initialCapacity, new ObjHashCodeComputer<>(hashFunction));

        this.equalityFunction = equalityFunction;
    }

    @Override
    protected boolean keyEquals(final K a, final K b) {
        return equalityFunction.test(a, b);
    }

}
