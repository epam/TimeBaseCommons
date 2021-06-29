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

import com.epam.deltix.util.collections.hash.HashCodeComputer;
import com.epam.deltix.util.collections.hash.SimpleHashCodeComputer;
import org.apache.commons.collections.SortedBag;
import org.apache.commons.collections.bag.TreeBag;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class Test_FixedSizeLongToOrderHashMap {
    private static final int MAX_SIZE = 16;

    private FixedSizeLongToObjectMap<Long> map = new FixedSizeLongToObjectMap<>(MAX_SIZE, null);

    @Test
    public void testEmpty() {
        assertNull(map.get(1L));
        assertFalse(map.iterator().hasNext());
    }

    @Test
    public void testClear() {
        testEmpty();
        map.clear();
        testEmpty();

        map.putIfEmpty(1, 1L);
        map.clear();
        testEmpty();
    }

    @Test
    public void testSingleElement() {
        map.putIfEmpty(1, 1L);
        assertEquals(new Long(1), map.get(1));

        Iterator<Long> iter = map.iterator();
        assertTrue(iter.hasNext());
        assertEquals(new Long(1), iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    public void testPutIfEmpty() {
        map.putIfEmpty(1, 11L);
        assertEquals(new Long(11), map.get(1));
        Long result = map.putIfEmpty(1, 111L); // shouldn't affect map
        assertEquals(new Long(11), result);
        assertEquals(new Long(11), map.get(1));

        Iterator<Long> iter = map.iterator();
        assertTrue(iter.hasNext());
        assertEquals(new Long(11), iter.next());
        assertFalse(iter.hasNext());
    }


    @Test
    public void testPutAndGet() {
        insert(1, 1L);
        insert(2, 2L);
        insert(3, 3L);
        assertContent("1, 2, 3");
        insert(4, 4L);
        assertEquals(new Long(1), map.get(1));
        assertEquals(new Long(2), map.get(2));
        assertEquals(new Long(3), map.get(3));
        assertEquals(new Long(4), map.get(4));
        assertContent("1, 2, 3, 4");
// no longer expose override method (Map.put)
//        override(2, 22L);
//        override(4, 44L);
//        assertContent("1, 3, 22, 44");
//        assertEquals(new Long(1), map.get(1));
//        assertEquals(new Long(22), map.get(2));
//        assertEquals(new Long(3), map.get(3));
//        assertEquals(new Long(44), map.get(4));

    }

// Hashing algorith m:
//
//    public int              modHashCode (long key, int mod) {
//        return (computeModHashCode ((int) (key ^ (key >>> 32)), mod));
//    }
//
//    public static int       computeModHashCode (int key, int mod) {
//        if (key == Integer.MIN_VALUE)
//            return (1);
//
//        if (key < 0)
//            key = -key;
//
//        return (key % mod);
//    }

    @Test
    public void testOverloadedContent() {
        for (int i=0; i < 3* MAX_SIZE /2; i++)
            insert(i, (long)i);

        assertContent("8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23");
    }

    @Test
    public void testKeysWithIdenticalHash() {
        HashCodeComputer hash = SimpleHashCodeComputer.INSTANCE;

        int key = MAX_SIZE + 1;
        for (int i=0; i < MAX_SIZE; i++, key += MAX_SIZE) {
            assertEquals(1, hash.modHashCode(key, MAX_SIZE)); // key % mod = key % MAX_SIZE = 1
            insert(key, (long)key);
        }

        assertContent("17, 33, 49, 65, 81, 97, 113, 129, 145, 161, 177, 193, 209, 225, 241, 257");

        insert(key, (long)key);
        assertContent("33, 49, 65, 81, 97, 113, 129, 145, 161, 177, 193, 209, 225, 241, 257, 273");
        key+= MAX_SIZE;

        insert(key, (long)key);
        assertContent("49, 65, 81, 97, 113, 129, 145, 161, 177, 193, 209, 225, 241, 257, 273, 289");
        key+= MAX_SIZE;

        insert(key, (long)key);
        assertContent("65, 81, 97, 113, 129, 145, 161, 177, 193, 209, 225, 241, 257, 273, 289, 305");
        key+= MAX_SIZE;

    }


    @Test
    public void testAddRemoveInLoop() {

        for (int i=0; i < 100; i++) {
            insert(i, (long)i);
        }

        assertContent("84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99");
    }


    private void insert(long key, Long value) {
        Long newItem = map.putIfEmpty(key, value);
        assertSame(newItem, value);
    }


    private void override(long key, Long value) {
        Long newItem = map.putIfEmpty(key, value);
        assertNotSame(newItem, value);
    }



    private void assertContent(String expected) {
        SortedBag bag = new TreeBag();
        Iterator<Long> iter = map.iterator();
        while(iter.hasNext())
            bag.add(iter.next());

        StringBuilder sb = new StringBuilder();
        for(Object item : bag) {
            if (sb.length() != 0)
                sb.append(", ");
            sb.append(item);
        }
        assertEquals(expected, sb.toString());
    }


}