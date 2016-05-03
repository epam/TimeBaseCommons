package deltix.util.collections;
import java.lang.String;

import deltix.util.collections.generated.ByteArrayList;

/**
 * Created by DriapkoA on 02.05.2016.
 */
public class ByteArrayListUtils{
    /**
     * Assign binary array ar by x
     * @param ar binary array
     * @param x x
     */
    public static void assign(ByteArrayList ar, byte x) {
        ar.clear();
        append(ar, x);
    }

    /**
     * Assign binary array ar by x
     * @param ar binary array
     * @param x x
     */
    public static void assign(ByteArrayList ar, short x) {
        ar.clear();
        append(ar, x);
    }
    /**
     * Assign binary array ar by x
     * @param ar binary array
     * @param x x
     */
    public static void assign(ByteArrayList ar, int x) {
        ar.clear();
        append(ar, x);
    }
    /**
     * Assign binary array ar by x
     * @param ar binary array
     * @param x x
     */
    public static void assign(ByteArrayList ar, long x) {
        ar.clear();
        append(ar, x);
    }
    /**
     * Assign binary array ar by x
     * @param ar binary array
     * @param x x
     */
    public static void assign(ByteArrayList ar, String x) {
        ar.clear();
        append(ar, x);
    }

    /**
     * Append x to binary array ar
     * @param ar binary array
     * @param x x
     */
    public static void append(ByteArrayList ar, byte x) {
        ar.add(x);
    }
    /**
     * Append x to binary array ar
     * @param ar binary array
     * @param x x
     */

    public static void append(ByteArrayList ar, short x) {
        ar.add((byte)(x >>> 8));
        ar.add((byte)(x & 255));

    }
    /**
     * Append x to binary array ar
     * @param ar binary array
     * @param x x
     */
    public static void append(ByteArrayList ar, int x) {
        ar.add((byte)(x >>> 24));
        ar.add((byte)((x >>> 16) & 255));
        ar.add((byte)((x >>> 8) & 255));
        ar.add((byte)(x & 255));
    }
    /**
     * Append x to binary array ar
     * @param ar binary array
     * @param x x
     */
    public static void append(ByteArrayList ar, long x) {
        ar.add((byte)(x >>> 56));
        ar.add((byte)((x >>> 48) & 255));
        ar.add((byte)((x >>> 40) & 255));
        ar.add((byte)((x >>> 32) & 255));
        ar.add((byte)((x >>> 24) & 255));
        ar.add((byte)((x >>> 16) & 255));
        ar.add((byte)((x >>> 8) & 255));
        ar.add((byte)(x & 255));
    }
    /**
     * Append x to binary array ar
     * @param ar binary array
     * @param x x
     */
    public static void append(ByteArrayList ar, String x) {
        for (int i = 0; i < x.length(); ++i) {
            char ch = x.charAt(i);
            append(ar, (short) ch);
        }
    }
    /**
     * Append x to binary array ar
     * @param ar binary array
     * @param x x
     */

    public static void appendASCII(ByteArrayList ar, String x) {
        for (int i = 0; i < x.length(); ++i) {
            char ch = x.charAt(i);
            append(ar, (byte) ch);
        }
    }

