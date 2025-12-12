package com.epam.deltix.util.collections;

/**
 * @author Jacob Alber
 */
public class SafeArrays {

//    @SafeVarargs
//    public static <Generic> Generic[] asArray(Generic... array) {
//        return array;
//    }

    public static <Generic> void safeArrayCopy(Generic[] src, int srcPos, Generic[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    public static void safeArrayCopy(int[] src, int srcPos, int[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    public static void safeArrayCopy(byte[] src, int srcPos, byte[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    public static void safeArrayCopy(short[] src, int srcPos, short[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    public static void safeArrayCopy(long[] src, int srcPos, long[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    public static void safeArrayCopy(double[] src, int srcPos, double[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    public static void safeArrayCopy(float[] src, int srcPos, float[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    public static void safeArrayCopy(char[] src, int srcPos, char[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    public static void safeArrayCopy(boolean[] src, int srcPos, boolean[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }
}
