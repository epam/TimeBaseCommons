package deltix.util.memory;

/**
 *  Reads/writes primitive values from/to an array of bytes,
 *	in <b>precisely</b> the same format as DataInput/DataOutput.
 */
public class DataExchangeUtils {
	private static int		b (byte [] bytes, int offset) {
		return (((int) bytes [offset]) & 0xFF);
	}
	
	private static void		b (byte [] bytes, int offset, int byt) {
		bytes [offset] = (byte) (byt & 0xFF);
	}
	
	private static void		b (byte [] bytes, int offset, long byt) {
		bytes [offset] = (byte) (byt & 0xFF);
	}
	
	private static long		lb (byte [] bytes, int offset) {
		return (((long) bytes [offset]) & 0xFF);
	}
	
    public static short   	readShort (byte [] bytes, int offset) {
        return ((short)
			(b (bytes, offset) << 8 |
			 b (bytes, offset + 1))
		);
    }
    
    public static void   	writeShort (byte [] bytes, int offset, short s) {
		b (bytes, offset, s >>> 8);
		b (bytes, offset + 1, s);
    }
    
    public static char   	readChar (byte [] bytes, int offset) {
        return ((char)
			(b (bytes, offset) << 8 |
			 b (bytes, offset + 1))
		);
    }
    
    public static void   	writeChar (byte [] bytes, int offset, char s) {
		b (bytes, offset, s >>> 8);
		b (bytes, offset + 1, s);
    }
    
    public static int   	readInt (byte [] bytes, int offset) {
        return (
                b (bytes, offset) << 24 |
                b (bytes, offset + 1) << 16 |
                b (bytes, offset + 2) << 8 |
                b (bytes, offset + 3)
		);
    }
    
    public static int   	readIntInvertBytes (byte [] bytes, int offset) {
        return (
			b (bytes, offset) |
			b (bytes, offset + 1) << 8 |
			b (bytes, offset + 2) << 16 |
			b (bytes, offset + 3) << 24
		);
    }
    
    public static void   	writeInt (byte [] bytes, int offset, int i) {
		b (bytes, offset, i >>> 24);
		b (bytes, offset + 1, i >>> 16);
		b (bytes, offset + 2, i >>> 8);
		b (bytes, offset + 3, i);
    }
    
    public static void   	writeIntInvertBytes (byte [] bytes, int offset, int i) {
		b (bytes, offset, i);
		b (bytes, offset + 1, i >>> 8);
		b (bytes, offset + 2, i >>> 16);
		b (bytes, offset + 3, i >>> 24);
    }
    
    public static float   	readFloat (byte [] bytes, int offset) {
        return (Float.intBitsToFloat (readInt (bytes, offset)));
    }
    
    public static void   	writeFloat (byte [] bytes, int offset, float f) {
		writeInt (bytes, offset, Float.floatToIntBits (f));
    }
    
    public static long  	readLong (byte [] bytes, int offset) {
        return (
			lb (bytes, offset) << 56 |
			lb (bytes, offset + 1) << 48 |
			lb (bytes, offset + 2) << 40 |
			lb (bytes, offset + 3) << 32 |
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
    
    public static double   	readDouble (byte [] bytes, int offset) {
        return (Double.longBitsToDouble (readLong (bytes, offset)));
    }
    
    public static void   	writeDouble (byte [] bytes, int offset, double d) {
		writeLong (bytes, offset, Double.doubleToLongBits (d));
    }
    
}
