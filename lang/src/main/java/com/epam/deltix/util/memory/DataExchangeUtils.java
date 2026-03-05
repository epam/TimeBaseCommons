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


import com.epam.deltix.dfp.Decimal64;
import com.epam.deltix.hdtime.*;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;

/**
 *  Reads/writes primitive values from/to an array of bytes,
 *	in <b>precisely</b> the same format as DataInput/DataOutput.
 */
public class DataExchangeUtils {
    public static final long        MAX_LONG48 = 0x00007FFFFFFFFFFFL;
    public static final long        MIN_LONG48 = 0xFFFF800000000000L;

    // Big-endian array access
    private static final VarHandle BE_LONG_HANDLE = MethodHandles.byteArrayViewVarHandle(long[].class, ByteOrder.BIG_ENDIAN);
    private static final VarHandle BE_INT_HANDLE = MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.BIG_ENDIAN);
    private static final VarHandle BE_SHORT_HANDLE = MethodHandles.byteArrayViewVarHandle(short[].class, ByteOrder.BIG_ENDIAN);
    private static final VarHandle BE_CHAR_HANDLE = MethodHandles.byteArrayViewVarHandle(char[].class, ByteOrder.BIG_ENDIAN);
    // Little-endian array access
    private static final VarHandle LE_LONG_HANDLE = MethodHandles.byteArrayViewVarHandle(long[].class, ByteOrder.LITTLE_ENDIAN);
    private static final VarHandle LE_INT_HANDLE = MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.LITTLE_ENDIAN);
    private static final VarHandle LE_SHORT_HANDLE = MethodHandles.byteArrayViewVarHandle(short[].class, ByteOrder.LITTLE_ENDIAN);
    private static final VarHandle LE_CHAR_HANDLE = MethodHandles.byteArrayViewVarHandle(char[].class, ByteOrder.LITTLE_ENDIAN);


    public static int		readByte (byte [] bytes, int offset) {
        return (((int) bytes [offset]) & 0xFF);
    }

    public static void		writeByte (byte [] bytes, int offset, int byt) {
        bytes [offset] = (byte) byt;
    }

    public static void		writeByte (byte [] bytes, int offset, byte byt) {
        bytes [offset] = byt;
    }

    public static void		writeByte7 (byte [] bytes, int offset, byte byt) {
        bytes [offset] = (byte) (byt & 0x7F);
    }

    public static void		writeByte7 (byte [] bytes, int offset, int byt) {
        bytes [offset] = (byte) (byt & 0x7F);
    }

    private static void		b (byte [] bytes, int offset, long byt) {
        bytes [offset] = (byte) byt;
    }

    private static long		lb (byte [] bytes, int offset) {
        return (((long) bytes [offset]) & 0xFF);
    }

    public static byte   	readByte7 (byte [] bytes, int offset) {
        return ((byte) (bytes [offset] << 25 >> 25));
    }

    public static short   	readShort (byte [] bytes, int offset) {
        return (short) BE_SHORT_HANDLE.get(bytes, offset);
    }

    public static short   	readShortInvertBytes (byte [] bytes, int offset) {
        return (short) LE_SHORT_HANDLE.get(bytes, offset);
    }

    public static short   	readShort15 (byte [] bytes, int offset) {
        return ((short) (readShort (bytes, offset) << 17 >> 17));
    }

    public static int   	readUnsignedShort (byte [] bytes, int offset) {
        short value = (short) BE_SHORT_HANDLE.get(bytes, offset);
        return value & 0xFFFF;
    }

    public static void   	writeShort (byte [] bytes, int offset, short s) {
        BE_SHORT_HANDLE.set(bytes, offset, s);
    }

    public static void   	writeShortInvertBytes (byte [] bytes, int offset, short s) {
        LE_SHORT_HANDLE.set(bytes, offset, s);
    }

    public static void   	writeShort15 (byte [] bytes, int offset, short s) {
        writeUnsignedShort (bytes, offset, s & 0x7FFF);
    }

    public static void   	writeUnsignedShort (byte [] bytes, int offset, int s) {
        BE_SHORT_HANDLE.set(bytes, offset, (short) s);
    }

    public static char   	readChar (byte [] bytes, int offset) {
        return (char) BE_CHAR_HANDLE.get(bytes, offset);
    }

    public static void   	writeChar (byte [] bytes, int offset, char s) {
        BE_CHAR_HANDLE.set(bytes, offset, s);
    }

    public static void   	writeCharInvertBytes (byte [] bytes, int offset, char s) {
        LE_CHAR_HANDLE.set(bytes, offset, s);
    }

    public static int   	readInt (byte [] bytes, int offset) {
        return (int) BE_INT_HANDLE.get(bytes, offset);
    }

    public static int    	readInt31 (byte [] bytes, int offset) {
        return (readInt (bytes, offset) << 1 >> 1);
    }

    public static int   	readIntInvertBytes (byte [] bytes, int offset) {
        return (int) LE_INT_HANDLE.get(bytes, offset);
    }

    public static void   	writeInt (byte [] bytes, int offset, int i) {
        BE_INT_HANDLE.set(bytes, offset, i);
    }

    public static void   	writeInt31 (byte [] bytes, int offset, int i) {
        writeInt (bytes, offset, i & 0x7FFFFFFF);
    }

    public static void   	writeUnsignedInt (byte [] bytes, int offset, long i) {
        BE_INT_HANDLE.set(bytes, offset, (int) i);
    }

    public static void   	writeIntInvertBytes (byte [] bytes, int offset, int i) {
        LE_INT_HANDLE.set(bytes, offset, i);
    }

    public static float   	readFloat (byte [] bytes, int offset) {
        return (Float.intBitsToFloat (readInt (bytes, offset)));
    }

    public static void   	writeFloat (byte [] bytes, int offset, float f) {
		writeInt (bytes, offset, Float.floatToIntBits (f));
    }

    public static void   	writeFloatInvertBytes (byte [] bytes, int offset, float f) {
        writeIntInvertBytes (bytes, offset, Float.floatToIntBits (f));
    }

    /**
     * @deprecated use {@link #readLong(byte[], int)} instead
     */
    @Deprecated(forRemoval = true)
    public static long  	readLongOld (byte [] bytes, int offset) {
        return (
			((long) bytes [offset]) << 56 |
			lb (bytes, offset + 1) << 48 |
			lb (bytes, offset + 2) << 40 |
			lb (bytes, offset + 3) << 32 |
			lb (bytes, offset + 4) << 24 |
			readByte (bytes, offset + 5) << 16 |
			readByte (bytes, offset + 6) << 8 |
			readByte (bytes, offset + 7)
		);
    }

    public static long readLong(byte[] b, int a) {
        return (long) BE_LONG_HANDLE.get(b, a);
    }

    /**
     *  Useful for compressing timestamps. 48 bits cover approximately
     *  years -2000 .. 6000, which is usually enough.
     */
    public static long  	readLong48 (byte [] bytes, int offset) {
        // Note: using VarHandles for long48 is not effective as for other types
        // because it requires two calls to VarHandle.get() but it's still a bit faster than
        // reading 6 bytes separately.

        // This implementation reads 6 byte signed value as 4-byte signed int and 2-byte unsigned char.
        int highBits = (int) BE_INT_HANDLE.get(bytes, offset);
        // We use char instead of short to avoid needless sign extension
        char lowBits = (char) BE_CHAR_HANDLE.get(bytes, offset + Integer.BYTES);
        return (long) highBits << Short.SIZE | lowBits;
    }

    public static long  	readUnsigned40 (byte [] bytes, int offset) {
        // TODO: Consider using VarHandle
        return (
			lb (bytes, offset) << 32 |
			lb (bytes, offset + 1) << 24 |
			readByte (bytes, offset + 2) << 16 |
			readByte (bytes, offset + 3) << 8 |
			readByte (bytes, offset + 4)
		);
    }

    /**
     * Java uses big-endian numbers encoding. This method parses little-endian encoded 40-bit (!) long.
     * @see #readLong(byte[], int)
     * @see #readLittleEndianLong(byte[])
     */
    public static long readLittleEndianLong40(byte[] bytes) {
        // TODO: Consider using VarHandle
        return
                (0xFF & bytes[0])         +
               ((0xFF & bytes[1])  <<  8) +
               ((0xFF & bytes[2])  << 16) +
               ((0xFFL & bytes[3]) << 24) +
               ((0xFFL & bytes[4]) << 32);
    }

    /**
     * Java uses big-endian numbers encoding. This method parses little-endian encoded long.
     * @see #readLong(byte[], int)
     */
    public static long      readLittleEndianLong (byte [] bytes) {
        return (long) LE_LONG_HANDLE.get(bytes, 0);
    }

    /**
     * Java uses big-endian numbers encoding. This method parses little-endian encoded 40-bit (!) long.
     * @see #readLong(byte[], int)
     * @see #readLittleEndianLong(byte[])
     */
    public static long readLittleEndianLong40(byte[] bytes, int offset) {
        // TODO: Consider using VarHandle
        return
                (0xFF  & bytes[offset])         +
               ((0xFF  & bytes[offset+1]) <<  8) +
               ((0xFF  & bytes[offset+2]) << 16) +
               ((0xFFL & bytes[offset+3]) << 24) +
               ((0xFFL & bytes[offset+4]) << 32) ;
    }

    /**
     * Java uses big-endian numbers encoding. This method parses little-endian encoded long.
     * @see #readLong(byte[], int)
     */
    public static long      readLittleEndianLong (byte [] bytes, int offset) {
        return (long) LE_LONG_HANDLE.get(bytes, offset);
    }

    public static long    	readLong63 (byte [] bytes, int offset) {
        return (readLong (bytes, offset) << 1 >> 1);
    }

    public static long  	readUnsignedInt (byte [] bytes, int offset) {
        int value = (int) BE_INT_HANDLE.get(bytes, offset);
        return value & 0xFFFFFFFFL;
    }

    public static void  	writeLong (byte [] bytes, int offset, long l) {
        BE_LONG_HANDLE.set(bytes, offset, l);
    }

    public static void   	writeLongInvertBytes (byte [] bytes, int offset, long l) {
        LE_LONG_HANDLE.set(bytes, offset, l);
    }

    public static void  	writeLong63 (byte [] bytes, int offset, long l) {
        writeLong (bytes, offset, l & 0x7FFFFFFFFFFFFFFFL);
    }

    /**
     *  Useful for compressing timestamps. 48 bits cover approximately
     *  years -2000 .. 6000, which is usually enough.
     */
    public static void  	writeLong48 (byte [] bytes, int offset, long l) {
        assert l <= MAX_LONG48 && l >= MIN_LONG48 : l;

        // Note: using VarHandles for long48 is not effective as for other types
        // because it requires two calls to VarHandle.set() but it's still a bit faster than
        // writing 6 bytes separately.

        // This implementation writes 6 byte signed value as 4-byte signed int and 2-byte signed char
        BE_INT_HANDLE.set(bytes, offset, (int) (l >>> Short.SIZE));
        BE_SHORT_HANDLE.set(bytes, offset + Integer.BYTES, (short) l);
    }

    /**
     *  Useful for compressing timestamps. 48 bits cover approximately
     *  years -2000 .. 6000, which is usually enough.
     */
    public static void  	writeUnsigned40 (byte [] bytes, int offset, long l) {
        assert l <= 0xFFFFFFFFFFL && l >= 0 : l;

		b (bytes, offset,     l >>> 32);
		b (bytes, offset + 1, l >>> 24);
		b (bytes, offset + 2, l >>> 16);
		b (bytes, offset + 3, l >>> 8);
		b (bytes, offset + 4, l);
    }

    public static double   	readDouble (byte [] bytes, int offset) {
        return (Double.longBitsToDouble (readLong (bytes, offset)));
    }

    public static void   	writeDouble (byte [] bytes, int offset, double d) {
        writeLong (bytes, offset, Double.doubleToLongBits (d));
    }

    public static void   	writeDoubleInvertBytes (byte [] bytes, int offset, double d) {
        writeLongInvertBytes (bytes, offset, Double.doubleToLongBits (d));
    }

    public static Decimal64 readDecimal (byte [] bytes, int offset) {
        return Decimal64.fromUnderlying(readLong(bytes, offset));
    }

    public static HdDateTime readHdDateTime (byte [] bytes, int offset) {
        return HdDateTime.fromUnderlying(readLong(bytes, offset));
    }

    public static HdTimeSpan readHdTimeSpan (byte [] bytes, int offset) {
        return HdTimeSpan.fromUnderlying(readLong(bytes, offset));
    }
}