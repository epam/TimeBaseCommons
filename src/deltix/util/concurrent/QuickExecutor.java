package deltix.util.concurrent;

import deltix.util.collections.SimpleSet;

import java.util.*;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.*;

import deltix.util.time.GlobalTimer;
import deltix.util.time.TimeKeeper;
import net.jcip.annotations.GuardedBy;

/**
 *  Similar to standard Java executors, but does not allocate memory on task
 *  reschedule.
 */
public class QuickExecutor {
    public static final boolean         DEBUG_TASKS = false;
    public static final Logger          LOGGER = Logger.getLogger ("deltix.executor");

    public static int                   DELAY = 1000 * 60 * 5;

    public enum TaskState {
        IDLE,

        /**
         *  Running in a Worker
         */
        RUNNING,

        /**
         *  Scheduled while running; will be re-run when finished.
         */
        REARMED
    }

    /**
     *  Sweeper task runs every "DELAY" interval and clean-up threads from free pool.
     */
    private class SweeperTask extends TimerTask {

        @Override
        public void run() {

            if (shutdownInProgress)
                return;

            LOGGER.fine ("Running sweeper having idle workers: " + getIdleWorkersSize());

            long time = TimeKeeper.currentTime;
            int length = getIdleWorkersSize();

            for (int i = 0; i < length; i++) {
                Worker w;

                synchronized (freePool) {
                    w = freePool.peekFirst();
                }

                if (w == null)
                    break;

                if (time - w.timestamp > DELAY) {
                    w = pollWorker(false);
                    if (w != null)
                        terminateWorker(w);
                }
            }

        }
    }

    public static abstract class QuickTask {
        protected final QuickExecutor       executor;
        //
        //  The state is guarded by this
        //
        TaskState                           state = TaskState.IDLE;
        Worker                              worker = null;

        protected QuickTask (QuickExecutor executor) {
            if (executor == null)
                throw new IllegalArgumentException ("null executor");
            
            this.executor = executor;
        }

        protected QuickTask () {
            this (getGlobalInstance());
        }

        protected boolean                   killSupported () {
            return (false);
        }

        /**
         *  This method must stop immediately on interrupt and throw
         *  InterruptedException, to cooperate with shutdown.
         */
        public abstract void                run ()
            throws InterruptedException;

        final synchronized boolean          setDone () {
            if (state == TaskState.REARMED) {
                if (DEBUG_TASKS)
                    System.out.println (this + " is re-armed");

                state = TaskState.RUNNING;  // go again
                return (true);
            } else {
                if (DEBUG_TASKS)
                    System.out.println (this + " is finished");

                state = TaskState.IDLE;
                worker = null;
                return (false);
            }
        }

        public final synchronized void      unschedule () {
            if (state == TaskState.REARMED) {
                if (DEBUG_TASKS)
                    System.out.println (this + " is disarmed");

                state = TaskState.RUNNING;
            }
        }

        public final synchronized void      kill () {
            if (!killSupported ())
                throw new UnsupportedOperationException (this + " does not support kill ()");
            
            switch (state) {
                case REARMED:
                    state = TaskState.RUNNING;
                    // Fall through to RUNNING
                case RUNNING:
                    if (DEBUG_TASKS)
                        System.out.println (this + " is being killed");

                    worker.interrupt ();
                    break;
            }
        }

        public final void                   submit () {
            synchronized (this) {
                switch (state) {
                    case REARMED:
                        return;

                    case RUNNING:
                        state = TaskState.REARMED;
                        return;

                    case IDLE:
                        state = TaskState.RUNNING;
                        worker = executor.feedToWorker (this);
                        break;

                    default:
                        throw new RuntimeException (state.name ());
                }
            }
        }

        @Override
        public synchronized String          toString () {
            if (worker == null)
                return super.toString ();
            else
                return (super.toString () + " running in " + worker);
        }
    }

    private class Worker extends Thread {
        volatile QuickTask      task;
        volatile boolean        stop = false;
        volatile long           timestamp = Long.MIN_VALUE; // time of getting into free pool

        Worker (QuickTask task, int idx) {
            super ("Worker #" + idx + " for " + QuickExecutor.this);

            this.task = task;
        }

        void                    terminate () {
            stop = true;
            interrupt ();
        }

        void                    wakeUp (QuickTask task) {
            this.task = task;

            LockSupport.unpark (this);
        }

