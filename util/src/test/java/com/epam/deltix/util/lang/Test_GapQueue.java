package com.epam.deltix.util.lang;

import com.epam.deltix.util.collections.GapByteQueue;
import com.epam.deltix.util.io.GapQueueInputStream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

/**
 *
 */
public class Test_GapQueue {

    private byte[] data = new byte[1024];

    @Test
    public void test1() throws Exception {
        GapByteQueue q = new GapByteQueue(128);
        GapQueueInputStream in = new GapQueueInputStream(q);

        byte[] data = new byte[16];
        for (int i = 0, len = data.length; i < len; i++)
            data[i] = 1;

        assertTrue(q.write(data, 0, data.length, 0));
        assertEquals(data.length, q.available());

        q.write(data, 0, data.length, 64);

        for (int i = 1; i < 4; i++)
            q.write(data, 0, data.length, i * data.length);

        assertEquals(80, q.available());

        in.skip(20);

        q.write(data, 0, data.length, 120);

        q.write(data, 0, 10, 80);
        q.write(data, 0, 10, 100);
        q.write(data, 0, 10, 90);
        q.write(data, 0, 10, 110);

        assertEquals(116, q.available());
    }

    @Test
    public void test2() throws Exception {
        GapByteQueue q = new GapByteQueue(128);
        GapQueueInputStream in = new GapQueueInputStream(q);

        byte[] data = new byte[64];
        for (int i = 0, len = data.length; i < len; i++)
            data[i] = 1;

        q.write(data, 0, data.length, 0);
        q.write(data, 0, 16, 64);

        assertEquals(80, q.available());
        in.skip(32);
        assertEquals(48, q.available());

        q.write(data, 0, 16, 10);
        assertEquals(48, q.available());

        q.write(data, 0, 16, 120);
        assertEquals(48, q.available());

        // fill gaps
        q.write(data, 0, 40, 80);
        q.write(data, 0, 2, 8);

        assertEquals(122, q.available());

    }

    @Test
    public void test3() throws Exception {
        GapByteQueue q = new GapByteQueue(128);
        GapQueueInputStream in = new GapQueueInputStream(q);

        byte[] data = new byte[64];
        for (int i = 0, len = data.length; i < len; i++)
            data[i] = 1;

        q.write(data, 0, 64, 0);
        in.skip(32);

        q.write(data, 0, 64, 96);
        q.write(data, 0, 32, 64);

        assertEquals(128, q.available());
    }

    @Test
    public void test4() throws Exception {
        GapByteQueue q = new GapByteQueue(128);
        GapQueueInputStream in = new GapQueueInputStream(q);

        byte[] data = new byte[64];
        for (int i = 0, len = data.length; i < len; i++)
            data[i] = 1;

        q.write(data, 0, 64, 0);
        in.skip(32);

        q.write(data, 0, 16, 80);
        q.write(data, 0, 32, 96);
    }

    @Test
    public void test5() {

        GapByteQueue q = new GapByteQueue(131172);
        fill(q, 36761, 1, (byte) 1);
        fill(q, 5621, 109040, (byte) 2);

        fill(q, 8429, 114661, (byte) 3);
        fill(q, 1, 0, (byte) 4);
        fill(q, 30503, 36762, (byte) 5);
        fill(q, 8553, 67265, (byte) 6);
        fill(q, 3929, 75818, (byte) 7);
        fill(q, 4116, 79747, (byte) 8);
        fill(q, 4205, 83863, (byte) 9);
        fill(q, 4156, 88068, (byte) 10);
        fill(q, 4115, 92224, (byte) 11);
        fill(q, 12701, 96339, (byte) 12);

        assertEquals(123090, q.size());

        byte[] result = new byte[q.size()];
        q.poll(result, 0, q.size());

        assertEquals(36761, count(result, (byte) 1));
        assertEquals(5621, count(result, (byte) 2));
        assertEquals(8429, count(result, (byte) 3));
        assertEquals(1, count(result, (byte) 4));
        assertEquals(30503, count(result, (byte) 5));
        assertEquals(8553, count(result, (byte) 6));
        assertEquals(12701, count(result, (byte) 12));
    }

    private void fill(GapByteQueue q, int length, int position, byte value) {
        if (data.length < length)
            data = new byte[length + length / 2];
        Arrays.fill(data, 0, length, value);
        q.write(data, 0, length, position);
    }

    public int count(byte[] bytes, byte value) {
        int count = 0;
        for (byte b : bytes) {
            if (b == value)
                count++;
        }

        return count;
    }
}
