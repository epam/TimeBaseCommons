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
