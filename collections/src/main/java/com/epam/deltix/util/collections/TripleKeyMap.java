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

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Collections;

/**
 * Triple keys map.
 */
public class TripleKeyMap<OK, MK, IK, V> {
    @SuppressWarnings("unchecked")
    private static final InternalMapFactory SIMPLE_FACTORY = new ClassMapFactory(null, null, null);

    private final InternalMapFactory<OK, MK, IK, V> factory;
    private final Map<OK, Map<MK, Map<IK, V>>> map;

    public static <OK, MK, IK, V> TripleKeyMap<OK, MK, IK, V> create() {
        return new TripleKeyMap<OK,MK,IK,V>();
    }

    public static <OK, MK, IK, V> TripleKeyMap<OK, MK, IK, V> create(Class<? extends OK> outerKeyClass,
                                                                     Class<? extends MK> middleKeyClass,
                                                                     Class<? extends IK> innerKeyClass) {
        return new TripleKeyMap<OK,MK,IK,V>(outerKeyClass, middleKeyClass, innerKeyClass);
    }

    @SuppressWarnings("unchecked")
    public TripleKeyMap() {
        this(SIMPLE_FACTORY);
    }

    public TripleKeyMap(Class<? extends OK> outerKeyClass,
                        Class<? extends MK> middleKeyClass,
                        Class<? extends IK> innerKeyClass) {
        this(new ClassMapFactory<OK,MK,IK,V>(outerKeyClass, middleKeyClass, innerKeyClass));
    }

    public TripleKeyMap(InternalMapFactory<OK, MK, IK, V> factory) {
        this.factory = factory;
        map = factory.createOuterMap();
    }

    /////////////////////////// METHODS /////////////////////////

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public int size() {
        return map.size();
    }

    public void clear() {
        map.clear();
    }

    public boolean containsKey(OK outerKey, MK middleKey, IK innerKey) {
        Map<MK, Map<IK, V>> middleMap = map.get(outerKey);
        if (middleMap != null) {
            Map<IK, V> innerMap = middleMap.get(middleKey);
            if (innerMap != null)
                return innerMap.containsKey(innerKey);
        }
        return false;
    }

    public V get(OK outerKey, MK middleKey, IK innerKey) {
        Map<MK, Map<IK, V>> middleMap = map.get(outerKey);
        if (middleMap != null) {
            Map<IK, V> innerMap = middleMap.get(middleKey);
            if (innerMap != null)
                return innerMap.get(innerKey);
        }
        return null;
    }

    public V put(OK outerKey, MK middleKey, IK innerKey, V value) {
        Map<MK, Map<IK, V>> middleMap = map.get(outerKey);
        if (middleMap == null) {
            middleMap = factory.createMiddleMap();
            map.put(outerKey, middleMap);
        }
        Map<IK, V> innerMap = middleMap.get(middleKey);
        if (innerMap == null) {
            innerMap = factory.createInnerMap();
            middleMap.put(middleKey, innerMap);
        }
        return innerMap.put(innerKey, value);
    }

    public V remove(OK outerKey, MK middleKey, IK innerKey) {
        V result = null;
        Map<MK, Map<IK, V>> middleMap = map.get(outerKey);
        if (middleMap != null) {
            Map<IK, V> innerMap = middleMap.get(middleKey);
            if (innerMap != null) {
                result = innerMap.remove(innerKey);

                if (innerMap.isEmpty())
                    middleMap.remove(middleKey);
            }
            if (middleMap.isEmpty())
                map.remove(outerKey);
        }
        return result;
    }

    public Set<OK> outerKeySet() {
        return map.keySet();
    }

    public Set<MK> middleKeySet(OK outerKey) {
        Map<MK, Map<IK, V>> middleMap = map.get(outerKey);
        return middleMap != null ? middleMap.keySet() : Collections.<MK>emptySet();
    }

    public Set<IK> innerKeySet(OK outerKey, MK middleKey) {
        Map<MK, Map<IK, V>> middleMap = map.get(outerKey);
        if (middleMap != null) {
            Map<IK, V> innerMap = middleMap.get(middleKey);
            return innerMap != null ? innerMap.keySet() : Collections.<IK>emptySet();
        }
        return Collections.emptySet();
    }

    ////////////////////////////// UTILITIES /////////////////////////

    @SuppressWarnings("unchecked")
    private static <K, V> Map<K, V> create(Class<? extends K> keyClass) {
        if (keyClass == null)
            return new HashMap<K, V>();

        if (keyClass.isEnum())
            return new EnumMap(keyClass);
        if (keyClass.isAssignableFrom(CharSequence.class))
            return new CharSequenceToObjectMap();
        return new HashMap<K, V>();
    }

    ////////////////////////////// HELPER CLASSES /////////////////////////

    public static interface InternalMapFactory<OK, MK, IK, V> {
        Map<OK, Map<MK, Map<IK, V>>> createOuterMap();
        Map<MK, Map<IK, V>> createMiddleMap();
        Map<IK, V> createInnerMap();
    }

    public static class ClassMapFactory<OK, MK, IK, V> implements InternalMapFactory<OK, MK, IK, V> {
        private final Class<? extends OK> outerKeyClass;
        private final Class<? extends MK> middleKeyClass;
        private final Class<? extends IK> innerKeyClass;

        public ClassMapFactory(Class<? extends OK> outerKeyClass,
                               Class<? extends MK> middleKeyClass,
                               Class<? extends IK> innerKeyClass) {
            this.outerKeyClass = outerKeyClass;
            this.middleKeyClass = middleKeyClass;
            this.innerKeyClass = innerKeyClass;
        }

        @Override
        public Map<OK, Map<MK, Map<IK, V>>> createOuterMap() {
            return create(outerKeyClass);
        }

        @Override
        public Map<MK, Map<IK, V>> createMiddleMap() {
            return create(middleKeyClass);
        }

        @Override
        public Map<IK, V> createInnerMap() {
            return create(innerKeyClass);
        }
    }
}