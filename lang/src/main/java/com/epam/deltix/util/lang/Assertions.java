package com.epam.deltix.util.lang;

public final class Assertions {
    public static boolean  ENABLED = false;
    private Assertions() {}

    static {
        try {
            assert false;
        } catch (AssertionError x) {
            ENABLED = true;
        }
    }
}
