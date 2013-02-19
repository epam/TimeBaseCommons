package deltix.qsrv.hf.tickdb.comm.client;

/**
 *
 */
public class Signal {
    private boolean             state = false;

    public synchronized void    set() {
        state = true;
        notify();
    }

    public synchronized void    reset() {
        state = false;
    }

    public synchronized boolean await() throws InterruptedException {
        while (!state)
            wait(10000);

        state = false;
        return true;
    }
}
