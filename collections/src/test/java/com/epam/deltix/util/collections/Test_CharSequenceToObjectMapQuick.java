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
import org.junit.Before;

import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Test_CharSequenceToObjectMapQuick {

    private CharSequenceToObjectMapQuick<String> map;
    private StringBuilder sb;

    @Before
    public void setUp() {
        map = new CharSequenceToObjectMapQuick<>();
        sb = new StringBuilder();
    }

    private CharSequence wrap(String text) {
        sb.setLength(0);
        sb.append(text);
        return sb;
    }

    // ==================== Basic Put/Get Tests ====================

    @Test
    public void testPutAndGetWithString() {
        map.put("key1", "value1");
        assertEquals("value1", map.get("key1", null));
    }

    @Test
    public void testPutAndGetWithCharSequence() {
        map.put(wrap("key1"), "value1");
        assertEquals("value1", map.get(wrap("key1"), null));
    }

    @Test
    public void testGetNotFoundReturnsDefault() {
        assertNull(map.get("nonexistent", null));
        assertEquals("default", map.get("nonexistent", "default"));
    }

    @Test
    public void testPutOverwritesExistingValue() {
        map.put("key1", "value1");
        map.put("key1", "value2");
        assertEquals("value2", map.get("key1", null));
        assertEquals(1, map.size());
    }

    @Test
    public void testPutAndGet() {
        assertNull(map.putAndGet(wrap("key1"), "value1", null));
        assertEquals("value1", map.putAndGet(wrap("key1"), "value2", null));
        assertEquals("value2", map.get(wrap("key1"), null));
    }

    // ==================== Substring Access Tests ====================

    @Test
    public void testGetWithSubstring() {
        map.put("hello", "world");
        // "XXhelloYY" with start=2, end=7 should match "hello"
        String source = "XXhelloYY";
        assertEquals("world", map.get(source, 2, 7, null));
    }

    @Test
    public void testGetWithSubstringNotFound() {
        map.put("hello", "world");
        String source = "XXhelXoYY";
        assertNull(map.get(source, 2, 7, null));
        assertEquals("default", map.get(source, 2, 7, "default"));
    }

    @Test
    public void testPutAndGetWithSubstring() {
        String source = "XXhelloYY";
        // Put using substring
        assertNull(map.putAndGet(source, 2, 7, "world", null));
        assertEquals(1, map.size());

        // Get using substring
        assertEquals("world", map.get(source, 2, 7, null));

        // Overwrite using substring
        assertEquals("world", map.putAndGet(source, 2, 7, "newValue", null));
        assertEquals("newValue", map.get(source, 2, 7, null));
        assertEquals(1, map.size());
    }

    @Test
    public void testContainsKeyWithSubstring() {
        map.put("test", "value");
        String source = "PREFIXtestSUFFIX";
        assertTrue(map.containsKey(source, 6, 10));
        assertFalse(map.containsKey(source, 0, 6));
    }

    @Test
    public void testRemoveWithSubstring() {
        map.put("remove", "me");
        String source = "XXremoveYY";

        assertEquals("me", map.remove(source, 2, 8, null));
        assertEquals(0, map.size());
        assertNull(map.get("remove", null));
    }

    @Test
    public void testRemoveWithSubstringNotFound() {
        map.put("keep", "value");
        String source = "XXremoveYY";

        assertNull(map.remove(source, 2, 8, null));
        assertEquals("default", map.remove(source, 2, 8, "default"));
        assertEquals(1, map.size());
    }

    // ==================== ContainsKey Tests ====================

    @Test
    public void testContainsKey() {
        map.put("exists", "value");
        assertTrue(map.containsKey("exists"));
        assertTrue(map.containsKey(wrap("exists")));
        assertFalse(map.containsKey("notExists"));
    }

    // ==================== Remove Tests ====================

    @Test
    public void testRemove() {
        map.put("key1", "value1");
        map.put("key2", "value2");

        assertEquals("value1", map.remove(wrap("key1"), null));
        assertEquals(1, map.size());
        assertFalse(map.containsKey("key1"));
        assertTrue(map.containsKey("key2"));
    }

    @Test
    public void testRemoveNonExistent() {
        map.put("key1", "value1");
        assertNull(map.remove(wrap("nonexistent"), null));
        assertEquals("default", map.remove(wrap("nonexistent"), "default"));
        assertEquals(1, map.size());
    }

    // ==================== Size and Empty Tests ====================

    @Test
    public void testSizeAndEmpty() {
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());

        map.put("key1", "value1");
        assertFalse(map.isEmpty());
        assertEquals(1, map.size());

        map.put("key2", "value2");
        assertEquals(2, map.size());

        map.remove(wrap("key1"), null);
        assertEquals(1, map.size());

        map.remove(wrap("key2"), null);
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    // ==================== ForEach Tests ====================

    @Test
    public void testForEachEmpty() {
        List<String> visited = new ArrayList<>();
        map.forEach((k, v) -> visited.add(k.toString() + "=" + v));
        assertTrue(visited.isEmpty());
    }

    @Test
    public void testForEachSingleEntry() {
        map.put("key1", "value1");

        List<String> visited = new ArrayList<>();
        map.forEach((k, v) -> visited.add(k.toString() + "=" + v));

        assertEquals(1, visited.size());
        assertEquals("key1=value1", visited.get(0));
    }

    @Test
    public void testForEachMultipleEntries() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");

        Set<String> visited = new HashSet<>();
        map.forEach((k, v) -> visited.add(k.toString() + "=" + v));

        assertEquals(3, visited.size());
        assertTrue(visited.contains("key1=value1"));
        assertTrue(visited.contains("key2=value2"));
        assertTrue(visited.contains("key3=value3"));
    }

    // ==================== Keys Enumeration Tests ====================

    @Test
    public void testResetBefore() {
        CharSequenceToObjectMapQuick<String> map = new CharSequenceToObjectMapQuick<> ();
        map.put("KEY1", "VALUE1");
        ElementsEnumeration<String> e = map.keys();

        assertEquals("KEY1", dump(e));
        e.reset();
        assertEquals("KEY1", dump(e));
    }

    @Test
    public void testResetAfter() {
        CharSequenceToObjectMapQuick<String> map = new CharSequenceToObjectMapQuick<> ();

        ElementsEnumeration<String> e = map.keys();
        map.put("KEY1", "VALUE1");
        e.reset();
        assertEquals("KEY1", dump(e));
    }

    // ==================== Clear Tests ====================

    @Test
    public void testClear() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        assertEquals(2, map.size());

        map.clear();

        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertFalse(map.containsKey("key1"));
        assertFalse(map.containsKey("key2"));
    }

    // ==================== Serialization Tests ====================

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        map.put("key1", "value1");
        map.put("key2", "value2");

        // Serialize
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(map);
        oos.close();

        // Deserialize
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        @SuppressWarnings("unchecked")
        CharSequenceToObjectMapQuick<String> deserialized =
            (CharSequenceToObjectMapQuick<String>) ois.readObject();
        ois.close();

        // Verify
        assertEquals(2, deserialized.size());
        assertEquals("value1", deserialized.get("key1", null));
        assertEquals("value2", deserialized.get("key2", null));
    }

    @Test
    public void testSerializationPreservesSubstringFunctionality() throws IOException, ClassNotFoundException {
        map.put("hello", "world");

        // Serialize and deserialize
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(map);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        @SuppressWarnings("unchecked")
        CharSequenceToObjectMapQuick<String> deserialized =
            (CharSequenceToObjectMapQuick<String>) ois.readObject();
        ois.close();

        // Verify substring access works after deserialization
        String source = "XXhelloYY";
        assertEquals("world", deserialized.get(source, 2, 7, null));
    }

    // ==================== Edge Case Tests ====================

    @Test
    public void testEmptyStringKey() {
        map.put("", "emptyKey");
        assertEquals("emptyKey", map.get("", null));
        assertTrue(map.containsKey(""));
    }

    @Test
    public void testNullValue() {
        map.put("key1", null);
        assertNull(map.get("key1", "default"));
        assertTrue(map.containsKey("key1"));
    }

    @Test
    public void testManyEntries() {
        int count = 1000;
        for (int i = 0; i < count; i++) {
            map.put("key" + i, "value" + i);
        }

        assertEquals(count, map.size());

        for (int i = 0; i < count; i++) {
            assertEquals("value" + i, map.get("key" + i, null));
        }
    }

    @Test
    public void testSubstringAtBoundaries() {
        map.put("test", "value");

        // Full string as substring
        assertEquals("value", map.get("test", 0, 4, null));

        // Beginning of string
        String beginning = "testXXX";
        assertEquals("value", map.get(beginning, 0, 4, null));

        // End of string
        String end = "XXXtest";
        assertEquals("value", map.get(end, 3, 7, null));
    }

    @Test
    public void testKeyStoredAsString() {
        // When putting with CharSequence, key should be stored as String (converted via toString())
        StringBuilder key = new StringBuilder("mutableKey");
        map.put(key, "value");

        // Modify the original StringBuilder
        key.setLength(0);
        key.append("differentKey");

        // Original key should still be accessible
        assertEquals("value", map.get("mutableKey", null));
        assertFalse(map.containsKey("differentKey"));
    }

    @Test
    public void testSameKeyDifferentCharSequenceTypes() {
        // Put with String
        map.put("key", "value1");

        // Get with StringBuilder
        StringBuilder sbKey = new StringBuilder("key");
        assertEquals("value1", map.get(sbKey, null));

        // Overwrite with StringBuilder
        map.put(sbKey, "value2");
        assertEquals("value2", map.get("key", null));
        assertEquals(1, map.size());
    }

    // ==================== Helper Methods ====================

    private static String dump(ElementsEnumeration<String> enumeration) {
        StringBuilder result = new StringBuilder();
        while (enumeration.hasMoreElements()) {
            if (result.length() > 0)
                result.append(", ");
            result.append(enumeration.nextElement());
        }
        return result.toString();
    }
}