        @Override
        public void             run () {
            try {
                while (!stop) {
                    if (task == null) {

                        LockSupport.park ();

                        if (interrupted() && stop)
                            break;

                        if (task == null)
                            continue;
                    }

                    try {
                        task.run ();
                    } catch (UncheckedInterruptedException x) {
                        if (!stop)
                            LOGGER.log (Level.FINE, task + " interrupted.", x);
                    } catch (InterruptedException x) {
                        if (!stop)
                            LOGGER.log (Level.FINE, task + " interrupted.", x);
                    } catch (Throwable x) {
                        LOGGER.log (Level.SEVERE, task + " failed", x);
                    } finally {
                        if (!task.setDone ()) {
                            task = null;
                            freeWorker(this);
                        }
                    }
                }
            } finally {
                synchronized (freePool) {
                    freePool.remove (this);
                }

                synchronized (workers) {
                    workers.remove (this);
                }

                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine (this + " is terminating.");
            }
        }
    }

    private static QuickExecutor            globalInstance = null;
    private static int                      usages = 0;

    public static synchronized QuickExecutor getGlobalInstance () {
        if (globalInstance == null)
            globalInstance = new QuickExecutor ("Global Executor");

        return (globalInstance);
    }
    
    private final String                    name;

    @GuardedBy ("freePool")
    private final LinkedList <Worker>       freePool = new LinkedList<Worker>();
    
    @GuardedBy ("workers")
    private final SimpleSet <Worker>        workers = new SimpleSet<Worker>();
    
    @GuardedBy ("workers")
    private int                             workerId = 1;

    private volatile boolean                shutdownInProgress = false;

    //private int                             depth; // free pool depth

    private QuickExecutor (String name) {
        this.name = name;
        GlobalTimer.INSTANCE.schedule(new SweeperTask(), DELAY, DELAY);
    }

    @Override
    public String           toString () {
        return ("QuickExecutor \"" + name + "\"");
    }

    Worker                  feedToWorker (QuickTask task) {
        assert task.executor == this :
            task + " is being submitted to the wrong executor";

        if (shutdownInProgress)
            throw new IllegalStateException ("Shutdown in progress");

        Worker      w = pollWorker(true);

        if (w != null) {
            assert w.task == null;
            w.wakeUp (task);
        } else {
            synchronized (workers) {
                w = new Worker (task, workerId++);
                workers.add (w);
            }

            w.start();

            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine ("# Workers: " + getWorkersSize());
        }

        return (w);
    }

    private void                                freeWorker(Worker w) {
        synchronized (freePool) {
            w.timestamp = TimeKeeper.currentTime;
            freePool.add(w);
        }
    }

    private void                                terminateWorker(Worker w) {
        w.terminate();
        synchronized (workers) {
            workers.remove(w);
        }
    }

    private Worker                              pollWorker(boolean last) {
        synchronized (freePool) {
            Worker w = last ? freePool.pollLast() : freePool.pollFirst();
            if (w != null)
                w.timestamp = Long.MIN_VALUE;
            return w;
        }
    }
    
    public int                                  getWorkersSize() {
        synchronized (workers) {
            return workers.size();
        }
    }

    public int                                  getIdleWorkersSize() {
        synchronized (freePool) {
            return freePool.size();
        }
    }

    public synchronized static QuickExecutor    reuse() {
        usages++;
        return getGlobalInstance();
    }

    public synchronized static void             shutdown() {
        usages--;
        if (usages <= 0)
            globalInstance.shutdown(true);
    }

    private void             shutdown(boolean waitForCompleteShutdown) {
        Worker []               workerSnapshot;
        
        shutdownInProgress = true;

        synchronized (workers) {
            workerSnapshot = workers.toArray (new Worker [workers.size ()]);
        }
               
        for (Worker w : workerSnapshot)
            w.terminate ();

        if (waitForCompleteShutdown) {
            for (Worker w : workerSnapshot) {
                try {
                    //
                    //  Keep interrupting until it's dead.
                    //
                    //  This works around ignored interrupts in
                    //      misbehaving tasks.
                    for (;;) {
                        w.join (1000);

                        if (!w.isAlive ())
                            break;

                        LOGGER.warning (w + " failed to terminate in 1s, interrupting again ...");
                        w.terminate ();
                    }
                } catch (InterruptedException x) {
                    LOGGER.log (Level.WARNING, "While shutting down " + this, x);
                }
            }
        }

        shutdownInProgress = false;
    }
}
