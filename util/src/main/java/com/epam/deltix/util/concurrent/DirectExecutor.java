package com.epam.deltix.util.concurrent;

import java.util.concurrent.Executor;

/**
 * Executes submitted task immediately in the current thread.
 */
public class DirectExecutor implements Executor {
    public static final Executor INSTANCE = new DirectExecutor();

    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
