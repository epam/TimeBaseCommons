package deltix.util.lang;

/*  ##UTILS## */

import deltix.util.collections.GapByteQueue;
import deltix.util.io.GapQueueInputStream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

/**
 *
 */
public class Test_GapQueue {

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
}
