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