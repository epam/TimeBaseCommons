package deltix.util.concurrent;

import deltix.util.lang.ExceptionHandler;
import deltix.util.lang.Util;
import deltix.util.time.TimeKeeper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;

/**
 *  Executes Runnables while maintaining a pre-set level of CPU usage.
 */
public class ThrottlingExecutor extends Thread {
    public interface Task {
        public boolean              run ()
            throws InterruptedException;
    }

    public static final long                MEASURABLE_INTERVAL = 20;
    
    private final BlockingQueue <Task>      queue;
    private volatile double                 k;
    private volatile long                   maxSleepInterval = Long.MAX_VALUE;
    private ExceptionHandler                handler = null;

    public ThrottlingExecutor (
        String                              name,
        double                              usageRate
    )
    {
        this (name, new LinkedBlockingDeque <Task> (), usageRate);
    }

    public ThrottlingExecutor (
        String                              name,
        BlockingQueue <Task>                queue,
        double                              usageRate
    )
    {
        super (name);
        
        this.queue = queue;
        setUsageRate (usageRate);
    }

    public void                         setUsageRate (double usageRate) {
        if (usageRate <= 0.0001 || usageRate > 1)
            throw new IllegalArgumentException ("usageRate out of range: " + usageRate);

        this.k = 1 / usageRate - 1;
    }

    public ExceptionHandler             getExceptionHandler () {
        return handler;
    }

    public void                         setExceptionHandler (ExceptionHandler hanlder) {
        this.handler = hanlder;
    }

    public BlockingQueue <Task>         getQueue () {
        return queue;
    }

    public void                         addTask (Task task) {
        if (!queue.offer (task))
            throw new RuntimeException ("offer (" + task + ") returned false");
    }

    public boolean                      removeTask (Task task) {
        return (queue.remove (task));
    }

    public long                         getMaxSleepInterval () {
        return maxSleepInterval;
    }

    public void                         setMaxSleepInterval (long maxSleepInterval) {
        this.maxSleepInterval = maxSleepInterval;
    }

    private long                        performMeasurableWork ()
        throws InterruptedException
    {
        Task            task = queue.take ();

        long            t0 = TimeKeeper.currentTime;
        long            limit = t0 + MEASURABLE_INTERVAL;
        long            t1;

        for (;;) {
            try {
                boolean     requeue = task.run ();

                if (requeue)
                    queue.offer (task);
            } catch (UncheckedInterruptedException x) {
                throw x;
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

                if (duration != 0) {
                    long        s = (long) (duration * k);

                    if (s > maxSleepInterval)
                        s = maxSleepInterval;
/*
                    System.out.printf ("%tT.%<tL: worked for %d; will sleep for %d; qsize: %d\n",
                        System.currentTimeMillis (), duration, s, queue.size ()
                    );
 */
                    Thread.sleep (s);
                }
            }
        } catch (InterruptedException x) {
            Util.LOGGER.fine (this + " was interrupted.");
        } catch (UncheckedInterruptedException x) {
            Util.LOGGER.fine (this + " was interrupted.");
        }

        Util.LOGGER.fine (this + " is terminating.");
    }
}
