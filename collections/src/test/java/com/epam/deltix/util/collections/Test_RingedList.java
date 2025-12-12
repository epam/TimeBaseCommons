package com.epam.deltix.util.collections;

import com.epam.deltix.util.CommonsJUnitCategories;
import deltix.util.collections.generated.LongRingedList;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayDeque;
import java.util.Random;

@Category(CommonsJUnitCategories.Utils.class)
public class Test_RingedList {

    @Test
    public void testAddRemove() {
        LongRingedList queue = new LongRingedList();
        for (int i = 0; i < 100_000; ++i) {
            queue.add(i);
        }
        for (int i = 0; i < 50_000; ++i) {
            queue.pop();
        }
        for (int i = 0; i < 25_000; ++i) {
            queue.add(i);
        }
        Assert.assertEquals(75_000, queue.size());

        for (int i = 50_000, j = 0; i < 100_000; ++i, ++j) {
            Assert.assertEquals(i, queue.getLong(j));
        }
        for (int i = 0, j = 50_000; i < 25_000; ++i, ++j) {
            Assert.assertEquals(i, queue.getLong(j));
        }
    }

    @Test
    public void testPrimitiveStress() {
        LongRingedList queue = new LongRingedList();
        ArrayDeque<Long> etalon = new ArrayDeque<>();

        Random rand = new Random();
        int iterations = 5_000_000;
        for (int i = 0; i < iterations; ++i) {
            int type = Math.abs(rand.nextInt()) % 4;
            if (Math.abs(type) == 0 && queue.size() > 0) {
                // pop and compare
                Assert.assertEquals(etalon.removeFirst().longValue(), queue.pop());
            } else {
                // add random value
                long newValue = rand.nextLong();
                queue.add(newValue);
                etalon.addLast(newValue);
            }

            if (i % 100_000 == 0) {
                // compare queues
                int index = 0;
                Assert.assertEquals(etalon.size(), queue.size());
                Long[] etalonArray = etalon.toArray(new Long[0]);
                for (Long element : queue) {
                    Assert.assertEquals(etalonArray[index], element);
                    Assert.assertEquals(element, queue.get(index));
                    Assert.assertEquals(element.longValue(), queue.getLong(index));
                    index++;
                }
            }

            if (i % 300_001 == 0) {
                // drain queues
                Assert.assertEquals(etalon.size(), queue.size());
                int size = etalon.size();
                for (int j = 0; j < size; ++j) {
                    Assert.assertEquals(etalon.pop().longValue(), queue.pop());
                }

                Assert.assertEquals(etalon.size(), 0);
                Assert.assertEquals(queue.size(), 0);
            }
        }
    }

    @Test
    public void testSetSize() {
        LongRingedList queue = new LongRingedList();
        for (int i = 0; i < 100_000; ++i) {
            queue.add(i);
        }
        Assert.assertEquals(100_000, queue.size());

        queue.setSize(1_000_000);
        Assert.assertEquals(1_000_000, queue.size());
        for (int i = 0; i < 100_000; ++i) {
            Assert.assertEquals(i, queue.getLong(i));
        }
        for (int i = 100_000; i < 1_000_000; ++i) {
            Assert.assertEquals(0, queue.getLong(i));
        }
    }

    @Test
    public void testSort() {
        LongRingedList queue = new LongRingedList();
        for (int i = 1; i <= 10_000; ++i) {
            queue.add(10_000 - i);
        }
        Assert.assertEquals(10_000, queue.size());

        queue.sort();
        Assert.assertEquals(queue.size(), 10_000);
        for (int i = 0; i < 10_000; ++i) {
            Assert.assertEquals(i, queue.getLong(i));
        }
    }

}
