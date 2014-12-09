package deltix.util.runtime;

public final class Shutdown {

    private final static Object lock = new Object();
    private static boolean      terminated;

    public static void          terminate() {
        synchronized (lock) {
            terminated = true;
        }

        exit(1001);
    }

    public static void          asyncTerminate() {
        synchronized (lock) {
            terminated = true;
        }

        new Thread() {
            @Override
            public void run() {
                exit(1001);
            }
        }.start();
    }

    public static boolean       isTerminated() {
        synchronized (lock) {
            return terminated;
        }
    }

    public static void          exit(int code) {
        System.exit(code);
    }
}
