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
    public static final boolean         CRASH_ON_TASK_REENTER = false;
    public static final Logger          LOGGER = Logger.getLogger ("deltix.executor");

    public enum TaskState {
        IDLE,

        /**
         *  In queue
         */
        SCHEDULED,

        /**
         *  Running in a Worker
         */
        RUNNING,

        /**
         *  Scheduled while running; will be re-run when finished.
         */
        REARMED
    }

    public static abstract class QuickTask extends QuickList.Entry <QuickTask> {
        protected final QuickExecutor       executor;
        //
        //  The state is guarded by executor.tasks
        //
        TaskState                           state = TaskState.IDLE;
        Worker                              worker = null;

        protected QuickTask (QuickExecutor executor) {
            if (executor == null)
                throw new IllegalArgumentException ("null executor");
            
            this.executor = executor;
        }

        protected QuickTask () {
            this (getGlobalInstance ());
        }

        /**
         *  This method must stop immediately on interrupt and throw
         *  InterruptedException, to cooperate with shutdown.
         */
        public abstract void    run () throws InterruptedException;

        public final void       unschedule () {
            executor.unschedule (this);
        }

        public final void       kill () {
            executor.kill (this);
        }

        public final void       submit () {
            executor.submit (this);
        }
    }

    private class Worker extends Thread {
        Worker (int idx) {
            super ("Worker #" + idx + " for " + QuickExecutor.this);
        }

        @Override
        public void             run () {
            try {
                QuickTask   task = null;

                for (;;) {
                    if (task == null) {
                        synchronized (tasks) {
                            while (tasks.isEmpty ())
                                tasks.wait ();

                            task = tasks.getFirst ();
                            
                            assert task.state == TaskState.SCHEDULED;

                            task.unlink ();
                            task.state = TaskState.RUNNING;
                            task.worker = this;
                            numAvailableWorkers--;
                        }
                    }

                    try {
                        task.run ();
                    } catch (UncheckedInterruptedException x) {
                        Util.LOGGER.log (Level.INFO, task + " interrupted.", x);
                    } catch (InterruptedException x) {
                        Util.LOGGER.log (Level.INFO, task + " interrupted.", x);
                    } catch (Throwable x) {
                        Util.LOGGER.log (Level.SEVERE, task + " failed", x);
                    } finally {
                        synchronized (tasks) {
                            if (task.state == TaskState.REARMED)
                                task.state = TaskState.RUNNING;  // go again
                            else {
                                numAvailableWorkers++;
                                task.state = TaskState.IDLE;
                                task.worker = null;
                                task = null;    // cause a queue poll
                            }
                        }
                    }
                }
            } catch (InterruptedException x) {
                // Worker shutdown
            }
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
    private boolean                         shutdownInProgress = false;

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
        if (shutdownInProgress)
            throw new IllegalStateException ("Shutdown in progress");

        Worker      w = new Worker (workerId++);
        w.start ();
        workers.add (w);
        numAvailableWorkers++;
        LOGGER.fine ("# Workers: " + workers.size ());
    }

    void                    submit (QuickTask task) {
        assert task.executor == this :
            task + " is being submitted to the wrong executor";

        synchronized (tasks) {
            switch (task.state) {
                case SCHEDULED:
                case REARMED:
                    break;

                case RUNNING:
                    task.state = TaskState.REARMED;
                    break;

                case IDLE:
                    if (numAvailableWorkers < 1)
                        addWorkerInternal ();

                    task.state = TaskState.SCHEDULED;

                    tasks.linkLast (task);
                    tasks.notify ();
                    break;

                default:
                    throw new RuntimeException (task.state.name ());
            }           
        }
    }

    void                    unschedule (QuickTask task) {
        assert task.executor == this :
            task + " is being unscheduled with the wrong executor";

        synchronized (tasks) {
            switch (task.state) {
                case SCHEDULED:
                    task.unlink ();
                    task.state = TaskState.IDLE;
                    break;

                case REARMED:
                    task.state = TaskState.RUNNING;
                    break;
            }
        }
    }

    void                    kill (QuickTask task) {
        assert task.executor == this :
            task + " is being killed with the wrong executor";

        synchronized (tasks) {
            switch (task.state) {
                case SCHEDULED:
                    task.unlink ();
                    task.state = TaskState.IDLE;
                    break;

                case REARMED:
                    task.state = TaskState.RUNNING;
                    // Fall through to RUNNING
                case RUNNING:
                    task.worker.interrupt ();
                    break;
            }
        }
    }

    public void             shutdown (boolean waitForCompleteShutdown) {
        Worker []               workerSnapshot;

        synchronized (tasks) {
            shutdownInProgress = true;

            workerSnapshot = workers.toArray (new Worker [workers.size ()]);
        }
        
        for (Worker w : workerSnapshot)
            w.interrupt ();

        if (waitForCompleteShutdown) {
            for (Worker w : workerSnapshot) {
                try {
                    w.join ();
                } catch (InterruptedException x) {
                    Util.LOGGER.log (Level.WARNING, "While shutting down " + this, x);
                }
            }
        }        
    }   
}
