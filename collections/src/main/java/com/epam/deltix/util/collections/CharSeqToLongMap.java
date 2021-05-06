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
