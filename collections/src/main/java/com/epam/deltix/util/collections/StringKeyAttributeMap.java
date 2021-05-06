package com.epam.deltix.util.collections;

public interface StringKeyAttributeMap<V> {
    boolean contains(CharSequence key);

    V put(CharSequence key, V value);

    V remove(CharSequence key);

    V get(CharSequence key);

    int size();

    void clear();

    void visit(StringKeyVisitor<V> visitor);

    //////////////////////////// HELPER CLASSES //////////////////////////

    interface StringKeyVisitor<V> {
        /** @return false to stop iterating over map */
        boolean visit(CharSequence key, V value);
    }
}
