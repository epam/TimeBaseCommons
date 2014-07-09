package deltix.util.concurrent;

import deltix.util.collections.QuickList;
import deltix.util.lang.ExceptionHandler;
import deltix.util.lang.Util;
import deltix.util.time.TimeKeeper;

import java.util.ArrayDeque;
import java.util.logging.Level;

/**
 *  Executes Runnables while maintaining a pre-set level of CPU usage.
 */
public class ThrottlingExecutor extends Thread {

    public enum TaskState {
        IDLE,

        /**
         *  Running
         */
        RUNNING,

        /**
         *  Scheduled while running; will be re-run when finished.
         */
        REARMED
    }

    public static abstract class Task extends QuickList.Entry {

        // guarded by this
        private TaskState state = TaskState.IDLE;

        public abstract boolean     run ()
                throws InterruptedException;

        public synchronized void        submit (ThrottlingExecutor exe) {
            if (state == TaskState.IDLE) {
                state = TaskState.RUNNING;
                exe.addTask(this);
            } else if (state == TaskState.RUNNING) {
                state = TaskState.REARMED;
            }
        }

        /*
            Returns true if task is finished
         */
        synchronized boolean   complete (ThrottlingExecutor exe, boolean arm) {
            if (state == TaskState.REARMED) {
                state = TaskState.RUNNING;  // go again
                exe.addTask(this);
                return false;
            } else if (arm) {
                state = TaskState.RUNNING; // arm task
                exe.addTask(this);
                return false;
            } else {
                state = TaskState.IDLE;
                return true;
            }
        }
    }

    public static final long                MEASURABLE_INTERVAL = 20;

    private final ArrayDeque<Task> queue = new ArrayDeque<>();
    private volatile double                 k;
    private volatile long                   maxSleepInterval = Long.MAX_VALUE;
    private ExceptionHandler                handler = null;

    public ThrottlingExecutor (
            String                              name,
            double                              usageRate
    )
    {
        super (name);
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

    protected void                      addTask (Task task) {
        synchronized (queue) {
            queue.addLast(task);
            queue.notify();
        }
    }

    public long                         getMaxSleepInterval () {
        return maxSleepInterval;
    }

    public void                         setMaxSleepInterval (long maxSleepInterval) {
        this.maxSleepInterval = maxSleepInterval;
    }

    private Task                        poll() throws InterruptedException {

        synchronized (queue) {
            while (queue.isEmpty())
                queue.wait();

            return queue.poll();
        }
    }

    void                                removeTask(Task task) {
        synchronized (queue) {
            queue.remove(task);
            queue.notify();
        }
    }

    private long                        performMeasurableWork ()
            throws InterruptedException
    {
        long            t0 = TimeKeeper.currentTime;
        long            limit = t0 + MEASURABLE_INTERVAL;
        long            t1;

        for (;;) {

            Task            next = poll();

            try {
                boolean arm = next.run();
                next.complete(this, arm);

            } catch (UncheckedInterruptedException x) {
                throw x;
            } catch (Throwable x) {
                if (handler == null)
                    Util.LOGGER.log (Level.SEVERE, "Exception in " + next, x);
                else
                    handler.handle (x);
            }

            t1 = TimeKeeper.currentTime;

            if (t1 >= limit)
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

                if (isInterrupted())
                    break;
            }
        } catch (InterruptedException | UncheckedInterruptedException x) {
            Util.LOGGER.fine (this + " was interrupted.");
        }

        synchronized (queue) {
            queue.clear();
            queue.notify();
        }

        Util.LOGGER.fine (this + " is terminating.");
    }
}
