/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.util.concurrent;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;
import com.epam.deltix.gflog.api.LogLevel;
import com.epam.deltix.thread.affinity.AffinityConfig;
import com.epam.deltix.thread.affinity.AffinityThreadFactoryBuilder;
import com.epam.deltix.util.collections.QuickList;
import com.epam.deltix.util.collections.generated.ObjectHashSet;

import java.util.*;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

import com.epam.deltix.util.time.GlobalTimer;
import com.epam.deltix.util.time.Interval;
import com.epam.deltix.util.time.TimeKeeper;
import com.epam.deltix.util.time.TimeUnit;
import net.jcip.annotations.GuardedBy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *  Similar to standard Java executors, but does not allocate memory on task
 *  reschedule.
 */
public class QuickExecutor {
    public static final boolean         DEBUG_TASKS = false;
    public static final Log LOGGER = LogFactory.getLog("deltix.executor");

    public static int                   DELAY = 1000 * 60 * 5; // 5 min

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

            if (LOGGER.isDebugEnabled())
                LOGGER.debug().append("Running sweeper having idle workers: ").append(getIdleWorkersSize()).commit();

            long time = TimeKeeper.currentTime;
            int length = getIdleWorkersSize();

            for (int i = 0; i < length; i++) {
                Worker w = null;

                synchronized (freePool) {
                    WorkerEntry first = freePool.getFirst();
                    if (first != null && first.isIdle(time)) {
                        w = first.worker;
                        first.unlink();
                    }
                }

                if (w != null)
                    terminateWorker(w);
                else
                    break;
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

        /**
         * @deprecated use {@link #QuickTask(QuickExecutor)} instead
         */
        @Deprecated
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

                    worker.thread.interrupt ();
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

    private class WorkerEntry extends QuickList.Entry<WorkerEntry> {
        private final Worker worker;

        WorkerEntry(Worker worker) {
            this.worker = worker;
        }

        boolean         isIdle(long time) {
            return time - worker.timestamp > DELAY;
        }
    }

    private class Worker implements Runnable {
        private final Thread    thread;
        volatile QuickTask      task;
        volatile boolean        stop = false;
        volatile long           timestamp = Long.MIN_VALUE; // time of getting into free pool

        final WorkerEntry       entry;

        Worker(QuickTask task, ThreadFactory factory) {
            this.entry = new WorkerEntry(this);
            this.task = task;

            // Please do not copy-paste this code. In general case it's may be wrong.
            this.thread = factory.newThread(this);
        }

        void                    terminate () {
            stop = true;
            thread.interrupt ();
        }

        void                    wakeUp (QuickTask task) {
            this.task = task;

            LockSupport.unpark (thread);
        }

        @Override
        public void             run () {
            assert Thread.currentThread() == this.thread;

            try {
                while (!stop) {
                    if (task == null) {

                        LockSupport.park ();

                        if (Thread.interrupted() && stop)
                            break;

                        if (task == null)
                            continue;
                    }

                    try {
                        task.run ();
                    } catch (UncheckedInterruptedException | InterruptedException x) {
                        if (!stop)
                            LOGGER.log(LogLevel.DEBUG).append(task).append(" interrupted.").append(x).commit();
                    } catch (Throwable x) {
                        LOGGER.log(LogLevel.ERROR).append(task).append(" failed.").append(x).commit();
                    } finally {
                        if (!task.setDone ()) {
                            task = null;
                            freeWorker(this);
                        }
                    }
                }
            } finally {

                synchronized (freePool) {
                    if (task == null)
                        entry.unlink();
                }

                synchronized (workers) {
                    workers.remove (this);
                }

                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("%s is terminating.").with(this);
            }
        }
    }

    private static QuickExecutor            globalInstance = null;
    private final AtomicInteger instanceUsages = new AtomicInteger(0);

    @Deprecated
    public static synchronized QuickExecutor getGlobalInstance() {
        if (globalInstance == null) {
            globalInstance = createNewInstance("Global Executor", null);
        }
        return (globalInstance);
    }

    @GuardedBy ("freePool")
    private final QuickList<WorkerEntry>    freePool = new QuickList<>();
    
    @GuardedBy ("workers")
    private final ObjectHashSet <Worker>        workers = new ObjectHashSet<>();
    
    @GuardedBy ("workers")
    private int                             workerId = 1;

    private volatile boolean                shutdownInProgress = false;

    private final String                    fullName;
    private final String                    name;
    private final ThreadFactory             threadFactory;

    //private int                             depth; // free pool depth

    public static QuickExecutor createNewInstance(@Nonnull String name, @Nullable AffinityConfig affinityConfig) {
        return new QuickExecutor(name, affinityConfig);
    }

    private QuickExecutor(@Nonnull String name, @Nullable AffinityConfig affinityConfig) {
        AffinityThreadFactoryBuilder threadFactoryBuilder = new AffinityThreadFactoryBuilder(affinityConfig);

        this.name = name;
        this.fullName = "QuickExecutor \"" + name + "\"";
        this.threadFactory = threadFactoryBuilder
                .setNameFormat("Worker #%d for " + this.fullName)
                .build();

        long delay = Long.getLong("QuickExecutor.Sweeper.delay", DELAY);

        if (delay != DELAY)
            LOGGER.info("%s: override threads sweeping delay to %s").with(name).with(Interval.create(delay, TimeUnit.MILLISECOND).toHumanString());

        GlobalTimer.INSTANCE.schedule(new SweeperTask(), delay, delay);
    }

    @Override
    public String           toString () {
        return fullName;
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
                w = new Worker (task, threadFactory);
                workers.add (w);
            }

            w.thread.start();

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("# Workers: %s").with(getWorkersSize());
        }

