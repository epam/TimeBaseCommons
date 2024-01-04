package deltix.util.oauth;

import deltix.util.lang.Disposable;

public interface RefreshTokenScheduler extends Disposable {

    void schedule(long delayMs, Runnable task);

}
