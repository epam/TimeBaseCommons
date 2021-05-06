package com.epam.deltix.util.memory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Test_DataExchangeUtils {


    @Test
    public void testWalkingBit () {
        byte [] buf = new byte [8];

        for (int i = 0; i < buf.length; i++) {
            int mask = 1;
            for (int b=0; b<8; b++) {
                buf[i] = (byte) mask;

                long expected = getBigEndianLong (buf);
                long actual = DataExchangeUtils.readLong(buf, 0);
                assertEquals ("100 nanos", expected, actual);

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
                assertEquals ("100 nanos", expected, actual);

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
