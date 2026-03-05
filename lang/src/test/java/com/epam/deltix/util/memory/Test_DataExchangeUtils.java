/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.util.memory;

import com.epam.deltix.util.memory.DataExchangeUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("utils")
public class Test_DataExchangeUtils {

    @Test
    @Disabled("Not really a test. Just a check that type casting works as expected")
    public void javaTypeCastCheck() {
        for (int i = -256; i <= 256; i++) {
            assertEquals((byte) (i & 0xFF), (byte) i);
        }
    }

    //region Tests for long

    @Test
    public void writeLong() {
        byte[] out = new byte[8];
        DataExchangeUtils.writeLong(out, 0, 0x0807060504030201L);

        // BigEndian expected
        assertArrayEquals(new byte[]{0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01}, out);
    }

    @Test
    public void writeLong_Negative() {
        byte[] out = new byte[8];
        DataExchangeUtils.writeLong(out, 0, 0x7EF7F6F5F4F3F2F1L);

        // BigEndian expected
        assertArrayEquals(new byte[]{0x7E, (byte) 0xF7, (byte) 0xF6, (byte) 0xF5, (byte) 0xF4, (byte) 0xF3, (byte) 0xF2, (byte) 0xF1}, out);
    }

    @Test
    public void writeLongInvertBytes() {
        byte[] out = new byte[8];
        DataExchangeUtils.writeLongInvertBytes(out, 0, 0x0807060504030201L);

        // LittleEndian expected
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08}, out);
    }

    @Test
    public void writeLongInvertBytes_Negative() {
        byte[] out = new byte[8];
        DataExchangeUtils.writeLongInvertBytes(out, 0, 0x7EF7F6F5F4F3F2F1L);

        // LittleEndian expected
        assertArrayEquals(new byte[]{(byte) 0xF1, (byte) 0xF2, (byte) 0xF3, (byte) 0xF4, (byte) 0xF5, (byte) 0xF6, (byte) 0xF7, 0x7E}, out);
    }


    @Test
    public void writeLong_NegativeOffset() {
        byte[] out = new byte[8];
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            DataExchangeUtils.writeLong(out, -1, 0x0807060504030201L);
        });
    }

    @Test
    public void writeLong_OffsetOverflow() {
        byte[] out = new byte[8];
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            DataExchangeUtils.writeLong(out, 7, 0x0807060504030201L);
        });
    }

    @Test
    public void readLong() {
        byte[] in = new byte[]{0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01};
        long value = DataExchangeUtils.readLong(in, 0);

        // BigEndian
        Assertions.assertEquals(0x0807060504030201L, value);
    }

    @Test
    public void readLittleEndianLong() {
        byte[] in = new byte[]{0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01};
        long value = DataExchangeUtils.readLittleEndianLong(in, 0);

        // LittleEndian
        Assertions.assertEquals(0x0102030405060708L, value);
    }

    @Test
    public void readLittleEndianLong_ZeroOffset() {
        byte[] in = new byte[]{0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01};
        long value = DataExchangeUtils.readLittleEndianLong(in);

        // LittleEndian
        Assertions.assertEquals(0x0102030405060708L, value);
    }

    //endregion

    //region Tests for long48


    @Test
    public void writeLong48() {
        byte[] out = new byte[6];
        DataExchangeUtils.writeLong48(out, 0, 0x060504030201L);

        // BigEndian
        assertArrayEquals(new byte[]{0x06, 0x05, 0x04, 0x03, 0x02, 0x01}, out);
    }

    @Test
    public void writeLong48_MaxValue() {
        byte[] out = new byte[6];
        DataExchangeUtils.writeLong48(out, 0, DataExchangeUtils.MAX_LONG48);

        // BigEndian
        assertArrayEquals(new byte[]{0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}, out);
    }

    @Test
    public void writeLong48_MinValue() {
        byte[] out = new byte[6];
        DataExchangeUtils.writeLong48(out, 0, DataExchangeUtils.MIN_LONG48);

        // BigEndian
        assertArrayEquals(new byte[]{(byte)0x80, 0x00, 0x00, 0x00, 0x00, 0x00}, out);
    }

    @Test
    public void readLong48() {
        byte[] in = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        long value = DataExchangeUtils.readLong48(in, 0);

        // BigEndian
        Assertions.assertEquals(0x010203040506L, value);
    }

    @Test
    public void readLong48_MaxValue() {
        byte[] in = new byte[]{0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        long value = DataExchangeUtils.readLong48(in, 0);
        // BigEndian
        Assertions.assertEquals(DataExchangeUtils.MAX_LONG48, value);
    }

    @Test
    public void readLong48_MinValue() {
        byte[] in = new byte[]{(byte)0x80, 0x00, 0x00, 0x00, 0x00, 0x00};
        long value = DataExchangeUtils.readLong48(in, 0);
        // BigEndian
        Assertions.assertEquals(DataExchangeUtils.MIN_LONG48, value);
    }

    //endregion

    //region Tests for int

    @Test
    public void writeInt() {
        byte[] out = new byte[4];
        DataExchangeUtils.writeInt(out, 0, 0x04030201);

        // BigEndian
        assertArrayEquals(new byte[]{0x04, 0x03, 0x02, 0x01}, out);
    }

    @Test
    public void writeUnsignedInt() {
        byte[] out = new byte[4];
        DataExchangeUtils.writeUnsignedInt(out, 0, 0xF4F3F2F1L);

        // BigEndian
        assertArrayEquals(new byte[]{(byte) 0xF4, (byte) 0xF3, (byte) 0xF2, (byte) 0xF1}, out);
    }

    @Test
    public void writeIntInvertBytes() {
        byte[] out = new byte[4];
        DataExchangeUtils.writeIntInvertBytes(out, 0, 0xF4F3F2F1);

        // LittleEndian
        assertArrayEquals(new byte[]{(byte) 0xF1, (byte) 0xF2, (byte) 0xF3, (byte) 0xF4}, out);
    }

    @Test
    public void readInt() {
        byte[] in = new byte[]{0x01, 0x02, 0x03, 0x04};
        int value = DataExchangeUtils.readInt(in, 0);

        // BigEndian
        Assertions.assertEquals(0x01020304L, value);
    }

    @Test
    public void readIntInvertBytes() {
        byte[] in = new byte[]{0x01, 0x02, 0x03, 0x04};
        int value = DataExchangeUtils.readIntInvertBytes(in, 0);

        // BigEndian
        Assertions.assertEquals(0x04030201L, value);
    }

    @Test
    public void readUnsignedInt() {
        byte[] in = new byte[]{(byte) 0xF1, (byte) 0xF2, (byte) 0xF3, (byte) 0xF4};
        long value = DataExchangeUtils.readUnsignedInt(in, 0);

        // BigEndian
        Assertions.assertEquals(0xF1F2F3F4L, value);
    }

    // Generated tests

    @Test
    void testReadIntInvertBytes_MaxUnsigned32BitValue() {
        byte[] bytes = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        assertEquals((int) 0xFFFFFFFFL, DataExchangeUtils.readIntInvertBytes(bytes, 0));
    }

    @Test
    void testReadUnsignedInt_ValueWithMSBSet() {
        // This value (0xABCDEF12) would be negative if interpreted as a signed 32-bit integer,
        // but as unsigned, it's a large positive number.
        byte[] bytes = {(byte) 0xAB, (byte) 0xCD, (byte) 0xEF, (byte) 0x12};
        long expected = 0xABCDEF12L; // 2882400018L
        long actual = DataExchangeUtils.readUnsignedInt(bytes, 0);
        assertEquals(expected, actual);
    }

    @Test
    void testReadUnsignedInt_MaxValue() {
        // 0xFFFFFFFF is the maximum value for an unsigned 32-bit integer.
        byte[] bytes = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        long actual = DataExchangeUtils.readUnsignedInt(bytes, 0);
        assertEquals(0xFFFFFFFFL, actual);
    }

    @Test
    void testReadUnsignedInt_MinValue() {
        // 0x00000000 is the minimum value for an unsigned 32-bit integer.
        byte[] bytes = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        long actual = DataExchangeUtils.readUnsignedInt(bytes, 0);
        assertEquals(0x00000000L, actual);
    }

    //endregion

    //region Tests for short

    @Test
    public void writeShort() {
        byte[] out = new byte[2];
        DataExchangeUtils.writeShort(out, 0, (short) 0x0201);

        // BigEndian
        assertArrayEquals(new byte[]{0x02, 0x01}, out);
    }

    @Test
    public void writeUnsignedShort() {
        byte[] out = new byte[2];
        DataExchangeUtils.writeUnsignedShort(out, 0, 0xF2F1);

        // BigEndian
        assertArrayEquals(new byte[]{(byte) 0xF2, (byte) 0xF1}, out);
    }

    @Test
    public void writeShortInvertBytes() {
        byte[] out = new byte[2];
        DataExchangeUtils.writeShortInvertBytes(out, 0, (short) 0xF2F1);

        // LittleEndian
        assertArrayEquals(new byte[]{(byte) 0xF1, (byte) 0xF2}, out);
    }

    @Test
    public void readShort() {
        byte[] in = new byte[]{0x01, 0x02};
        int value = DataExchangeUtils.readShort(in, 0);

        // BigEndian
        Assertions.assertEquals(0x0102L, value);
    }

    @Test
    public void readShortInvertBytes() {
        byte[] in = new byte[]{0x01, 0x02};
        int value = DataExchangeUtils.readShortInvertBytes(in, 0);

        // BigEndian
        Assertions.assertEquals(0x0201L, value);
    }

    @Test
    public void readUnsignedShort() {
        byte[] in = new byte[]{(byte) 0xF1, (byte) 0xF2};
        long value = DataExchangeUtils.readUnsignedShort(in, 0);

        // BigEndian
        Assertions.assertEquals(0xF1F2, value);
    }

    @Test
    public void readUnsignedShort_MaxValue() {
        byte[] in = new byte[]{(byte) 0xFF, (byte) 0xFF};
        long value = DataExchangeUtils.readUnsignedShort(in, 0);

        // BigEndian
        Assertions.assertEquals(0xFFFF, value);
    }

    //endregion

    //region Tests for char

    @Test
    public void writeChar() {
        byte[] out = new byte[2];
        DataExchangeUtils.writeChar(out, 0, (char) 0x0201);

        // BigEndian
        assertArrayEquals(new byte[]{0x02, 0x01}, out);
    }

    @Test
    public void writeCharInvertBytes() {
        byte[] out = new byte[2];
        DataExchangeUtils.writeCharInvertBytes(out, 0, (char) 0xF2F1);

        // LittleEndian
        assertArrayEquals(new byte[]{(byte) 0xF1, (byte) 0xF2}, out);
    }

    @Test
    public void readChar() {
        byte[] in = new byte[]{0x01, 0x02};
        int value = DataExchangeUtils.readChar(in, 0);

        // BigEndian
        Assertions.assertEquals(0x0102L, value);
    }

    @Test
    public void readChar_Max() {
        byte[] in = new byte[]{(byte)0xFF, (byte)0xFF};
        int value = DataExchangeUtils.readChar(in, 0);

        // BigEndian
        Assertions.assertEquals(0xFFFFL, value);
    }

    // Generated tests for readChar

    @Test
    void testReadChar_SimpleValue() {
        byte[] bytes = {(byte) 0xAB, (byte) 0xCD};
        char result = DataExchangeUtils.readChar(bytes, 0);
        assertEquals((char) 0xABCD, result);
    }

    @Test
    void testReadChar_WithOffset() {
        byte[] bytes = {(byte) 0x01, (byte) 0xAB, (byte) 0xCD, (byte) 0x02};
        char result = DataExchangeUtils.readChar(bytes, 1);
        assertEquals((char) 0xABCD, result);
    }

    @Test
    void testReadChar_ZeroValue() {
        byte[] bytes = {(byte) 0x00, (byte) 0x00};
        char result = DataExchangeUtils.readChar(bytes, 0);
        assertEquals((char) 0x0000, result);
    }

    @Test
    void testReadChar_MaxValue() {
        byte[] bytes = {(byte) 0xFF, (byte) 0xFF};
        char result = DataExchangeUtils.readChar(bytes, 0);
        assertEquals((char) 0xFFFF, result);
    }

    @Test
    void testReadChar_SpecificExample() {
        byte[] bytes = {(byte) 0xF1, (byte) 0xF2};
        char result = DataExchangeUtils.readChar(bytes, 0);
        assertEquals((char) 0xF1F2, result);
    }

    // Tests for writeChar
    @Test
    void testWriteChar_SimpleValue() {
        char valueToWrite = (char) 0xABCD;
        byte[] buffer = new byte[2];
        DataExchangeUtils.writeChar(buffer, 0, valueToWrite);
        assertArrayEquals(new byte[]{(byte) 0xAB, (byte) 0xCD}, buffer);
        assertEquals(valueToWrite, DataExchangeUtils.readChar(buffer, 0));
    }

    @Test
    void testWriteChar_WithOffset() {
        char valueToWrite = (char) 0x1234;
        byte[] buffer = new byte[4];
        // Initialize buffer to check non-written parts are untouched
        buffer[0] = (byte) 0xAA;
        buffer[3] = (byte) 0xBB;
        DataExchangeUtils.writeChar(buffer, 1, valueToWrite);
        assertArrayEquals(new byte[]{(byte) 0xAA, (byte) 0x12, (byte) 0x34, (byte) 0xBB}, buffer);
        assertEquals(valueToWrite, DataExchangeUtils.readChar(buffer, 1));
    }

    @Test
    void testWriteChar_ZeroValue() {
        char valueToWrite = (char) 0x0000;
        byte[] buffer = new byte[2];
        DataExchangeUtils.writeChar(buffer, 0, valueToWrite);
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x00}, buffer);
        assertEquals(valueToWrite, DataExchangeUtils.readChar(buffer, 0));
    }

    @Test
    void testWriteChar_MaxValue() {
        char valueToWrite = (char) 0xFFFF;
        byte[] buffer = new byte[2];
        DataExchangeUtils.writeChar(buffer, 0, valueToWrite);
        assertArrayEquals(new byte[]{(byte) 0xFF, (byte) 0xFF}, buffer);
        assertEquals(valueToWrite, DataExchangeUtils.readChar(buffer, 0));
    }

    @Test
    void testWriteChar_SpecificExampleFromPrompt() {
        char valueToWrite = (char) 0xF1F2;
        byte[] buffer = new byte[2];
        DataExchangeUtils.writeChar(buffer, 0, valueToWrite);
        assertArrayEquals(new byte[]{(byte) 0xF1, (byte) 0xF2}, buffer);
        assertEquals(valueToWrite, DataExchangeUtils.readChar(buffer, 0));
    }

    @Test
    void testWriteCharInvertBytes_SimpleValue() {
        char valueToWrite = (char) 0xABCD; // MSB=AB, LSB=CD
        byte[] buffer = new byte[2];
        DataExchangeUtils.writeCharInvertBytes(buffer, 0, valueToWrite);
        // Expected: LSB first, then MSB -> CD, AB
        assertArrayEquals(new byte[]{(byte) 0xCD, (byte) 0xAB}, buffer);

        // Reading it normally should result in a swapped char value
        char readValue = DataExchangeUtils.readChar(buffer, 0);
        assertEquals((char) 0xCDAB, readValue);
    }

    @Test
    void testWriteCharInvertBytes_WithOffset() {
        char valueToWrite = (char) 0x1234; // MSB=12, LSB=34
        byte[] buffer = new byte[4];
        buffer[0] = (byte) 0xAA;
        buffer[3] = (byte) 0xBB;
        DataExchangeUtils.writeCharInvertBytes(buffer, 1, valueToWrite);
        // Expected at offset 1: LSB first, then MSB -> 34, 12
        assertArrayEquals(new byte[]{(byte) 0xAA, (byte) 0x34, (byte) 0x12, (byte) 0xBB}, buffer);

        char readValue = DataExchangeUtils.readChar(buffer, 1);
        assertEquals((char) 0x3412, readValue);
    }

    @Test
    void testWriteCharInvertBytes_ZeroValue() {
        char valueToWrite = (char) 0x0000;
        byte[] buffer = new byte[2];
        DataExchangeUtils.writeCharInvertBytes(buffer, 0, valueToWrite);
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x00}, buffer);

        char readValue = DataExchangeUtils.readChar(buffer, 0);
        assertEquals((char) 0x0000, readValue); // Swapped 0x0000 is still 0x0000
    }

    @Test
    void testWriteCharInvertBytes_MaxValue() {
        char valueToWrite = (char) 0xFFFF;
        byte[] buffer = new byte[2];
        DataExchangeUtils.writeCharInvertBytes(buffer, 0, valueToWrite);
        assertArrayEquals(new byte[]{(byte) 0xFF, (byte) 0xFF}, buffer);

        char readValue = DataExchangeUtils.readChar(buffer, 0);
        assertEquals((char) 0xFFFF, readValue); // Swapped 0xFFFF is still 0xFFFF
    }

    @Test
    void testWriteCharInvertBytes_SpecificExample() {
        char valueToWrite = (char) 0xF1F2; // MSB=F1, LSB=F2
        byte[] buffer = new byte[2];
        DataExchangeUtils.writeCharInvertBytes(buffer, 0, valueToWrite);
        // Expected: LSB first, then MSB -> F2, F1
        assertArrayEquals(new byte[]{(byte) 0xF2, (byte) 0xF1}, buffer);

        char readValue = DataExchangeUtils.readChar(buffer, 0);
        assertEquals((char) 0xF2F1, readValue);
    }

    //endregion


    @Test
    public void testWalkingBit () {
        byte [] buf = new byte [8];

        for (int i = 0; i < buf.length; i++) {
            int mask = 1;
            for (int b=0; b<8; b++) {
                buf[i] = (byte) mask;

                long expected = getBigEndianLong (buf);
                long actual = DataExchangeUtils.readLong(buf, 0);
                assertEquals (expected, actual, "100 nanos");

                mask = mask << 1;
                buf[i] = 0;
            }
        }
    }

    @Test
    public void testAddingBit () {
        byte [] buf = new byte [8];

        for (int i = 0; i < buf.length; i++) {
            int mask = 1;
            for (int b=0; b<8; b++) {
                buf[i] |= (byte) mask;

                long expected = getBigEndianLong (buf);
                long actual = DataExchangeUtils.readLong(buf, 0);
                assertEquals (expected, actual,"100 nanos");

                mask = mask << 1;
            }
        }
    }

    @Test
    public void testGetBigEndianLong () {
        assertEquals (0,                                                                    getBigEndianLong(new byte [] {0,0,0,0,0,0,0,0}));
        assertEquals (1,                                                                    getBigEndianLong(new byte [] {0,0,0,0,0,0,0,1}));
        assertEquals (1 + (1<<8),                                                           getBigEndianLong(new byte [] {0,0,0,0,0,0,1,1}));
        assertEquals (1 + (1<<8) + (1<<16),                                                 getBigEndianLong(new byte [] {0,0,0,0,0,1,1,1}));
        assertEquals (1 + (1<<8) + (1<<16) + (1<<24),                                       getBigEndianLong(new byte [] {0,0,0,0,1,1,1,1}));
        assertEquals (1 + (1<<8) + (1<<16) + (1<<24) + (1L<<32),                            getBigEndianLong(new byte [] {0,0,0,1,1,1,1,1}));
        assertEquals (1 + (1<<8) + (1<<16) + (1<<24) + (1L<<32)+(1L<<40),                   getBigEndianLong(new byte [] {0,0,1,1,1,1,1,1}));
        assertEquals (1 + (1<<8) + (1<<16) + (1<<24) + (1L<<32)+(1L<<40)+(1L<<48),          getBigEndianLong(new byte [] {0,1,1,1,1,1,1,1}));
        assertEquals (1 + (1<<8) + (1<<16) + (1<<24) + (1L<<32)+(1L<<40)+(1L<<48)+(1L<<56), getBigEndianLong(new byte [] {1,1,1,1,1,1,1,1}));
        assertEquals (-1L, getBigEndianLong(new byte [] {(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF}));

    }

    public static long      getBigEndianLong (byte [] bytes) {
        long result =
                (0xFF & bytes[7])         +
               ((0xFF & bytes[6])  <<  8) +
               ((0xFF & bytes[5])  << 16) +
               ((0xFFL & bytes[4]) << 24) +
               ((0xFFL & bytes[3]) << 32) +
               ((0xFFL & bytes[2]) << 40) +
               ((0xFFL & bytes[1]) << 48) +
               ((0xFFL & bytes[0]) << 56);

        return result;
    }

//    @Test
//    public void benchmark () {
//
//        byte [] bytes1 = new byte [] {(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
//        byte [] bytes2 = new byte [] {(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
//        byte [] bytes3 = new byte [] {(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
//        byte [] bytes4 = new byte [] {(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
//        long l1 = 0;
//        final byte mask = (byte)0xCC;
//        for (int i=0; i < 120000; i++) {
//            bytes1[i%8] = mask;
//            l1 += DataExchangeUtils.readLong(bytes1, 0);
//        }
//
//        long l2 = 0;
//        for (int i=0; i < 120000; i++) {
//            bytes2[i%8] = mask;
//            l2 += Test_DataExchangeUtils.getBigEndianLong(bytes2);
//        }
//
//        long t0 = System.currentTimeMillis();
//        for (int i=0; i < 15000000; i++) {
//            bytes3[i%8] = mask;
//            l1 += Test_DataExchangeUtils.getBigEndianLong(bytes3);
//        }
//
//        long t1 = System.currentTimeMillis();
//        for (int i=0; i < 15000000; i++) {
//            bytes4[i%8] = mask;
//            l2 += DataExchangeUtils.readLong(bytes4, 0);
//        }
//        long t2 = System.currentTimeMillis();
//
//
//        System.out.println("Time1: " + (t1-t0) + " time2: " + (t2-t1) + " l1=" + l1 + " l2=" + l2);
//    }

}
