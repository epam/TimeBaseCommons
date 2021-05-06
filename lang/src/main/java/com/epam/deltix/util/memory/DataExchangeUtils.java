package com.epam.deltix.util.memory;


import com.epam.deltix.dfp.Decimal64;
import com.epam.deltix.hdtime.HdDateTime;
import com.epam.deltix.hdtime.HdTimeSpan;

/**
 *  Reads/writes primitive values from/to an array of bytes,
 *	in <b>precisely</b> the same format as DataInput/DataOutput.
 */
public class DataExchangeUtils {
    public static final long        MAX_LONG48 = 0x00007FFFFFFFFFFFL;
    public static final long        MIN_LONG48 = 0xFFFF800000000000L;

    public static int		readByte (byte [] bytes, int offset) {
        return (((int) bytes [offset]) & 0xFF);
    }

    public static void		writeByte (byte [] bytes, int offset, int byt) {
        bytes [offset] = (byte) (byt & 0xFF);
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
        bytes [offset] = (byte) (byt & 0xFF);
    }

    private static long		lb (byte [] bytes, int offset) {
        return (((long) bytes [offset]) & 0xFF);
    }

    public static byte   	readByte7 (byte [] bytes, int offset) {
        return ((byte) (bytes [offset] << 25 >> 25));
    }

    public static short   	readShort (byte [] bytes, int offset) {
        return ((short)
			(readByte (bytes, offset) << 8 |
			 readByte (bytes, offset + 1))
		);
    }

    public static short   	readShortInvertBytes (byte [] bytes, int offset) {
        return ((short)
			(readByte (bytes, offset) |
			 readByte (bytes, offset + 1) << 8)
		);
    }

    public static short   	readShort15 (byte [] bytes, int offset) {
        return ((short) (readShort (bytes, offset) << 17 >> 17));
    }

    public static int   	readUnsignedShort (byte [] bytes, int offset) {
        return (
			(readByte (bytes, offset) << 8 |
			 readByte (bytes, offset + 1))
		);
    }

    public static void   	writeShort (byte [] bytes, int offset, short s) {
		writeByte (bytes, offset, s >>> 8);
		writeByte (bytes, offset + 1, s);
    }

    public static void   	writeShortInvertBytes (byte [] bytes, int offset, short s) {
        writeByte (bytes, offset, s);
        writeByte (bytes, offset + 1, s >>> 8);
    }

    public static void   	writeShort15 (byte [] bytes, int offset, short s) {
        writeUnsignedShort (bytes, offset, s & 0x7FFF);
    }

    public static void   	writeUnsignedShort (byte [] bytes, int offset, int s) {
		writeByte (bytes, offset, s >>> 8);
		writeByte (bytes, offset + 1, s);
    }

    public static char   	readChar (byte [] bytes, int offset) {
        return ((char)
			(readByte (bytes, offset) << 8 |
			 readByte (bytes, offset + 1))
		);
    }

    public static void   	writeChar (byte [] bytes, int offset, char s) {
		writeByte (bytes, offset, s >>> 8);
		writeByte (bytes, offset + 1, s);
    }

    public static void   	writeCharInvertBytes (byte [] bytes, int offset, char s) {
        writeByte (bytes, offset, s);
        writeByte (bytes, offset + 1, s >>> 8);
    }

    public static int   	readInt (byte [] bytes, int offset) {
        return (
                ((int) bytes [offset]) << 24 |
                readByte (bytes, offset + 1) << 16 |
                readByte (bytes, offset + 2) << 8 |
                readByte (bytes, offset + 3)
		);
    }

    public static int    	readInt31 (byte [] bytes, int offset) {
        return (readInt (bytes, offset) << 1 >> 1);
    }

    public static int   	readIntInvertBytes (byte [] bytes, int offset) {
        return (
			readByte (bytes, offset) |
			readByte (bytes, offset + 1) << 8 |
			readByte (bytes, offset + 2) << 16 |
			readByte (bytes, offset + 3) << 24
		);
    }

    public static void   	writeInt (byte [] bytes, int offset, int i) {
		writeByte (bytes, offset, i >>> 24);
		writeByte (bytes, offset + 1, i >>> 16);
		writeByte (bytes, offset + 2, i >>> 8);
		writeByte (bytes, offset + 3, i);
    }

    public static void   	writeInt31 (byte [] bytes, int offset, int i) {
        writeInt (bytes, offset, i & 0x7FFFFFFF);
    }

    public static void   	writeUnsignedInt (byte [] bytes, int offset, long i) {
		b (bytes, offset, i >>> 24);
		b (bytes, offset + 1, i >>> 16);
		b (bytes, offset + 2, i >>> 8);
		b (bytes, offset + 3, i);
    }

