package com.epam.deltix.util.collections;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Special version of CharSequenceToObjectMap that keeps maximum capacity and doesn't support explicit removal.
 * Instead oldest entries are removed to provide space for new additions.
 */
public class FixedSizeCharSeqToObjectMap<V> {
    private final LimitedCharSeq2ObjectHashMap<V> map;

    public interface OnDeleteCallback<V> {
        void itemDeleted(V item);
    }

    public FixedSizeCharSeqToObjectMap(int maxSize, OnDeleteCallback<V> itemDeleteCallback) {
        if (maxSize < 16)
            throw new IllegalArgumentException("Size is too small");
        map = new LimitedCharSeq2ObjectHashMap<>(maxSize, itemDeleteCallback);
    }

    public V get(CharSequence key) {
        return map.get(key);
    }

    public boolean putIfEmpty(CharSequence key, V value) {
        return map.putIfEmpty(key, value);
    }

    public V putAndGetIfEmpty(CharSequence key, V value) {
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
    private static class LimitedCharSeq2ObjectHashMap<V> extends CharSequenceToObjectMapQuick<V> {
        private final OnDeleteCallback<V> itemDeleteCallback;
        private final CircularBufferOfInt insertionPoints;
        private Object [] values;

        public LimitedCharSeq2ObjectHashMap(int maxSize, OnDeleteCallback<V> itemDeleteCallback) {
            super(maxSize);
            this.values = new Object [maxSize];
            this.insertionPoints = new CircularBufferOfInt (maxSize);
            this.itemDeleteCallback = itemDeleteCallback;
        }

        @SuppressWarnings("unchecked")
        public V           get (CharSequence key) {
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
        @Override
        @SuppressWarnings("unchecked")
        public V              putAndGetIfEmpty (CharSequence key, V value) {
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

        @Override
        public boolean putIfEmpty(final CharSequence key, final V value) {
            int         hidx = hashIndex (key);
            int         idx = find (hidx, key);

            if (idx != NULL) {
                return false;
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
            return true;
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
