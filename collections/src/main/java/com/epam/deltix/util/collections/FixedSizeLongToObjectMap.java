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

import com.epam.deltix.util.collections.generated.LongHashMapBase;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Special version of LongToObjectMap that keeps maximum capacity and doesn't support explicit removal.
 * Instead oldest entries are removed to provide space for new additions.
 */
public class FixedSizeLongToObjectMap<V> {
    private final LimitedLong2ObjectHashMap<V> map;

    public interface OnDeleteCallback<V> {
        void itemDeleted(V item);
    }

    public FixedSizeLongToObjectMap(int maxSize, OnDeleteCallback<V> itemDeleteCallback) {
        map = new LimitedLong2ObjectHashMap<>(maxSize, itemDeleteCallback);
    }

    public V get(long key) {
        return map.get(key);
    }

    public V putIfEmpty(long key, V value) {
        return map.putAndGetIfEmpty(key, value);
    }

    public void clear() {
        map.clear();
    }

    /** (Test only) WARNING: This method is slow and allocates new ArrayList on each call */
    Iterator<V> iterator() {
        return map.iterator();
    }


    /// Hides access to Map itself to avoid accidental access to base class functionality like .remove().
    private static class LimitedLong2ObjectHashMap<V> extends LongHashMapBase {
        private final OnDeleteCallback<V> itemDeleteCallback;
        private final CircularBufferOfInt insertionPoints;
        private Object [] values;

        public LimitedLong2ObjectHashMap(int maxSize, OnDeleteCallback<V> itemDeleteCallback) {
            super(maxSize);
            this.values = new Object [maxSize];
            this.insertionPoints = new CircularBufferOfInt (maxSize);
            this.itemDeleteCallback = itemDeleteCallback;
        }

        @SuppressWarnings("unchecked")
        public V           get (long key) {
            int         pos = find (key);
            return (V)(pos == NULL ? null : values [pos]);
        }

        /**
         *  Put new element into the map only if there no previously stored element under the same key
         *
         *  @param key       The key
         *  @param value     The value
         *  @return  Element that remains in the map. Never null.
         */
        @SuppressWarnings("unchecked")
        public V              putAndGetIfEmpty (long key, V value) {
            int         hidx = hashIndex (key);
            int         idx = find (hidx, key);

            if (idx != NULL) {
                return (V)values [idx];
            }

            if (freeHead == NULL) {
                idx = insertionPoints.tail();  // no more free capacity => evict the oldest entry
                assert idx != CircularBufferOfInt.EMPTY;
                free (idx);
            }

            idx = allocEntry (hidx);

            values [idx] = value;
            keys [idx] = key;
            insertionPoints.add(idx);
            return value;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void free(int idx) {
            super.free(idx);
            if (itemDeleteCallback != null)
                itemDeleteCallback.itemDeleted ((V)values[idx]);
            values[idx] = null;
        }

        @Override
        public void clear() {
            super.clear();
            Arrays.fill(values, null);
            insertionPoints.clear();
        }

        public Iterator<V> iterator() {
            return new MapIterator();
        }

        /** Support iterator used by tests */
        private final class MapIterator implements Iterator<V> {

            private int index = -1;

            @Override
            public boolean hasNext() {

                while (++index < values.length)
                    if (values[index] != null)
                        return true;

                return false;
            }

            @SuppressWarnings("unchecked")
            @Override
            public V next() {
                return (V)values[index];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }

    }

}