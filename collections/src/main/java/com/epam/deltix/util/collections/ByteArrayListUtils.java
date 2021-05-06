package com.epam.deltix.util.collections;
import com.epam.deltix.util.collections.generated.ByteArrayList;

import java.util.UUID;

/**
 * Created by DriapkoA on 02.05.2016.
 */
public class ByteArrayListUtils{
    private static ThreadLocal<StringBuilder> threadLocalBuilder = new ThreadLocal<StringBuilder>() {
        @Override
        protected StringBuilder initialValue() {
            return new StringBuilder();
        }
    };

    /**
     * Assign binary array ar by x
     * @param ar binary array
     * @param x x
     * @return this
     */
    public static ByteArrayList assign(ByteArrayList ar, byte x) {
        if (ar == null) ar = new ByteArrayList();
        ar.clear();
        append(ar, x);
        return ar;
    }

    /**
     * Assign binary array ar by x
     * @param ar binary array
     * @param x x
     * @return this
     */
    public static ByteArrayList assign(ByteArrayList ar, short x) {
        if (ar == null) ar = new ByteArrayList();
        ar.clear();
        append(ar, x);
        return ar;
    }
    /**
     * Assign binary array ar by x
     * @param ar binary array
     * @param x x
     * @return this
     */
    public static ByteArrayList assign(ByteArrayList ar, int x) {
        if (ar == null) ar = new ByteArrayList();
        ar.clear();
        append(ar, x);
        return ar;
    }
    /**
     * Assign binary array ar by x
     * @param ar binary array
     * @param x x
     * @return this
     */
    public static ByteArrayList assign(ByteArrayList ar, long x) {
        if (ar == null) ar = new ByteArrayList();
        ar.clear();
        append(ar, x);
        return ar;
    }
    /**
     * Assign binary array ar by x
     * @param ar binary array
     * @param x x
     * @return this
     */
    public static ByteArrayList assign(ByteArrayList ar, String x) {
        if (ar == null) ar = new ByteArrayList();
        ar.clear();
        append(ar, x);
        return ar;
    }
    /**
     * Assign binary array ar by x
     * @param ar binary array
     * @param x x
     * @return this
     */
    public static ByteArrayList assign(ByteArrayList ar, CharSequence x) {
        if (ar == null) ar = new ByteArrayList();
        ar.clear();
        append(ar, x);
        return ar;
    }
    /**
     * Assign binary array ar by x
     * @param ar binary array
     * @param x x
     * @return this
     */
    public static ByteArrayList assign(ByteArrayList ar, ByteArrayList x) {
        if (ar == null) ar = new ByteArrayList();
        ar.clear();
        append(ar, x);
        return ar;
    }
    /**
     * Assign binary array ar by x
     * @param ar binary array
     * @param id id
     * @return this
     */
    public static ByteArrayList assign(ByteArrayList ar, UUID id) {
        if (ar == null) ar = new ByteArrayList();
        ar.clear();
        append(ar, id);
        return ar;
    }
    /**
     * Append x to binary array ar
     * @param ar binary array
     * @param x x
     * @return this
     */
    public static ByteArrayList append(ByteArrayList ar, byte x) {
        if (ar == null) ar = new ByteArrayList();
        ar.add(x);
        return ar;
    }
    /**
     * Append x to binary array ar
     * @param ar binary array
     * @param x x
     * @return this
     */

