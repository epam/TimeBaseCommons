package deltix.util.collections;

public interface StringKeyAttributeMap<V> {
    boolean contains(CharSequence key);

    V put(CharSequence key, V value);

    V remove(CharSequence key);

    V get(CharSequence key);

    int size();

    void visit(StringKeyVisitor<V> visitor);

    //////////////////////////// HELPER CLASSES //////////////////////////

    public static interface StringKeyVisitor<V> {
        boolean visit(CharSequence key, V value);
    }
}
