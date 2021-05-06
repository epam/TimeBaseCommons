package com.epam.deltix.util.runtime;

public final class Shutdown {

    private final static Object lock = new Object();
    private static boolean      terminated;
    private static int          code = 0;

    public static void          terminate() {
        synchronized (lock) {
            terminated = true;
        }

        System.exit(1001);
    }

    public static void          asyncTerminate() {
        synchronized (lock) {
            terminated = true;
        }

        asyncExit(1001);
    }

    public static int           getCode() {
        return code;
    }

    public static boolean       isTerminated() {
        synchronized (lock) {
            return terminated;
        }
    }

    public static void          asyncExit(final int code) {
        Shutdown.code = code;
        new Thread() {
            @Override
            public void run() {
                System.exit(code);
            }
        }.start();
    }
}