    public static ByteArrayList append(ByteArrayList ar, short x) {
        if (ar == null) ar = new ByteArrayList();
        ar.add((byte)(x & 255));
        ar.add((byte)(x >>> 8));
        return ar;
    }
    /**
     * Append x to binary array ar
     * @param ar binary array
     * @param x x
     * @return this
     */
    public static ByteArrayList append(ByteArrayList ar, int x) {
        if (ar == null) ar = new ByteArrayList();
        ar.add((byte)(x & 255));
        ar.add((byte)((x >>> 8) & 255));
        ar.add((byte)((x >>> 16) & 255));
        ar.add((byte)(x >>> 24));
        return ar;
    }
    /**
     * Append x to binary array ar
     * @param ar binary array
     * @param x x
     * @return this
     */
    public static ByteArrayList append(ByteArrayList ar, long x) {
        if (ar == null) ar = new ByteArrayList();
        ar.add((byte)(x & 255));
        ar.add((byte)((x >>> 8) & 255));
        ar.add((byte)((x >>> 16) & 255));
        ar.add((byte)((x >>> 24) & 255));
        ar.add((byte)((x >>> 32) & 255));
        ar.add((byte)((x >>> 40) & 255));
        ar.add((byte)((x >>> 48) & 255));
        ar.add((byte)(x >>> 56));
        return ar;
    }
    /**
     * Append x to binary array ar, x will be interpreted as Big-endian long.
     * @param ar binary array
     * @param x x
     * @return this
     */
    public static ByteArrayList appendBigEndian(ByteArrayList ar, long x) {
        if (ar == null) ar = new ByteArrayList();
        ar.add((byte)(x >>> 56));
        ar.add((byte)((x >>> 48) & 255));
        ar.add((byte)((x >>> 40) & 255));
        ar.add((byte)((x >>> 32) & 255));
        ar.add((byte)((x >>> 24) & 255));
        ar.add((byte)((x >>> 16) & 255));
        ar.add((byte)((x >>> 8) & 255));
        ar.add((byte)(x & 255));
        return ar;
    }
    /**
     * Append x to binary array ar
     * @param ar binary array
     * @param x x
     * @return this
     */
    public static ByteArrayList append(ByteArrayList ar, String x) {
        if (ar == null) ar = new ByteArrayList();
        if (x == null) return ar;
        for (int i = 0; i < x.length(); ++i) {
            char ch = x.charAt(i);
            append(ar, (short) ch);
        }
        return ar;
    }
    /**
     * Append x to binary array ar
     * @param ar binary array
     * @param x x
     * @return this
     */
    public static ByteArrayList append(ByteArrayList ar, CharSequence x) {
        if (ar == null) ar = new ByteArrayList();
        if (x == null) return ar;
        for (int i = 0; i < x.length(); ++i) {
            char ch = x.charAt(i);
            append(ar, (short) ch);
        }
        return ar;
    }
    /**
     * Append x to binary array ar
     * @param ar binary array
     * @param x x
     * @return this
     */
    public static ByteArrayList append(ByteArrayList ar, ByteArrayList x) {
        if (ar == null) ar = new ByteArrayList();
        if (x == null) return ar;
        ar.addAll(x.getInternalBuffer(), 0, x.size());
        return ar;
    }
    /**
     * Append x to binary array ar
     * @param ar binary array
     * @param id UUID
     * @return this
     */
    public static ByteArrayList append(ByteArrayList ar, UUID id) {
        if (ar == null) ar = new ByteArrayList();
        if (id == null) return ar;
        ar = ByteArrayListUtils.appendBigEndian(ar, id.getMostSignificantBits());
        ar = ByteArrayListUtils.appendBigEndian(ar, id.getLeastSignificantBits());
        return ar;
    }
    /**
     * Append x to binary array ar
     * @param ar binary array
     * @param x x
     * @return this
     */

    public static ByteArrayList appendASCII(ByteArrayList ar, String x) {
        if (ar == null) ar = new ByteArrayList();
        if (x == null) return ar;
        for (int i = 0; i < x.length(); ++i) {
            char ch = x.charAt(i);
            append(ar, (byte) ch);
        }
        return ar;
    }

