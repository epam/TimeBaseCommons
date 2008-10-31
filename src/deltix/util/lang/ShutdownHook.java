package deltix.util.lang;

import java.io.Closeable;
import java.io.IOException;

/** Simple mechanism to ensure that {@link Closeable#close()} is called on JVM shutdown (e.g. in event of Ctrl+C) */
public class ShutdownHook extends Thread {
    private final Closeable [] closeables;

    public ShutdownHook(Closeable ... closeables) {
        this.closeables = closeables;
    }

    public static void closeOnShutdown (Closeable closeable) {
        ShutdownHook shutdownHook = new ShutdownHook(closeable);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    public void run() {
        for (Closeable closeable : closeables)
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
