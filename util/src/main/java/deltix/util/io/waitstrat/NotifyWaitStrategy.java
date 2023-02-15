package deltix.util.io.waitstrat;

/**
 *
 */
public class NotifyWaitStrategy implements WaitStrategy {
    private boolean wasSignalled = false;

    @Override
    public synchronized void waitSignal() throws InterruptedException {
        while (!wasSignalled){
            wait();
        }
        wasSignalled = false;
    }

    @Override
    public synchronized void signal() {
        wasSignalled = true;
        notify();
    }

    @Override
    public void close() {
        signal();
    }
}
