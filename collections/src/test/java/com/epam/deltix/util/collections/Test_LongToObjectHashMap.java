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

import deltix.util.collections.generated.LongToObjectHashMap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Test_LongToObjectHashMap {
    private final static int INITIAL_CAPACITY = 4;
    private final LongToObjectHashMap<String> map = new LongToObjectHashMap<>(INITIAL_CAPACITY);

    @Test
    public void testComputeIfAbsent1() {
        map.put(1, "ONE");

        assertEquals("ONE", map.computeIfAbsent(1,
                (key)->{
                    fail("Not supposed to be called - key is present!");
                    return null;
                })
        );
    }


    @Test
    public void testComputeIfAbsent2() {
        map.put(1, "ONE");

        assertEquals("TWO", map.computeIfAbsent(2,
                (key)->{
                    assertEquals(2, key);
                    return "TWO";
                })
        );
    }

    @Test
    public void testComputeIfAbsent3() {
        assertEquals("ONE", map.computeIfAbsent(1,
                (key)->{
                    assertEquals(1, key);
                    return "ONE";
                })
        );

        assertEquals("ONE", map.get(1, null));

        assertEquals("ONE", map.computeIfAbsent(1,
                (key)->{
                    fail("Not supposed to be called - key is present!");
                    return null;
                })
        );
    }

    @Test
    public void testComputeReturnNull() {
        assertNull(map.computeIfAbsent(1, (key)->null));
        assertNull(map.get(1, null));
    }

    @Test
    public void testComputeIfAbsent4() {
        for (int i=0; i < INITIAL_CAPACITY*10; i++) {
            assertEquals("ValueFor#" + i, map.computeIfAbsent(i, (key) -> "ValueFor#" + key));
        }
    }
}