    /**
     * Convert binary array, started from offset to byte
     * @param ar binary array
     * @param offset offset
     * @return result of convertation
     */
    public static byte toByte(ByteArrayList ar, int offset) {
        return ar.getByte(offset);
    }
    /**
     * Convert binary array, started from offset to short
     * @param ar binary array
     * @param offset offset
     * @return result of convertation
     */
    public static short toShort(ByteArrayList ar, int offset) {
        return (short) ((((short)(ar.getByte(offset)) << 8) & (short)(0xff00)) |
                       ((short) ar.getByte(offset + 1)) & (short)(0xff));
    }
    /**
     * Convert binary array, started from offset to int
     * @param ar binary array
     * @param offset offset
     * @return result of convertation
     */
    public static int toInt(ByteArrayList ar, int offset) {
        return ((int)(ar.getByte(offset)) << 24) & 0xff000000 |
                ((int)(ar.getByte(offset + 1)) << 16) & 0x00ff0000 |
                ((int)(ar.getByte(offset + 2)) << 8)&0x0000ff00 |
                ((int)ar.getByte(offset + 3)) & 0x000000ff;
    }
    /**
     * Convert binary array, started from offset to long
     * @param ar binary array
     * @param offset offset
     * @return result of convertation
     */
    public static long toLong(ByteArrayList ar, int offset) {
        return ((long) (ar.getByte(offset)) << 56) & 0xff00000000000000L |
                ((long) (ar.getByte(offset + 1)) << 48) & 0x00ff000000000000L |
                ((long) (ar.getByte(offset + 2)) << 40) & 0x0000ff0000000000L |
                ((long) (ar.getByte(offset + 3)) << 32) & 0x000000ff00000000L |
                ((long) (ar.getByte(offset + 4)) << 24) & 0x00000000ff000000L |
                ((long) (ar.getByte(offset + 5)) << 16) & 0x0000000000ff0000L |
                ((long) (ar.getByte(offset + 6)) << 8) & 0x000000000000ff00L |
                ((long) (ar.getByte(offset + 7))) & 0x000000000000ffL;

    }
    /**
     * Convert binary array, started from offset to string
     * @param ar binary array
     * @param offset offset
     * @return result of convertation
     */
    public static String toString(ByteArrayList ar, int offset) {
        StringBuilder builder = new StringBuilder();
        for (int i = offset; i < ar.size(); i += 2) {
            builder.append((char)toShort(ar, i));
        }
        return builder.toString();
    }
    /**
     * Convert binary array, started from offset to ASCII string
     * @param ar binary array
     * @param offset offset
     * @return result of convertation
     */
    public static String toStringASCII(ByteArrayList ar, int offset) {
        StringBuilder builder = new StringBuilder();
        for (int i = offset; i < ar.size(); i++) {
            builder.append((char)toByte(ar, i));
        }
        return builder.toString();
    }

    /**
     * convert binary array to byte
     * @param ar binary array
     * @return result of convertation
     */
    public static byte toByte(ByteArrayList ar) {
        return toByte(ar, 0);
    }

    /**
     * convert binary array to short
     * @param ar binary array
     * @return result of convertation
     */
    public static short toShort(ByteArrayList ar) {
        return toShort(ar, 0);
    }
    /**
     * convert binary array to int
     * @param ar binary array
     * @return result of convertation
     */

    public static int toInt(ByteArrayList ar) {
        return toInt(ar, 0);
    }
    /**
     * convert binary array to long
     * @param ar binary array
     * @return result of convertation
     */
    public static long toLong(ByteArrayList ar) {
        return toLong(ar, 0);
    }
    /**
     * convert binary array to string
     * @param ar binary array
     * @return result of convertation
     */

    public static String toString(ByteArrayList ar) {
        return toString(ar, 0);
    }
    /**
     * convert binary array to ASCII string
     * @param ar binary array
     * @return result of convertation
     */

    public static String toStringASCII(ByteArrayList ar) {
        return toStringASCII(ar, 0);
    }

    /**
     * return hash code of binary array
     * @param ar binary array
     * @return hash code
     */
    public static int getHashCode(ByteArrayList ar) {
        int hashCode = 0;
        for (int i = 0; i < ar.size() ; ++i)
        {
            hashCode = hashCode * 28561;
            hashCode += ar.get(i);
        }
        return hashCode;
    }

    /**
     * return true if two binary arrays equal
     * @param ar binary array
     * @param ar1 other binary array
     * @return true if two binary arrays equal
     */
    public static Boolean equals(ByteArrayList ar, ByteArrayList ar1) {
    	if (ar.size() != ar1.size()) return false;
        for (int i = 0; i < ar.size(); ++i) if (ar1.getByte(i) != ar.getByte(i)) return false;
        return true;

    }
}
