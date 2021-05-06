package com.epam.deltix.util.collections;

import com.epam.deltix.util.collections.generated.ObjectArrayList;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;


public class Test_ObjectArrayList {

    @Test
    public void testRegressionAV () {
        final ObjectArrayList<String> list = new ObjectArrayList<>();
        list.add("TEST");
        final String[] array = list.toArray(new String[0]);
        assertEquals("TEST", array[0]);
    }

    @Test
    public void testConstructors() {
        assertList("[]", new ObjectArrayList<>());
        assertList("[]", new ObjectArrayList<>(5));
        assertList("[1, 2]", new ObjectArrayList<>(Arrays.asList("1", "2")));
        assertList("[3, 4]", new ObjectArrayList<>(new String[]{"3", "4"}));
        assertList("[6]", new ObjectArrayList<>(new String[]{"5", "6", "7"}, 1, 1));
    }

    @Test
    public void testBasicMethods() {
        final ObjectArrayList<String> list = new ObjectArrayList<>();
        list.add("1");
        assertList("[1]", list);
        String[] array = list.toArray(new String[0]);
        Assert.assertEquals(array[0], list.get(0));

        list.add("2");
        assertList("[1, 2]", list);

        list.remove("2");
        assertList("[1]", list);

        assertFalse(list.contains("2"));
        assertTrue(list.contains("1"));

        list.add("3");
        assertList("[1, 3]", list);
        assertEquals(1, list.indexOf("3"));
        assertEquals(-1, list.indexOf("2"));

        list.remove(0);
        assertList("[3]", list);

        list.set(0, "2");
        assertList("[2]", list);


        list.addAll(Arrays.asList("5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"));
        assertList("[2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]", list);

        list.clear();
        assertList("[]", list);
    }

    @Test
    public void testBoundChecks() {
        final ObjectArrayList<String> list = new ObjectArrayList<>();

        assertIndexOutOfBounds(() -> list.get(-1));
        assertIndexOutOfBounds(() -> list.set(-1, null));
        assertIndexOutOfBounds(() -> list.remove(-1));
        assertIndexOutOfBounds(() -> list.add(-1, "2"));

        assertIndexOutOfBounds(() -> list.get(0));
        assertIndexOutOfBounds(() -> list.set(0, null));
        assertIndexOutOfBounds(() -> list.remove(0));
        assertIndexOutOfBounds(() -> list.add(1, "2"));

        list.add("1");
        assertIndexOutOfBounds(() -> list.get(1));
        assertIndexOutOfBounds(() -> list.set(1, null));
        assertIndexOutOfBounds(() -> list.remove(1));
        assertIndexOutOfBounds(() -> list.add(2, "2"));
        assertIndexOutOfBounds(() -> list.addAll(Arrays.asList("2", "3"), 2));
    }

    private static void assertList(String expected, ObjectArrayList<String> list) {
        final Object[] array = list.toArray();
        Assert.assertEquals(array.length, list.size());
        Assert.assertEquals(expected, Arrays.toString(array));

        for (int i = 0; i < array.length; i++) {
            Assert.assertEquals(array[i], list.get(i));
        }
    }

    private static void assertIndexOutOfBounds(Action action) {
        try {
            action.perform();
            fail("expected index out of bounds exception");
        } catch (IndexOutOfBoundsException e) {
            // ignore
        }
    }

    @FunctionalInterface
    private interface Action {

        void perform();

    }

}