    public static void   	writeIntInvertBytes (byte [] bytes, int offset, int i) {
		writeByte (bytes, offset, i);
		writeByte (bytes, offset + 1, i >>> 8);
		writeByte (bytes, offset + 2, i >>> 16);
		writeByte (bytes, offset + 3, i >>> 24);
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
        return makeLong(
                b[a],
                b[a + 1],
                b[a + 2],
                b[a + 3],
                b[a + 4],
                b[a + 5],
                b[a + 6],
                b[a + 7]);
    }

    private static long makeLong(byte b7, byte b6, byte b5, byte b4,
                                 byte b3, byte b2, byte b1, byte b0)
    {
        return ((((long)b7       ) << 56) |
                (((long)b6 & 0xff) << 48) |
                (((long)b5 & 0xff) << 40) |
                (((long)b4 & 0xff) << 32) |
                (((long)b3 & 0xff) << 24) |
                (((long)b2 & 0xff) << 16) |
                (((long)b1 & 0xff) <<  8) |
                (((long)b0 & 0xff)      ));
    }

    /**
     *  Useful for compressing timestamps. 48 bits cover approximately
     *  years -2000 .. 6000, which is usually enough.
     */
    public static long  	readLong48 (byte [] bytes, int offset) {
        return (
			((long) bytes [offset]) << 40 |
			lb (bytes, offset + 1) << 32 |
			lb (bytes, offset + 2) << 24 |
			readByte (bytes, offset + 3) << 16 |
			readByte (bytes, offset + 4) << 8 |
			readByte (bytes, offset + 5)
		);
    }

    /**
     *  
     */
    public static long  	readUnsigned40 (byte [] bytes, int offset) {
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
        return
                (0xFF & bytes[0])         +
               ((0xFF & bytes[1])  <<  8) +
               ((0xFF & bytes[2])  << 16) +
               ((0xFFL & bytes[3]) << 24) +
               ((0xFFL & bytes[4]) << 32) +
               ((0xFFL & bytes[5]) << 40) +
               ((0xFFL & bytes[6]) << 48) +
               ((0xFFL & bytes[7]) << 56);
    }

    /**
     * Java uses big-endian numbers encoding. This method parses little-endian encoded 40-bit (!) long.
     * @see #readLong(byte[], int)
     * @see #readLittleEndianLong(byte[])
     */
    public static long readLittleEndianLong40(byte[] bytes, int offset) {
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
        return
                (0xFF  & bytes[offset])          +
               ((0xFF  & bytes[offset+1]) <<  8) +
               ((0xFF  & bytes[offset+2]) << 16) +
               ((0xFFL & bytes[offset+3]) << 24) +
               ((0xFFL & bytes[offset+4]) << 32) +
               ((0xFFL & bytes[offset+5]) << 40) +
               ((0xFFL & bytes[offset+6]) << 48) +
               ((0xFFL & bytes[offset+7]) << 56) ;
    }

    public static long    	readLong63 (byte [] bytes, int offset) {
        return (readLong (bytes, offset) << 1 >> 1);
    }

    public static long  	readUnsignedInt (byte [] bytes, int offset) {
        return (
			lb (bytes, offset + 4) << 24 |
			lb (bytes, offset + 5) << 16 |
			lb (bytes, offset + 6) << 8 |
			lb (bytes, offset + 7)
		);
    }

    public static void  	writeLong (byte [] bytes, int offset, long l) {
		b (bytes, offset, l >>> 56);
		b (bytes, offset + 1, l >>> 48);
		b (bytes, offset + 2, l >>> 40);
		b (bytes, offset + 3, l >>> 32);
		b (bytes, offset + 4, l >>> 24);
		b (bytes, offset + 5, l >>> 16);
		b (bytes, offset + 6, l >>> 8);
		b (bytes, offset + 7, l);
    }

    public static void   	writeLongInvertBytes (byte [] bytes, int offset, long l) {
        b (bytes, offset, l);
        b (bytes, offset + 1, l >>> 8);
        b (bytes, offset + 2, l >>> 16);
        b (bytes, offset + 3, l >>> 24);
        b (bytes, offset + 4, l >>> 32);
        b (bytes, offset + 5, l >>> 40);
        b (bytes, offset + 6, l >>> 48);
        b (bytes, offset + 7, l >> 56);
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

		b (bytes, offset, l >>> 40);
		b (bytes, offset + 1, l >>> 32);
		b (bytes, offset + 2, l >>> 24);
		b (bytes, offset + 3, l >>> 16);
		b (bytes, offset + 4, l >>> 8);
		b (bytes, offset + 5, l);
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
