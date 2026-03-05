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
public class Test_CharSequenceToIntegerMap {
    private final StringBuilder sb = new StringBuilder();

    private CharSequence wrap(String text) {
        sb.setLength(0);
        sb.append(text);
        return sb;
    }

    // ===================== Construction =====================

    @Test
    public void defaultConstructor() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void constructorWithCapacity() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap(64);
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    // ===================== put / get basics =====================

    @Test
    public void putNewKeyReturnsTrue() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        assertTrue(map.put(wrap("alpha"), 1));
    }

    @Test
    public void putExistingKeyReturnsFalse() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        assertTrue(map.put(wrap("alpha"), 1));
        assertFalse(map.put(wrap("alpha"), 2));
    }

    @Test
    public void getReturnsStoredValue() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        map.put(wrap("key"), 42);
        assertEquals(42, map.get(wrap("key"), -1));
    }

    @Test
    public void getReturnsNotFoundValueForAbsentKey() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        assertEquals(-999, map.get(wrap("missing"), -999));
    }

    @Test
    public void putOverwritesValue() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        map.put(wrap("k"), 10);
        map.put(wrap("k"), 20);
        assertEquals(20, map.get(wrap("k"), -1));
    }

    // ===================== containsKey =====================

    @Test
    public void containsKeyReturnsTrueForPresent() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        map.put(wrap("x"), 1);
        assertTrue(map.containsKey(wrap("x")));
    }

    @Test
    public void containsKeyReturnsFalseForAbsent() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        assertFalse(map.containsKey(wrap("x")));
    }

    // ===================== remove =====================

    @Test
    public void removeReturnsOldValue() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        map.put(wrap("r"), 77);
        assertEquals(77, map.remove(wrap("r"), -1));
    }

    @Test
    public void removeDecreasesSize() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        map.put(wrap("a"), 1);
        map.put(wrap("b"), 2);
        assertEquals(2, map.size());

        map.remove(wrap("a"), -1);
        assertEquals(1, map.size());
        assertFalse(map.containsKey(wrap("a")));
    }

    @Test
    public void removeAbsentKeyReturnsNotFoundValue() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        assertEquals(-1, map.remove(wrap("nope"), -1));
    }

    // ===================== size / isEmpty / clear =====================

    @Test
    public void sizeGrowsWithInserts() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        map.put(wrap("a"), 1);
        assertEquals(1, map.size());
        map.put(wrap("b"), 2);
        assertEquals(2, map.size());
        map.put(wrap("a"), 3); // overwrite, size stays
        assertEquals(2, map.size());
    }

    @Test
    public void clearResetsMap() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        map.put(wrap("a"), 1);
        map.put(wrap("b"), 2);
        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertFalse(map.containsKey(wrap("a")));
    }

    // ===================== Range-based operations =====================

    @Test
    public void putAndGetWithRange() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        String source = "<<hello>>";
        map.put(source, 2, 7, 100);

        assertEquals(100, map.get(wrap("hello"), -1));
    }

    @Test
    public void getWithRange() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        map.put(wrap("world"), 55);

        String source = "xxworldyy";
        assertEquals(55, map.get(source, 2, 7, -1));
    }

    @Test
    public void containsKeyWithRange() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        map.put(wrap("test"), 1);

        assertTrue(map.containsKey("xxtestxx", 2, 6));
        assertFalse(map.containsKey("xxtestxx", 0, 4));
    }

    @Test
    public void removeWithRange() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        map.put(wrap("del"), 99);

        assertEquals(99, map.remove("xxdelxx", 2, 5, -1));
        assertFalse(map.containsKey(wrap("del")));
    }

    @Test
    public void removeWithRangeAbsentKey() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        assertEquals(-1, map.remove("xxdelxx", 2, 5, -1));
    }

    // ===================== putIfEmpty / putAndGet =====================

    @Test
    public void putIfEmptyOnlyInsertsOnce() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        assertTrue(map.putIfEmpty(wrap("k"), 10));
        assertFalse(map.putIfEmpty(wrap("k"), 20));
        assertEquals(10, map.get(wrap("k"), -1));
    }

    @Test
    public void putAndGetReturnsOldValue() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        assertEquals(-1, map.putAndGet(wrap("k"), 10, -1));
        assertEquals(10, map.putAndGet(wrap("k"), 20, -1));
        assertEquals(20, map.get(wrap("k"), -1));
    }

    @Test
    public void putAndGetIfEmptyReturnsExistingValue() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        assertEquals(10, map.putAndGetIfEmpty(wrap("k"), 10));
        assertEquals(10, map.putAndGetIfEmpty(wrap("k"), 20));
    }

    // ===================== containsValue =====================

    @Test
    public void containsValueFindsValue() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        map.put(wrap("a"), 42);
        assertTrue(map.containsValue(42));
        assertFalse(map.containsValue(99));
    }

    // ===================== String vs StringBuilder keys =====================

    @Test
    public void stringAndStringBuilderKeysAreEquivalent() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        map.put(wrap("foo"), 7);

        assertEquals(7, map.get("foo", -1));
        assertTrue(map.containsKey("foo"));
    }

    // ===================== Many entries (growth) =====================

    @Test
    public void largeMapStressTest() {
        int n = 5000;
        Map<String, Integer> reference = new HashMap<>(n);
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        Random rnd = new Random(2026);

        for (int i = 0; i < n; i++) {
            int u = rnd.nextInt(Integer.MAX_VALUE);
            sb.setLength(0);
            while (u != 0) {
                sb.append((char) ('A' + (u % 26)));
                u = u / 26;
            }
            map.put(sb, i);
            reference.put(sb.toString(), i);
        }

        assertEquals(reference.size(), map.size());

        for (Map.Entry<String, Integer> e : reference.entrySet()) {
            int expected = e.getValue();
            assertTrue(map.containsKey(e.getKey()));
            assertEquals(expected, map.get(e.getKey(), expected - 1));
        }

        for (String key : reference.keySet()) {
            int old = reference.get(key);
            assertEquals(old, map.remove(key, old - 1));
        }

        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    // ===================== Edge cases =====================

    @Test
    public void emptyStringKey() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        map.put(wrap(""), 0);
        assertEquals(0, map.get(wrap(""), -1));
        assertTrue(map.containsKey(wrap("")));
    }

    @Test
    public void negativeAndExtremeValues() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        map.put(wrap("min"), Integer.MIN_VALUE);
        map.put(wrap("max"), Integer.MAX_VALUE);
        map.put(wrap("zero"), 0);

        assertEquals(Integer.MIN_VALUE, map.get(wrap("min"), 0));
        assertEquals(Integer.MAX_VALUE, map.get(wrap("max"), 0));
        assertEquals(0, map.get(wrap("zero"), -1));
    }

    @Test
    public void reinsertAfterRemove() {
        CharSequenceToIntegerMap map = new CharSequenceToIntegerMap();
        map.put(wrap("temp"), 1);
        map.remove(wrap("temp"), -1);
        assertFalse(map.containsKey(wrap("temp")));

        assertTrue(map.put(wrap("temp"), 2));
        assertEquals(2, map.get(wrap("temp"), -1));
    }
}
