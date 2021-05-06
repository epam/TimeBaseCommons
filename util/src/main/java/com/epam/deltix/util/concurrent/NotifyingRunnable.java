package com.epam.deltix.util.concurrent;

/**
 *  A Runnable that calls notify () from its run () method.
 */
public class NotifyingRunnable implements Runnable {
    private final Object        lock;

    public NotifyingRunnable (Object lock) {
        this.lock = lock;
    }

    public NotifyingRunnable () {
        lock = this;
    }

    /**
     *  Synchronizes on the configured lock, then calls notify ().
     */
    public void    run () {
        synchronized (lock) {
            lock.notify ();
        }
    }
}
