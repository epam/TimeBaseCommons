package com.epam.deltix.util.collections;

import java.util.*;

/**
 *  HashMap or Collections.
 */
public class HCMultiMap <K, V> implements MultiMap <K, V> {
    private final HashMap <K, Collection <V>>    map =
        new HashMap <K, Collection <V>> ();

    public void                 clear () {
        map.clear ();
    }

    public Collection <V>       get (K key) {
        return (map.get (key));
    }

    /**
     *  Factory method for new collections.
     *  Default implementation creates an ArrayList.
     *  Override to modify this behavior; for example, to create HasSets.
     *
     *  @return New collection for holding values corresponding to a single key.
     */
    public Collection <V>       newCollection () {
        return (new ArrayList <V> ());
    }

    public Collection <V>       put (K key, V value) {
        Collection <V>      a = map.get (key);

        if (a == null) {
            a = newCollection ();
            map.put (key, a);
        }

        a.add (value);
        return (a);
    }

    public Collection <V>       remove (K key) {
        return (map.remove (key));
    }

    @Override
    public boolean              isEmpty() {
        return map.isEmpty();
    }

    @Override
    public int                  size() {
        return map.size();
    }

    @Override
    public Set<K>               keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Collection<V>> values() {
        return map.values();
    }
}
