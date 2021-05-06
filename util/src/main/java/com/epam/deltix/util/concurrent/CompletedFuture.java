package com.epam.deltix.util.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CompletedFuture<T> implements Future<T> {
    private final T v;
    private final Throwable re;

    public CompletedFuture(T v, Throwable re) {
        this.v = v;
        this.re = re;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return true;
    }

    public T get() throws ExecutionException {
        if(this.re != null) {
            throw new ExecutionException(this.re);
        } else {
            return this.v;
        }
    }

    public T get(long timeout, TimeUnit unit) throws ExecutionException {
        return this.get();
    }
}
