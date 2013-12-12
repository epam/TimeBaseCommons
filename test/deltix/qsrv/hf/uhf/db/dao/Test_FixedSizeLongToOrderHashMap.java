package deltix.qsrv.hf.uhf.db.dao;

import org.apache.commons.collections.SortedBag;
import org.apache.commons.collections.bag.TreeBag;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

/*  ##UHF.FRAMEWORK## */
public class Test_FixedSizeLongToOrderHashMap {
    private static final int INITIAL_CAPACITY = 16;
    private FixedSizeLongToOrderHashMap<Long> map = new FixedSizeLongToOrderHashMap<> (INITIAL_CAPACITY);


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

        map.put(1, 1L);
        map.clear();
        testEmpty();
    }

    @Test
    public void testSingleElement() {
        map.put(1, 1L);
        assertEquals(new Long(1), map.get(1));

        Iterator<Long> iter = map.iterator();
        assertTrue(iter.hasNext());
        assertEquals(new Long(1), iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    public void testPutIfEmpty() {
        map.putIfEmpty(1, 1L);
        assertEquals(new Long(1), map.get(1));
        map.put(1, 11L);
        assertEquals(new Long(11), map.get(1));
        Long result = map.putIfEmpty(1, 111L); // this time it shouldn't affect map
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

        override(2, 22L);
        override(4, 44L);
        assertContent("1, 3, 22, 44");
        assertEquals(new Long(1), map.get(1));
        assertEquals(new Long(22), map.get(2));
        assertEquals(new Long(3), map.get(3));
        assertEquals(new Long(44), map.get(4));

    }



    @Test
    public void testOverloadedContent() {
        for (int i=0; i < 3*INITIAL_CAPACITY/2; i++)
            insert(i, (long)i);

        assertContent("8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23");
    }


    @Test
    public void testAddRemoveInLoop() {

        for (int i=0; i < 100; i++) {
            insert(i, (long)i);
        }

        assertContent("84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99");
    }


    private void insert(long key, Long value) {
        boolean newItem = map.put(key, value);
        assertTrue(newItem);
    }


    private void override(long key, Long value) {
        boolean newItem = map.put(key, value);
        assertFalse(newItem);
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
