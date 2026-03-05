package com.epam.deltix.util.memory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.LongToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link MemoryDataOutput#writeUnsignedByteX2},
 * {@link MemoryDataOutput#writeUnsignedByteX3},
 * and {@link MemoryDataOutput#writeUnsignedByteX4}.
 *
 * @author Generated
 */
public class Test_MemoryDataOutput_writeUnsignedByteX {
    private final MemoryDataOutput mdo = new MemoryDataOutput();

    // ==================== writeUnsignedByteX2 ====================

    @Test
    public void writeUnsignedByteX2_basic() {
        int pos = mdo.getPosition();
        mdo.writeUnsignedByteX2(0x41, 0x42);
        assertEquals(pos + 2, mdo.getPosition());
        assertEquals(pos + 2, mdo.getSize());
        assertEquals((byte) 0x41, mdo.getBuffer()[pos]);
        assertEquals((byte) 0x42, mdo.getBuffer()[pos + 1]);
    }

    @Test
    public void writeUnsignedByteX2_equivalentToTwoWriteUnsignedByte() {
        MemoryDataOutput ref = new MemoryDataOutput();
        ref.writeUnsignedByte(0xCA);
        ref.writeUnsignedByte(0xFE);

        mdo.writeUnsignedByteX2(0xCA, 0xFE);

        assertEquals(ref.getPosition(), mdo.getPosition());
        assertEquals(ref.getSize(), mdo.getSize());
        Assertions.assertArrayEquals(
                copyBuffer(ref, ref.getSize()),
                copyBuffer(mdo, mdo.getSize()));
    }

    @Test
    public void writeUnsignedByteX2_zeroes() {
        int pos = mdo.getPosition();
        mdo.writeUnsignedByteX2(0, 0);
        assertEquals(pos + 2, mdo.getPosition());
        assertEquals((byte) 0, mdo.getBuffer()[pos]);
        assertEquals((byte) 0, mdo.getBuffer()[pos + 1]);
    }

    @Test
    public void writeUnsignedByteX2_maxValues() {
        int pos = mdo.getPosition();
        mdo.writeUnsignedByteX2(0xFF, 0xFF);
        assertEquals(pos + 2, mdo.getPosition());
        assertEquals((byte) 0xFF, mdo.getBuffer()[pos]);
        assertEquals((byte) 0xFF, mdo.getBuffer()[pos + 1]);
    }

    @Test
    public void writeUnsignedByteX2_truncatesHighBits() {
        int pos = mdo.getPosition();
        mdo.writeUnsignedByteX2(0x1AA, 0x2BB);
        assertEquals((byte) 0xAA, mdo.getBuffer()[pos]);
        assertEquals((byte) 0xBB, mdo.getBuffer()[pos + 1]);
    }

    @Test
    public void writeUnsignedByteX2_bufferGrows() {
        MemoryDataOutput small = new MemoryDataOutput(2);
        small.writeUnsignedByteX2(0x01, 0x02);
        small.writeUnsignedByteX2(0x03, 0x04);
        assertEquals(4, small.getPosition());
        assertEquals(4, small.getSize());
        assertEquals((byte) 0x01, small.getBuffer()[0]);
        assertEquals((byte) 0x02, small.getBuffer()[1]);
        assertEquals((byte) 0x03, small.getBuffer()[2]);
        assertEquals((byte) 0x04, small.getBuffer()[3]);
    }

    @Test
    public void writeUnsignedByteX2_afterSeek() {
        mdo.seek(11); // Unaligned position
        int pos = mdo.getPosition();
        mdo.writeUnsignedByteX2(0xDE, 0xAD);
        assertEquals(pos + 2, mdo.getPosition());
        assertEquals(pos + 2, mdo.getSize());
        assertEquals((byte) 0xDE, mdo.getBuffer()[pos]);
        assertEquals((byte) 0xAD, mdo.getBuffer()[pos + 1]);
    }

    // ==================== writeUnsignedByteX3 ====================

    @Test
    public void writeUnsignedByteX3_basic() {
        int pos = mdo.getPosition();
        mdo.writeUnsignedByteX3(0x41, 0x42, 0x43);
        assertEquals(pos + 3, mdo.getPosition());
        assertEquals(pos + 3, mdo.getSize());
        assertEquals((byte) 0x41, mdo.getBuffer()[pos]);
        assertEquals((byte) 0x42, mdo.getBuffer()[pos + 1]);
        assertEquals((byte) 0x43, mdo.getBuffer()[pos + 2]);
    }

    @Test
    public void writeUnsignedByteX3_equivalentToThreeWriteUnsignedByte() {
        MemoryDataOutput ref = new MemoryDataOutput();
        ref.writeUnsignedByte(0xCA);
        ref.writeUnsignedByte(0xFE);
        ref.writeUnsignedByte(0xBA);

        mdo.writeUnsignedByteX3(0xCA, 0xFE, 0xBA);

        assertEquals(ref.getPosition(), mdo.getPosition());
        assertEquals(ref.getSize(), mdo.getSize());
        Assertions.assertArrayEquals(
                copyBuffer(ref, ref.getSize()),
                copyBuffer(mdo, mdo.getSize()));
    }

    @Test
    public void writeUnsignedByteX3_zeroes() {
        int pos = mdo.getPosition();
        mdo.writeUnsignedByteX3(0, 0, 0);
        assertEquals(pos + 3, mdo.getPosition());
        assertEquals((byte) 0, mdo.getBuffer()[pos]);
        assertEquals((byte) 0, mdo.getBuffer()[pos + 1]);
        assertEquals((byte) 0, mdo.getBuffer()[pos + 2]);
    }

    @Test
    public void writeUnsignedByteX3_maxValues() {
        int pos = mdo.getPosition();
        mdo.writeUnsignedByteX3(0xFF, 0xFF, 0xFF);
        assertEquals(pos + 3, mdo.getPosition());
        assertEquals((byte) 0xFF, mdo.getBuffer()[pos]);
        assertEquals((byte) 0xFF, mdo.getBuffer()[pos + 1]);
        assertEquals((byte) 0xFF, mdo.getBuffer()[pos + 2]);
    }

    @Test
    public void writeUnsignedByteX3_truncatesHighBits() {
        int pos = mdo.getPosition();
        mdo.writeUnsignedByteX3(0x1AA, 0x2BB, 0x3CC);
        assertEquals((byte) 0xAA, mdo.getBuffer()[pos]);
        assertEquals((byte) 0xBB, mdo.getBuffer()[pos + 1]);
        assertEquals((byte) 0xCC, mdo.getBuffer()[pos + 2]);
    }

    @Test
    public void writeUnsignedByteX3_bufferGrows() {
        MemoryDataOutput small = new MemoryDataOutput(2);
        small.writeUnsignedByteX3(0x01, 0x02, 0x03);
        small.writeUnsignedByteX3(0x04, 0x05, 0x06);
        assertEquals(6, small.getPosition());
        assertEquals(6, small.getSize());
        assertEquals((byte) 0x01, small.getBuffer()[0]);
        assertEquals((byte) 0x02, small.getBuffer()[1]);
        assertEquals((byte) 0x03, small.getBuffer()[2]);
        assertEquals((byte) 0x04, small.getBuffer()[3]);
        assertEquals((byte) 0x05, small.getBuffer()[4]);
        assertEquals((byte) 0x06, small.getBuffer()[5]);
    }

    @Test
    public void writeUnsignedByteX3_afterSeek() {
        mdo.seek(11); // Unaligned position
        int pos = mdo.getPosition();
        mdo.writeUnsignedByteX3(0xDE, 0xAD, 0xBE);
        assertEquals(pos + 3, mdo.getPosition());
        assertEquals(pos + 3, mdo.getSize());
        assertEquals((byte) 0xDE, mdo.getBuffer()[pos]);
        assertEquals((byte) 0xAD, mdo.getBuffer()[pos + 1]);
        assertEquals((byte) 0xBE, mdo.getBuffer()[pos + 2]);
    }

    // ==================== writeUnsignedByteX4 ====================

    @Test
    public void writeUnsignedByteX4_basic() {
        int pos = mdo.getPosition();
        mdo.writeUnsignedByteX4(0x41, 0x42, 0x43, 0x44);
        assertEquals(pos + 4, mdo.getPosition());
        assertEquals(pos + 4, mdo.getSize());
        assertEquals((byte) 0x41, mdo.getBuffer()[pos]);
        assertEquals((byte) 0x42, mdo.getBuffer()[pos + 1]);
        assertEquals((byte) 0x43, mdo.getBuffer()[pos + 2]);
        assertEquals((byte) 0x44, mdo.getBuffer()[pos + 3]);
    }

    @Test
    public void writeUnsignedByteX4_equivalentToFourWriteUnsignedByte() {
        MemoryDataOutput ref = new MemoryDataOutput();
        ref.writeUnsignedByte(0xCA);
        ref.writeUnsignedByte(0xFE);
        ref.writeUnsignedByte(0xBA);
        ref.writeUnsignedByte(0xBE);

        mdo.writeUnsignedByteX4(0xCA, 0xFE, 0xBA, 0xBE);

        assertEquals(ref.getPosition(), mdo.getPosition());
        assertEquals(ref.getSize(), mdo.getSize());
        Assertions.assertArrayEquals(
                copyBuffer(ref, ref.getSize()),
                copyBuffer(mdo, mdo.getSize()));
    }

    @Test
    public void writeUnsignedByteX4_zeroes() {
        int pos = mdo.getPosition();
        mdo.writeUnsignedByteX4(0, 0, 0, 0);
        assertEquals(pos + 4, mdo.getPosition());
        assertEquals((byte) 0, mdo.getBuffer()[pos]);
        assertEquals((byte) 0, mdo.getBuffer()[pos + 1]);
        assertEquals((byte) 0, mdo.getBuffer()[pos + 2]);
        assertEquals((byte) 0, mdo.getBuffer()[pos + 3]);
    }

    @Test
    public void writeUnsignedByteX4_maxValues() {
        int pos = mdo.getPosition();
        mdo.writeUnsignedByteX4(0xFF, 0xFF, 0xFF, 0xFF);
        assertEquals(pos + 4, mdo.getPosition());
        assertEquals((byte) 0xFF, mdo.getBuffer()[pos]);
        assertEquals((byte) 0xFF, mdo.getBuffer()[pos + 1]);
        assertEquals((byte) 0xFF, mdo.getBuffer()[pos + 2]);
        assertEquals((byte) 0xFF, mdo.getBuffer()[pos + 3]);
    }

    @Test
    public void writeUnsignedByteX4_truncatesHighBits() {
        int pos = mdo.getPosition();
        mdo.writeUnsignedByteX4(0x1AA, 0x2BB, 0x3CC, 0x4DD);
        assertEquals((byte) 0xAA, mdo.getBuffer()[pos]);
        assertEquals((byte) 0xBB, mdo.getBuffer()[pos + 1]);
        assertEquals((byte) 0xCC, mdo.getBuffer()[pos + 2]);
        assertEquals((byte) 0xDD, mdo.getBuffer()[pos + 3]);
    }

    @Test
    public void writeUnsignedByteX4_bufferGrows() {
        MemoryDataOutput small = new MemoryDataOutput(2);
        small.writeUnsignedByteX4(0x01, 0x02, 0x03, 0x04);
        small.writeUnsignedByteX4(0x05, 0x06, 0x07, 0x08);
        assertEquals(8, small.getPosition());
        assertEquals(8, small.getSize());
        assertEquals((byte) 0x01, small.getBuffer()[0]);
        assertEquals((byte) 0x02, small.getBuffer()[1]);
        assertEquals((byte) 0x03, small.getBuffer()[2]);
        assertEquals((byte) 0x04, small.getBuffer()[3]);
        assertEquals((byte) 0x05, small.getBuffer()[4]);
        assertEquals((byte) 0x06, small.getBuffer()[5]);
        assertEquals((byte) 0x07, small.getBuffer()[6]);
        assertEquals((byte) 0x08, small.getBuffer()[7]);
    }

    @Test
    public void writeUnsignedByteX4_afterSeek() {
        mdo.seek(11); // Unaligned position
        int pos = mdo.getPosition();
        mdo.writeUnsignedByteX4(0xDE, 0xAD, 0xBE, 0xEF);
        assertEquals(pos + 4, mdo.getPosition());
        assertEquals(pos + 4, mdo.getSize());
        assertEquals((byte) 0xDE, mdo.getBuffer()[pos]);
        assertEquals((byte) 0xAD, mdo.getBuffer()[pos + 1]);
        assertEquals((byte) 0xBE, mdo.getBuffer()[pos + 2]);
        assertEquals((byte) 0xEF, mdo.getBuffer()[pos + 3]);
    }

    // ==================== Helpers ====================

    private static byte[] copyBuffer(MemoryDataOutput mdo, int length) {
        byte[] result = new byte[length];
        System.arraycopy(mdo.getBuffer(), 0, result, 0, length);
        return result;
    }

}
