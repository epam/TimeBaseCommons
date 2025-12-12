package com.epam.deltix.util.lang;

public final class Bits {

    private Bits() {
        throw new AssertionError("Not for you!");
    }

    public static int computeShift(int scale) {
        return Integer.numberOfTrailingZeros(scale);
    }

    public static int nextPowerOfTwo(int value) {
        return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
    }

    public static boolean isPowerOfTwo(int value) {
        return value > 0 && (value & (value - 1)) == 0;
    }

}
