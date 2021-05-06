package com.epam.deltix.util.collections;

import org.junit.Test;
import static org.junit.Assert.*;
import com.epam.deltix.util.collections.generated.ByteArrayList;

import java.util.UUID;


public class Test_ByteArrayListUtils {

    @Test
    public void testAppendByte() {
        ByteArrayList ar = new ByteArrayList();
        for (int i = 0; i < 5; ++i) ByteArrayListUtils.append(ar, (byte) i);
        for (int i = 0; i < 5; ++i) assertEquals(ByteArrayListUtils.toByte(ar, i), i);
    }

    @Test
    public void testAssign() {
        ByteArrayList ar = new ByteArrayList();
        for (int i = 0; i < 5; ++i)
            ByteArrayListUtils.append(ar, (byte) i);

        ByteArrayList bytes = ByteArrayListUtils.assign(null, ar);
        assertEquals(5, bytes.size());
        for (int i = 0; i < 5; ++i)
            assertEquals(ar.get(i), bytes.get(i));

        for (int i = 0; i < 5; ++i) assertEquals(ByteArrayListUtils.toByte(bytes, i), i);
    }
    @Test
    public void testAppendShort() {
        ByteArrayList ar = new ByteArrayList();
        for (int i = 0; i < 5; ++i) ByteArrayListUtils.append(ar, (short)(512 + i));
        for (int i = 0; i < 5; ++i) assertEquals(ByteArrayListUtils.toShort(ar, i * 2), 512 + i);
    }
    @Test
    public void testAppendInt() {
        ByteArrayList ar = new ByteArrayList();
        for (int i = 0; i < 5; ++i) ByteArrayListUtils.append(ar, (int) (100000 + i));
        assertEquals(ar.getByte(0), -96);
        assertEquals(ar.getByte(1), -122);
        assertEquals(ar.getByte(2), 1);
        assertEquals(ar.getByte(3), 0);

        for (int i = 0; i < 5; ++i) assertEquals(ByteArrayListUtils.toInt(ar, i * 4), 100000 + i);
    }
    @Test
    public void testAppendLong() {
        ByteArrayList ar = new ByteArrayList();
        for (int i = 0; i < 5; ++i) ByteArrayListUtils.append(ar, (long)((long) 1000000 * (long) 1000000  + i));
        for (int i = 0; i < 5; ++i) assertEquals(ByteArrayListUtils.toLong(ar, i * 8), (long)1000000 * (long) 1000000 + i);
    }
    @Test
    public void testAppendString() {
        ByteArrayList ar = new ByteArrayList();
        ByteArrayListUtils.append(ar, "aba");
        ByteArrayListUtils.append(ar, "caba");
        assertEquals(ByteArrayListUtils.toString(ar), "abacaba");
        assertEquals(ByteArrayListUtils.toString(ar, 6), "caba");
    }

    @Test
    public void testEquals() {
        ByteArrayList ar1 = new ByteArrayList();
        ByteArrayList ar2 = new ByteArrayList();
        ar1.add((byte)1);
        ar1.add((byte)2);
        ar1.add((byte)3);
        ar1.add((byte)4);
        ar2.add((byte)1);
        ar2.add((byte)2);
        ar2.add((byte)3);
        assertEquals(ByteArrayListUtils.equals(ar1, ar2), false);
        ar2.add((byte)4);
        assertEquals(ByteArrayListUtils.equals(ar1, ar2), true);
        ar1.add((byte)17);
        ar2.add((byte)18);
        assertEquals(ByteArrayListUtils.equals(ar1, ar2), false);
    }

    @Test
    public void testCopy2() {
        ByteArrayList ar1 = new ByteArrayList();
        ByteArrayList ar2 = new ByteArrayList();
        ar1.add((byte)1);
        ar1.add((byte)2);
        ar1.add((byte)3);

        ar1.copyTo(ar2);
        assertEquals(ByteArrayListUtils.equals(ar1, ar2), true);
        ar2.add((byte)4);
        ar1.copyTo(ar2);
        assertEquals(ByteArrayListUtils.equals(ar1, ar2), true);
        ar1.add((byte)17);
        ar2.add((byte)18);

        ar1.copyTo(ar2);
        assertEquals(ByteArrayListUtils.equals(ar1, ar2), true);
    }

    @Test
    public void testUUID() {
        UUID u1 = new UUID(0xFEDCBA9876543210L, 0x8091A2B3C4D5E6F7L);

        ByteArrayList ar = ByteArrayListUtils.assign(null, u1);
        UUID u2 = ByteArrayListUtils.toUUID(ar);

        assertEquals(u1, u2);
        // Ensure endian
        assertEquals(ar.getByte(0), (byte)0xFE);
        assertEquals(ar.getByte(8), (byte)0x80);

    }




}