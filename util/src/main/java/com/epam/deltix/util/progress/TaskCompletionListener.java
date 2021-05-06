package com.epam.deltix.util.progress;

/**
 *
 */
public interface TaskCompletionListener {
    public void         taskCompleted (ProgressRunnable task);
    public void         taskFailed (ProgressRunnable task, Throwable x);
    public void         taskWasInterrupted (ProgressRunnable task, InterruptedException x);
}
