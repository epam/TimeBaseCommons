package deltix.util.concurrent;

import deltix.util.collections.*;
import deltix.util.lang.Util;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *  Similar to standard Java executors, but does not allocate memory on task
 *  reschedule.
 */
public class QuickExecutor {
    public static abstract class QuickTask extends QuickList.Entry <QuickTask> {
        protected final QuickExecutor       executor;
        boolean                             isScheduled;

        protected QuickTask (QuickExecutor executor) {
            this.executor = executor;
        }

        /**
         *  This method must stop immediately on interrupt and throw
         *  InterruptedException, to cooperate with shutdown.
         */
        public abstract void    run () throws InterruptedException;

        public final void       cancel () {
            executor.cancel (this);
        }
    }

    private class Worker extends Thread {
        Worker (int idx) {
            super (QuickExecutor.this + " Worker #" + idx);
        }

        @Override
        public void             run () {
            executorLoop ();
        }
    }

    private final QuickList <QuickTask>     tasks = new QuickList <QuickTask> ();
    private final String                    name;
    private final ArrayList <Worker>        workers = new ArrayList <Worker> ();

    public QuickExecutor (String name, int numWorkers) {
        this.name = name;

        for (int ii = 0; ii < numWorkers; ii++) {
            Worker  w = new Worker (ii);
            w.start ();
            workers.add (w);
        }
    }

    @Override
    public String           toString () {
        return ("QuickExecutor \"" + name + "\"");
    }

    public void             submit (QuickTask task) {
        assert task.executor == this :
            task + " is being submitted to the wrong executor";

        synchronized (tasks) {
            if (!task.isScheduled) {
                task.isScheduled = true;
                tasks.linkLast (task);
                tasks.notify ();
            }
        }
    }

    public void             cancel (QuickTask task) {
        assert task.executor == this :
            task + " is being cancelled with the wrong executor";

        synchronized (tasks) {
            if (task.isScheduled) {
                task.isScheduled = false;
                task.unlink ();
            }
        }
    }

    public void             shutdown (boolean waitForCompleteShutdown) {
        synchronized (workers) {
            for (Worker w : workers)
                w.interrupt ();

            if (waitForCompleteShutdown) {
                for (Worker w : workers) {
                    try {
                        w.join ();
                    } catch (InterruptedException x) {
                        Util.LOGGER.log (Level.WARNING, "While shutting down " + this, x);
                    }
                }
            }
        }
    }

    private void            executorLoop () {
        try {
            for (;;) {
                QuickTask   task;

                synchronized (tasks) {
                    while (tasks.isEmpty ())
                        tasks.wait ();

                    task = tasks.getFirst ();
                    task.unlink ();
                    task.isScheduled = false;
                }

                try {
                    task.run ();
                } catch (Error x) {
                    Util.LOGGER.log (Level.SEVERE, task + " failed", x);
                } catch (RuntimeException x) {
                    Util.LOGGER.log (Level.SEVERE, task + " failed", x);
                }
            }
        } catch (InterruptedException x) {
            //
        }
    }
}
