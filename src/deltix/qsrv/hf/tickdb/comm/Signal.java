package deltix.qsrv.hf.tickdb.comm;

/**
 *
 */
public class Signal {
    private boolean             state = false;
    private Throwable           error;

    public synchronized void    set() {
        state = true;
        error = null;

        notify();
    }

    public synchronized void    set(Throwable e) {
        state = true;
        error = e;

        notify();
    }

    public synchronized void    verify() throws IllegalStateException {
        if (error != null)
            throw new IllegalStateException(error);
    }

    public synchronized void    reset() {
        state = false;
        error = null;
    }

    public synchronized boolean await() throws InterruptedException {
        while (!state)
            wait(5000);

        state = false;
        return true;
    }
}
