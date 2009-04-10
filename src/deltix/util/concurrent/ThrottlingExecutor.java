package deltix.util.concurrent;

import deltix.util.lang.ExceptionHandler;
import deltix.util.lang.Util;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;

/**
 *  Executes Runnables while maintaining a pre-set level of CPU usage.
 */
public class ThrottlingExecutor extends Thread {
    public static final long                MEASURABLE_INTERVAL = 20;
    
    private final BlockingQueue <Runnable>  queue;
    private final double                    k;
    private ExceptionHandler                handler = null;

    public ThrottlingExecutor (
        String                              name,
        double                              usageRate
    )
    {
        this (name, new LinkedBlockingDeque <Runnable> (), usageRate);
    }

    public ThrottlingExecutor (
        String                              name,
        BlockingQueue <Runnable>            queue,
        double                              usageRate
    )
    {
        super (name);

        if (usageRate <= 0.0001 || usageRate > 1)
            throw new IllegalArgumentException ("usageRate out of range: " + usageRate);

        this.queue = queue;
        this.k = 1 / usageRate - 1;
    }

    public ExceptionHandler             getExceptionHandler () {
        return handler;
    }

    public void                         setExceptionHandler (ExceptionHandler hanlder) {
        this.handler = hanlder;
    }

    public BlockingQueue <Runnable>     getQueue () {
        return queue;
    }

    private long                        performMeasurableWork ()
        throws InterruptedException
    {
        Runnable        task = queue.take ();

        long            t0 = System.currentTimeMillis ();
        long            limit = t0 + MEASURABLE_INTERVAL;
        long            t1;

        for (;;) {
            try {
                task.run ();
            } catch (Throwable x) {
                if (handler == null)
                    Util.LOGGER.log (Level.SEVERE, "Exception in " + task, x);
                else
                    handler.handle (x);
            }

            t1 = System.currentTimeMillis ();

            if (t1 >= limit)
                break;

            task = queue.poll ();

            if (task == null)
                break;
        }

        return (t1 - t0);
    }

    @Override
    public void                         run () {
        Util.LOGGER.fine (this + " is starting.");

        try {
            for (;;) {
                long            duration = performMeasurableWork ();

                if (duration != 0)
                    Thread.sleep ((long) (duration * k));
            }
        } catch (InterruptedException x) {
            Util.LOGGER.fine (this + " was interrupted.");
        }

        Util.LOGGER.fine (this + " is terminating.");
    }
}
