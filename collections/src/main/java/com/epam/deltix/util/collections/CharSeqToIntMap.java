package com.epam.deltix.util.collections;

import com.epam.deltix.util.collections.generated.ObjectToIntegerHashMap;
import com.epam.deltix.util.collections.hash.ObjHashCodeComputer;
import com.epam.deltix.util.lang.Util;

import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

/**
 * Reference based map. Caller should care about immutability of keys.
 */
public class CharSeqToIntMap<K extends CharSequence> extends ObjectToIntegerHashMap<K> {

    protected final BiPredicate<? super K, ? super K> equalityFunction;

    public CharSeqToIntMap() {
        this(MIN_CAPACITY);
    }

    public CharSeqToIntMap(final int initialCapacity) {
        this(initialCapacity, Util::hashCode, Util::equals);
    }

    public CharSeqToIntMap(final int initialCapacity,
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
