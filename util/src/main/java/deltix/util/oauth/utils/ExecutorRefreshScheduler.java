package deltix.util.oauth.utils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ExecutorRefreshScheduler implements RefreshTokenScheduler {
    private final ScheduledExecutorService executor;

    private ScheduledFuture<?> currentTask;

    public ExecutorRefreshScheduler(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public synchronized void schedule(long timestampMs, Runnable task) {
        currentTask = executor.schedule(task, timestampMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public synchronized void close() {
        if (currentTask != null) {
            currentTask.cancel(true);
            currentTask = null;
        }
    }
}
