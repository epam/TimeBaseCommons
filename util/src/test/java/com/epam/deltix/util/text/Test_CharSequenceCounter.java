package com.epam.deltix.util.text;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class Test_CharSequenceCounter {

    CharSequenceCounter counter;

    @Test
    public void test () {
        CharSequenceCounter counter = new CharSequenceCounter();
        for (int i = 0; i < 100000; i++) {
            assertEquals(counter, i);
            counter.increment();
        }
    }

    @Test
    public void testInitialValue() {
        CharSequenceCounter counter = new CharSequenceCounter(123);
        assertEquals(counter, 123);
        counter.increment();
        assertEquals(counter, 124);
    }

    @Test
    public void testExtremes () {
        CharSequenceCounter counter = new CharSequenceCounter(Integer.MAX_VALUE);
        assertEquals(counter, Integer.MAX_VALUE);
        counter.increment();
        assertEquals(counter, Integer.MAX_VALUE+1L);
    }

    @Test
    public void testExternalStorage() {
        byte[] buffer = new byte[CharSequenceCounter.WIDTH+2];
        Arrays.fill(buffer, (byte)'*');

        CharSequenceCounter counter = new CharSequenceCounter(buffer, 1);
        for (int i = 0; i < 1000; i++) {
            assertEquals(counter, i);
            counter.increment();
        }

        Assert.assertEquals("*00000000000000001000*", new String(buffer));

    }


    private static void assertEquals(CharSequenceCounter counter, long value) {
        String number = String.format("%0" + CharSequenceCounter.WIDTH + "d", value);
        Assert.assertEquals(number, counter.toString());
    }
}