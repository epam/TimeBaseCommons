package com.epam.deltix.util.lang;

import java.io.Closeable;

/** Simple mechanism to ensure that {@link Closeable#close()} is called on JVM shutdown (e.g. in event of Ctrl+C) */
public class ShutdownHook extends Thread {
    private final Closeable [] closeables;

    public ShutdownHook(Closeable ... closeables) {
        this.closeables = closeables;
    }

    public static void closeOnShutdown (Closeable ... closeables) {
        ShutdownHook shutdownHook = new ShutdownHook(closeables);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    @Override
    public void run() {
        for (Closeable closeable : closeables)
            Util.close (closeable);
    }
}