        return (w);
    }

    private void                                freeWorker(Worker w) {
        synchronized (freePool) {
            w.timestamp = TimeKeeper.currentTime;
            freePool.linkLast(w.entry);
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
            WorkerEntry w = last ? freePool.getLast() : freePool.getFirst();

            if (w != null) {
                w.worker.timestamp = Long.MIN_VALUE;
                w.unlink();
                return w.worker;
            }
        }

        return null;
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

    public void                                 reuseInstance() {
        instanceUsages.incrementAndGet();
    }

    public synchronized void                    shutdownInstance() {
        int decrementedValue = instanceUsages.decrementAndGet();
        if (decrementedValue < 0) {
            LOGGER.error().append("QuickExecutor instance usages violated: ").append(decrementedValue).append(new Exception()).commit();
        }

        if (decrementedValue == 0) {
            this.shutdown(true);
        }
    }

    /**
     * Shutdowns global instance of QuickExecutor (if it exists).
     *
     * This method is needed because there still code that explicitly or implicitly uses global QuickExecutor instance.
     * So it have to be explicitly stopped by at least one (main?) thread.
     */
    public static synchronized void             shutdownGlobalInstance() {
        QuickExecutor globalInstance = QuickExecutor.globalInstance;
        if (globalInstance != null) {
            synchronized (globalInstance) {
                int usages = globalInstance.instanceUsages.get();
                if (usages > 0) {
                    LOGGER.warn().append("Global instance in use").append(new Exception()).commit();
                } else if (usages == 0) {
                    globalInstance.shutdown(true);
                } else {
                    LOGGER.error().append("QuickExecutor instance usages violated: ").append(usages).append(new Exception()).commit();
                }
            }
        }
    }

    private void                                shutdown(boolean waitForCompleteShutdown) {
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
                        w.thread.join (1000);

                        if (!w.thread.isAlive ())
                            break;

                        LOGGER.warn().append(w).append(" failed to terminate in 1s, interrupting again ...").commit();
                        w.terminate ();
                    }
                } catch (InterruptedException x) {
                    LOGGER.warn().append("While shutting down ").append(this).append(x).commit();
                }
            }
        }

        shutdownInProgress = false;
    }
}