package com.epam.deltix.util.collections.hash;

import java.util.function.ToIntFunction;


public final class ObjHashCodeComputer<T> extends HashCodeComputer {

    private final ToIntFunction<T> hashFunction;

    public ObjHashCodeComputer(final ToIntFunction<T> hashFunction) {
        this.hashFunction = hashFunction;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int modHashCode(final Object key, final int mod) {
        return modHashCode(hashFunction.applyAsInt((T) key), mod);
    }

}
