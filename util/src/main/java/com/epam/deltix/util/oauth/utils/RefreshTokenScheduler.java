package com.epam.deltix.util.oauth.utils;

import com.epam.deltix.util.lang.Disposable;

public interface RefreshTokenScheduler extends Disposable {

    void schedule(long delayMs, Runnable task);

}
