package com.epam.deltix.util.collections;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class Test_AtomicArrayList {

    private AtomicArrayList<Integer> list;

    @Before
    public void init() {
        list = new AtomicArrayList<>(16);
    }

    @Test
    public void testAdd() {
        assertEquals(list.size(), 0);

        list.add(0);
        list.add(1);
        assertEquals(list.size(), 2);

        addBatch(2, 19, list);
        assertEquals(list.size(), 20);

        for (int i = 0; i < 20; i++) {
            assertEquals(i, (int) list.get(i));
            assertEquals(true, list.contains(i));
        }

        addBatchIfAbsent(0, 20, list);
        assertEquals(list.size(), 21);
        assertEquals(true, list.contains(20));
    }

    @Test
    public void testRemove() {
        addBatch(0, 99, list);
        assertEquals(list.size(), 100);
        assertContainsBatch(0, 99, list);

        removeBatch(0, 9, list);
        assertEquals(list.size(), 90);
        assertContainsBatch(10, 99, list);

        addBatch(0, 9, list);
        assertEquals(list.size(), 100);
        assertContainsBatch(0, 99, list);

        removeBatch(0, 99, list);
        assertEquals(list.size(), 0);
    }

    @Test
    public void testVisit() {
        addBatch(0, 99, list);
        removeBatch(40, 59, list);

        final Collection<Integer> elements = new ArrayList<>();
        list.visit(new Visitor<Integer>() {
            @Override
            public boolean visit(Integer e) {
                elements.add(e);
                return true;
            }
        });

        assertEquals(80, elements.size());
        assertContainsBatch(0, 39, elements);
        assertContainsBatch(60, 99, elements);
    }

    private static void assertContainsBatch(int from, int to, AtomicContainer<Integer> container) {
        for (int i = from; i <= to; i++)
            assertEquals(true, container.contains(i));
    }

    private static void assertContainsBatch(int from, int to, Collection<Integer> container) {
        for (int i = from; i <= to; i++)
            assertEquals(true, container.contains(i));
    }

    private static void addBatch(int from, int to, AtomicContainer<Integer> container) {
        for (int i = from; i <= to; i++)
            container.add(i);
    }

    private static void addBatchIfAbsent(int from, int to, AtomicContainer<Integer> container) {
        for (int i = from; i <= to; i++)
            container.addIfAbsent(i);
    }

    private static void removeBatch(int from, int to, AtomicContainer<Integer> container) {
        for (int i = from; i <= to; i++)
            container.remove(i);
    }

}
