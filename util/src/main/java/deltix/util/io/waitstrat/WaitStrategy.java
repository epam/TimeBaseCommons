package deltix.util.io.waitstrat;

import deltix.util.lang.Disposable;

/**
 *
 */
public interface WaitStrategy extends Disposable {
    public void                 waitSignal() throws InterruptedException;
    public void                 signal();
}
