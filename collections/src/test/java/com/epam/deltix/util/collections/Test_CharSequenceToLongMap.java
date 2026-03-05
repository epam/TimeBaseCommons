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

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Generated
 */
public class Test_CharSequenceToLongMap {
    private final StringBuilder sb = new StringBuilder();

    private CharSequence wrap(String text) {
        sb.setLength(0);
        sb.append(text);
        return sb;
    }

    // ===================== Construction =====================

    @Test
    public void defaultConstructor() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void constructorWithCapacity() {
        CharSequenceToLongMap map = new CharSequenceToLongMap(64);
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    // ===================== put / get basics =====================

    @Test
    public void putNewKeyReturnsTrue() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        assertTrue(map.put(wrap("alpha"), 1L));
    }

    @Test
    public void putExistingKeyReturnsFalse() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        assertTrue(map.put(wrap("alpha"), 1L));
        assertFalse(map.put(wrap("alpha"), 2L));
    }

    @Test
    public void getReturnsStoredValue() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        map.put(wrap("key"), 42L);
        assertEquals(42L, map.get(wrap("key"), -1L));
    }

    @Test
    public void getReturnsNotFoundValueForAbsentKey() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        assertEquals(-999L, map.get(wrap("missing"), -999L));
    }

    @Test
    public void putOverwritesValue() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        map.put(wrap("k"), 10L);
        map.put(wrap("k"), 20L);
        assertEquals(20L, map.get(wrap("k"), -1L));
    }

    // ===================== containsKey =====================

    @Test
    public void containsKeyReturnsTrueForPresent() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        map.put(wrap("x"), 1L);
        assertTrue(map.containsKey(wrap("x")));
    }

    @Test
    public void containsKeyReturnsFalseForAbsent() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        assertFalse(map.containsKey(wrap("x")));
    }

    // ===================== remove =====================

    @Test
    public void removeReturnsOldValue() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        map.put(wrap("r"), 77L);
        assertEquals(77L, map.remove(wrap("r"), -1L));
    }

    @Test
    public void removeDecreasesSize() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        map.put(wrap("a"), 1L);
        map.put(wrap("b"), 2L);
        assertEquals(2, map.size());

        map.remove(wrap("a"), -1L);
        assertEquals(1, map.size());
        assertFalse(map.containsKey(wrap("a")));
    }

    @Test
    public void removeAbsentKeyReturnsNotFoundValue() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        assertEquals(-1L, map.remove(wrap("nope"), -1L));
    }

    // ===================== size / isEmpty / clear =====================

    @Test
    public void sizeGrowsWithInserts() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        map.put(wrap("a"), 1L);
        assertEquals(1, map.size());
        map.put(wrap("b"), 2L);
        assertEquals(2, map.size());
        map.put(wrap("a"), 3L); // overwrite, size stays
        assertEquals(2, map.size());
    }

    @Test
    public void clearResetsMap() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        map.put(wrap("a"), 1L);
        map.put(wrap("b"), 2L);
        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertFalse(map.containsKey(wrap("a")));
    }

    // ===================== Range-based operations =====================

    @Test
    public void putAndGetWithRange() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        String source = "<<hello>>";
        map.put(source, 2, 7, 100L);

        assertEquals(100L, map.get(wrap("hello"), -1L));
    }

    @Test
    public void getWithRange() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        map.put(wrap("world"), 55L);

        String source = "xxworldyy";
        assertEquals(55L, map.get(source, 2, 7, -1L));
    }

    @Test
    public void containsKeyWithRange() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        map.put(wrap("test"), 1L);

        assertTrue(map.containsKey("xxtestxx", 2, 6));
        assertFalse(map.containsKey("xxtestxx", 0, 4));
    }

    @Test
    public void removeWithRange() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        map.put(wrap("del"), 99L);

        assertEquals(99L, map.remove("xxdelxx", 2, 5, -1L));
        assertFalse(map.containsKey(wrap("del")));
    }

    @Test
    public void removeWithRangeAbsentKey() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        assertEquals(-1L, map.remove("xxdelxx", 2, 5, -1L));
    }

    // ===================== putIfEmpty / putAndGet =====================

    @Test
    public void putIfEmptyOnlyInsertsOnce() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        assertTrue(map.putIfEmpty(wrap("k"), 10L));
        assertFalse(map.putIfEmpty(wrap("k"), 20L));
        assertEquals(10L, map.get(wrap("k"), -1L));
    }

    @Test
    public void putAndGetReturnsOldValue() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        assertEquals(-1L, map.putAndGet(wrap("k"), 10L, -1L));
        assertEquals(10L, map.putAndGet(wrap("k"), 20L, -1L));
        assertEquals(20L, map.get(wrap("k"), -1L));
    }

    @Test
    public void putAndGetIfEmptyReturnsExistingValue() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        assertEquals(10L, map.putAndGetIfEmpty(wrap("k"), 10L));
        assertEquals(10L, map.putAndGetIfEmpty(wrap("k"), 20L));
    }

    // ===================== containsValue =====================

    @Test
    public void containsValueFindsValue() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        map.put(wrap("a"), 42L);
        assertTrue(map.containsValue(42L));
        assertFalse(map.containsValue(99L));
    }

    // ===================== String vs StringBuilder keys =====================

    @Test
    public void stringAndStringBuilderKeysAreEquivalent() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        map.put(wrap("foo"), 7L);

        assertEquals(7L, map.get("foo", -1L));
        assertTrue(map.containsKey("foo"));
    }

    // ===================== Long-specific value range =====================

    @Test
    public void longExtremeValues() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        map.put(wrap("min"), Long.MIN_VALUE);
        map.put(wrap("max"), Long.MAX_VALUE);
        map.put(wrap("zero"), 0L);
        map.put(wrap("big"), 3_000_000_000L);

        assertEquals(Long.MIN_VALUE, map.get(wrap("min"), 0L));
        assertEquals(Long.MAX_VALUE, map.get(wrap("max"), 0L));
        assertEquals(0L, map.get(wrap("zero"), -1L));
        assertEquals(3_000_000_000L, map.get(wrap("big"), -1L));
    }

    // ===================== Many entries (growth) =====================

    @Test
    public void largeMapStressTest() {
        int n = 5000;
        Map<String, Long> reference = new HashMap<>(n);
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        Random rnd = new Random(2026);

        for (int i = 0; i < n; i++) {
            long u = rnd.nextLong() & Long.MAX_VALUE;
            sb.setLength(0);
            long tmp = u;
            while (tmp != 0) {
                sb.append((char) ('A' + (tmp % 26)));
                tmp = tmp / 26;
            }
            if (sb.length() == 0)
                sb.append('A');

            map.put(sb, u);
            reference.put(sb.toString(), u);
        }

        assertEquals(reference.size(), map.size());

        for (Map.Entry<String, Long> e : reference.entrySet()) {
            long expected = e.getValue();
            assertTrue(map.containsKey(e.getKey()));
            assertEquals(expected, map.get(e.getKey(), expected - 1));
        }

        for (String key : reference.keySet()) {
            long old = reference.get(key);
            assertEquals(old, map.remove(key, old - 1));
        }

        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    // ===================== Edge cases =====================

    @Test
    public void emptyStringKey() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        map.put(wrap(""), 0L);
        assertEquals(0L, map.get(wrap(""), -1L));
        assertTrue(map.containsKey(wrap("")));
    }

    @Test
    public void reinsertAfterRemove() {
        CharSequenceToLongMap map = new CharSequenceToLongMap();
        map.put(wrap("temp"), 1L);
        map.remove(wrap("temp"), -1L);
        assertFalse(map.containsKey(wrap("temp")));

        assertTrue(map.put(wrap("temp"), 2L));
        assertEquals(2L, map.get(wrap("temp"), -1L));
    }
}
