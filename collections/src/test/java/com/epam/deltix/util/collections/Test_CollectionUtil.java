package com.epam.deltix.util.collections;

import com.epam.deltix.util.collections.generated.LongArrayList;
import com.epam.deltix.util.collections.generated.LongList;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;


public class Test_CollectionUtil {

    @Test
    public void testHashCode() {
        for (int i = 0; i < 1000; i++) {
            final long v1 = randomLong();
            final long v2 = randomLong();

            Assert.assertEquals(CollectionUtil.hashCode(list(v1, v2)), CollectionUtil.hashCode(list(v1, v2)));
        }

        Assert.assertNotEquals(CollectionUtil.hashCode(list(1)), CollectionUtil.hashCode(list(2)));
    }

    @Test
    public void testEquals() {
        for (int i = 0; i < 1000; i++) {
            final long v1 = randomLong();
            final long v2 = randomLong();

            Assert.assertTrue(CollectionUtil.equals(list(v1, v2), list(v1, v2)));
            Assert.assertFalse(CollectionUtil.equals(list(v1), list(v1, v2)));
        }
    }

    @Test
    public void testCopy() {
        for (int i = 0; i < 1000; i++) {
            final LongArrayList list = new LongArrayList(i);

            for (int j = 0; j < i; j++) {
                list.add(randomLong());
            }

            final LongArrayList copy = CollectionUtil.copy(list);
            Assert.assertTrue(CollectionUtil.equals(list, copy));
        }
    }

    private static LongList list(long... values) {
        return new LongArrayList(values);
    }

    private static long randomLong() {
        return ThreadLocalRandom.current().nextLong();
    }

}
