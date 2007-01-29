package deltix.util.concurrent;

/**
 *  A Runnable that calls notify () from its run () method.
 */
public class NotifyingRunnable implements Runnable {
    /**
     *  Code:
     *<pre>public synchronized void run () {
     *    notify ();
     *}</pre>
     */
    public synchronized void    run () {
        notify ();
    }
}
