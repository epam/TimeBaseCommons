package deltix.util.concurrent;

import deltix.util.collections.*;
import deltix.util.lang.Util;
import java.util.*;
import java.util.logging.*;

/**
 *  Similar to standard Java executors, but does not allocate memory on task
 *  reschedule.
 */
public class QuickExecutor {
    public static final Logger          LOGGER = Logger.getLogger ("deltix.executor");

    public static abstract class QuickTask extends QuickList.Entry <QuickTask> {
        protected final QuickExecutor       executor;
        boolean                             isScheduled = false;

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

        public final void       submit () {
            executor.submit (this);
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

    private static QuickExecutor            globalInstance = null;

    public static synchronized QuickExecutor getGlobalInstance () {
        if (globalInstance == null)
            globalInstance = new QuickExecutor ("Global Executor");

        return (globalInstance);
    }

    private final String                    name;
    //
    //  The following members are all guarded by "tasks"
    //
    private final QuickList <QuickTask>     tasks = new QuickList <QuickTask> ();
    private final Set <Worker>              workers = new HashSet <Worker> ();
    private int                             workerId = 1;
    private int                             numAvailableWorkers = 0;

    public QuickExecutor (String name) {
        this.name = name;
    }

    public void             start () {
    }

    @Override
    public String           toString () {
        return ("QuickExecutor \"" + name + "\"");
    }

    private void            addWorkerInternal () {
        Worker      w = new Worker (workerId++);
        w.start ();
        workers.add (w);
        numAvailableWorkers++;
        LOGGER.fine ("# Workers: " + workers.size ());
    }

    public void             submit (QuickTask task) {
        assert task.executor == this :
            task + " is being submitted to the wrong executor";

        synchronized (tasks) {
            if (task.isScheduled)
                return;

            if (numAvailableWorkers < 1)
                addWorkerInternal ();

            task.isScheduled = true;
            tasks.linkLast (task);
            tasks.notify ();
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
                    numAvailableWorkers--;
                }

                try {
                    task.run ();
                } catch (Error x) {
                    Util.LOGGER.log (Level.SEVERE, task + " failed", x);
                } catch (RuntimeException x) {
                    Util.LOGGER.log (Level.SEVERE, task + " failed", x);
                } finally {
                    synchronized (tasks) {
                        numAvailableWorkers++;
                    }
                }
            }
        } catch (InterruptedException x) {
            //
        }
    }
}
