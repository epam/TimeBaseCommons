package com.epam.deltix.util.collections;

public interface IntKeyAttributeMap<V> {
    boolean contains(int key);

    V put(int key, V value);

    V remove(int key);

    V get(int key);

    int size();

    void clear();

    void visit(IntKeyVisitor<V> visitor);

    //////////////////////////// HELPER CLASSES //////////////////////////

    public static interface IntKeyVisitor<V> {
        boolean visit(int key, V value);
    }
}
