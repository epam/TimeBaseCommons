package deltix.util.collections;

import deltix.util.collections.generated.ObjectList;

public interface PoolableObjectList<T> extends ObjectList<T> {

    int capacity();

    void clear();

    <Q extends T> void hintCapacity(int capacity, Class<Q> type);

    <Q extends T> void setSize(int count, Class<Q> type);

    <Q extends T> Q append(Class<Q> type);
}
