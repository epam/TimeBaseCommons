package com.epam.deltix.util.memory;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

import com.epam.deltix.util.lang.Bits;

public final class UnsafeAccess {

    public static final Unsafe UNSAFE;

    static {
        try {
            // This is a bit of voodoo to force the UNSAFE object into
            // visibility and acquire it.
            // This is not playing nice, but as an established back door it is
            // not likely to be
            // taken away.
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UnsafeAccess() {
        throw new AssertionError("Not for you!");
    }

    public static int arrayBaseOffset(Class clazz) {
        return UNSAFE.arrayBaseOffset(clazz);
    }

    public static int arrayIndexScale(Class clazz) {
        int scale = UNSAFE.arrayIndexScale(clazz);

        if (!Bits.isPowerOfTwo(scale))
            throw new Error("data type scale not a power of two: " + clazz);

        return scale;
    }

}