    /**
     * Convert binary array, started from offset to byte
     * @param ar binary array
     * @param offset offset
     * @return result of conversion
     */
    public static byte toByte(ByteArrayList ar, int offset) {
        return ar.getByte(offset);
    }
    /**
     * Convert binary array, started from offset to short
     * @param ar binary array
     * @param offset offset
     * @return result of conversion
     */
    public static short toShort(ByteArrayList ar, int offset) {
        return (short) ((((short)(ar.getByte(offset + 1)) << 8) & (short)(0xff00)) |
                       ((short) ar.getByte(offset)) & (short)(0xff));
    }
    /**
     * Convert binary array, started from offset to int
     * @param ar binary array
     * @param offset offset
     * @return result of conversion
     */
    public static int toInt(ByteArrayList ar, int offset) {
        return ((int)(ar.getByte(offset + 3)) << 24) & 0xff000000 |
                ((int)(ar.getByte(offset + 2)) << 16) & 0x00ff0000 |
                ((int)(ar.getByte(offset + 1)) << 8)&0x0000ff00 |
                ((int)ar.getByte(offset)) & 0x000000ff;
    }
    /**
     * Convert binary array, started from offset to long
     * @param ar binary array
     * @param offset offset
     * @return result of conversion
     */
    public static long toLong(ByteArrayList ar, int offset) {
        return ((long) (ar.getByte(offset + 7)) << 56) & 0xff00000000000000L |
                ((long) (ar.getByte(offset + 6)) << 48) & 0x00ff000000000000L |
                ((long) (ar.getByte(offset + 5)) << 40) & 0x0000ff0000000000L |
                ((long) (ar.getByte(offset + 4)) << 32) & 0x000000ff00000000L |
                ((long) (ar.getByte(offset + 3)) << 24) & 0x00000000ff000000L |
                ((long) (ar.getByte(offset + 2)) << 16) & 0x0000000000ff0000L |
                ((long) (ar.getByte(offset + 1)) << 8) & 0x000000000000ff00L |
                ((long) (ar.getByte(offset))) & 0x000000000000ffL;

    }
    /**
     * Convert binary array, started from offset to Big-endian long
     * @param ar binary array
     * @param offset offset
     * @return result of conversion
     */
    public static long toBigEndianLong(ByteArrayList ar, int offset) {
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
     * @return result of conversion
     */
    public static String toString(ByteArrayList ar, int offset) {
        StringBuilder builder = threadLocalBuilder.get();
        builder.setLength(0);
        for (int i = offset; i < ar.size(); i += 2) {
            builder.append((char)toShort(ar, i));
        }
        return builder.toString();
    }
    /**
     * Convert binary array, started from offset to CharSequence
     * @param ar binary array
     * @param offset offset
     * @param builder result.
     */
    public static void toStringBuilder(ByteArrayList ar, int offset, StringBuilder builder) {
        builder.setLength(0);
        for (int i = offset; i < ar.size(); i += 2) {
            builder.append((char) toShort(ar, i));
        }
    }
    /**
     * Convert binary array, started from offset to ASCII string
     * @param ar binary array
     * @param offset offset
     * @return result of conversion
     */
    public static String toStringASCII(ByteArrayList ar, int offset) {
        StringBuilder builder = threadLocalBuilder.get();
        builder.setLength(0);
        for (int i = offset; i < ar.size(); i++) {
            builder.append((char)toByte(ar, i));
        }
        return builder.toString();
    }

    /**
     * convert binary array to byte
     * @param ar binary array
     * @return result of conversion
     */
    public static byte toByte(ByteArrayList ar) {
        return toByte(ar, 0);
    }

    /**
     * convert binary array to short
     * @param ar binary array
     * @return result of conversion
     */
    public static short toShort(ByteArrayList ar) {
        return toShort(ar, 0);
    }
    /**
     * convert binary array to int
     * @param ar binary array
     * @return result of conversion
     */

    public static int toInt(ByteArrayList ar) {
        return toInt(ar, 0);
    }
    /**
     * convert binary array to long
     * @param ar binary array
     * @return result of conversion
     */
    public static long toLong(ByteArrayList ar) {
        return toLong(ar, 0);
    }
    /**
     * convert binary array to string
     * @param ar binary array
     * @return result of conversion
     */

    public static String toString(ByteArrayList ar) {
        return toString(ar, 0);
    }
    /**
     * convert binary array to CharSequence
     * @param ar binary array
     * @param builder result
     */

    public static void toStringBuilder(ByteArrayList ar, StringBuilder builder) {
        toStringBuilder(ar, 0, builder);
    }
    /**
     * convert binary array to ASCII string
     * @param ar binary array
     * @return result of conversion
     */

    public static String toStringASCII(ByteArrayList ar) {
        return toStringASCII(ar, 0);
    }

    /**
     * convert binary array to UUID
     * @param ar binary array
     * @return result of conversion
     */
    public static UUID toUUID(ByteArrayList ar) {
        if (ar.size() != 16)
            return null;
        return new UUID(toBigEndianLong(ar, 0), toBigEndianLong(ar, 8));
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
