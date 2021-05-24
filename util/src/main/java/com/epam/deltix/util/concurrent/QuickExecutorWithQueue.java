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
import com.epam.deltix.util.collections.QuickList;
import com.epam.deltix.util.lang.Util;
import java.util.*;
import java.util.concurrent.locks.LockSupport;
import net.jcip.annotations.GuardedBy;

/**
 *  Similar to standard Java executors, but does not allocate memory on task
 *  reschedule.
 */
public class QuickExecutorWithQueue {
    public static final boolean         DEBUG_TASKS = false;
    public static final Log LOGGER = LogFactory.getLog("deltix.executor");
    private static final int            JOIN_TIMEOUT_MS = 1000;

    private enum TaskState {
        IDLE,

        /**
         *  Not enough workers, sitting in a queue.
         */
        QUEUED,

        /**
         *  Running in a Worker
         */
        RUNNING,

        /**
         *  Scheduled while running; will be re-run when finished.
         */
        REARMED
    }

    //
    // Strict lock ordering policy: QuickTask THEN QuickExecutorWithQueue instance.
    //
    public static abstract class QuickTask extends QuickList.Entry <QuickTask> {
        protected final QuickExecutorWithQueue       executor;
        //
        //  The state is guarded by this
        //
        TaskState                           state = TaskState.IDLE;
        Worker                              worker = null;

        protected QuickTask (QuickExecutorWithQueue executor) {
            if (executor == null)
                throw new IllegalArgumentException ("null executor");
            
            this.executor = executor;
        }

        protected QuickTask () {
            this (getGlobalInstance ());
        }

        protected boolean                   killSupported () {
            return (false);
        }

        protected boolean                   queueSupported () {
            return (false);
        }

        /**
         *  This method must stop immediately on interrupt and throw
         *  InterruptedException, to cooperate with shutdown.
         */
        public abstract void                run ()
            throws InterruptedException;

        private synchronized boolean        finished () {
            if (state == TaskState.REARMED) {
                if (DEBUG_TASKS)
                    System.out.println (this + " is re-armed");

                state = TaskState.RUNNING;  // go again
                return (false);
            }
            else {
                if (DEBUG_TASKS)
                    System.out.println (this + " is finished");

                state = TaskState.IDLE;
                worker = null;
                return (true);
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
            if (DEBUG_TASKS)
                System.out.println (this + " is being submitted");

            synchronized (this) {
                switch (state) {
                    case REARMED:
                        return;

                    case RUNNING:
                        state = TaskState.REARMED;
                        return;

                    case IDLE:
                        executor.submit (this);
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

    private enum WorkerState {
        RUNNING_TASK,

        FREE,

        TERMINATED
    }

    private class Worker extends Thread {
        QuickTask               task;
        volatile WorkerState    state;

        Worker (int idx) {
            super ("Worker #" + idx + " for " + QuickExecutorWithQueue.this);
        }

        @Override
        public void             run () {
            try {
                while (state != WorkerState.TERMINATED) {
                    if (task == null && !getTask (this))
                        continue;

                    try {
                        task.run ();
                    } catch (UncheckedInterruptedException | InterruptedException x) {
                        if (state != WorkerState.TERMINATED)
                            LOGGER.log(LogLevel.DEBUG).append(task).append(" interrupted.").append(x).commit();
                    } catch (Throwable x) {
                        LOGGER.log(LogLevel.ERROR).append(task).append(" failed.").append(x).commit();
                    } finally {
                        if (task.finished ())
                            task = null;
                    }
                }
            } catch (Throwable x) {
                x.printStackTrace ();
            }
        }
    }

    private static QuickExecutorWithQueue            globalInstance = null;
    private static int                      usages = 0;

    public static synchronized QuickExecutorWithQueue getGlobalInstance () {
        if (globalInstance == null)
            globalInstance = new QuickExecutorWithQueue ("Global Executor");

        return (globalInstance);
    }
    
    private final String                    name;

    private volatile int                    maxNumWorkers = 40; // temporarily so low

    private final QuickList <QuickTask>     queue = new QuickList <QuickTask> ();

    @GuardedBy ("this")
    private final ArrayList <Worker>        freePool = new ArrayList <Worker> ();
    
    @GuardedBy ("this")
    private final Set <Worker>              workers = new HashSet <Worker> ();
    
    @GuardedBy ("this")
    private int                             workerId = 1;

    private volatile boolean                shutdownInProgress = false;

    private QuickExecutorWithQueue (String name) {
        this.name = name;
    }

    @Override
    public String           toString () {
        return ("QuickExecutorWithQueue \"" + name + "\"");
    }

    private boolean                             getTask (Worker w) {
        synchronized (this) {
            if (workers.size () > maxNumWorkers) {
                if (DEBUG_TASKS)
                    System.out.println (w + " is being disposed (too many running).");

                if (w.state == WorkerState.FREE)
                    freePool.remove (w);

                workers.remove (w);

                w.state = WorkerState.TERMINATED;              
                return (false);
            }

            QuickTask       task = queue.getFirst ();

            if (task != null) {
                assert task.state == TaskState.QUEUED :
                    "Unexpected state " + task.state;

                if (DEBUG_TASKS)
                    System.out.println (task + " is de-queued and dispatched to " + w);

                task.worker = w;
                task.state = TaskState.RUNNING;

                w.task = task;
                return (true);
            }

            //  Park it.
            w.state = WorkerState.FREE;
            freePool.add (w);
        }

        if (DEBUG_TASKS)
            System.out.println (w + " is parking.");

        do {
            LockSupport.park ();                                  
        } while (w.state == WorkerState.FREE);

        return (w.task != null);     // force re-check of all conditions on wakeup.
    }
    
    private synchronized void                   submit (QuickTask task) {
        assert task.executor == this :
            task + " is being submitted to the wrong executor";

        assert task.worker == null;
        assert Thread.holdsLock (task);
        
        if (shutdownInProgress)
            throw new IllegalStateException ("Shutdown in progress");

        Worker      w;
        int         numFreeWorkers = freePool.size ();

        if (numFreeWorkers > 0) {
            w = freePool.remove (numFreeWorkers - 1);

            assert w.state == WorkerState.FREE;

            task.state = TaskState.RUNNING;
            task.worker = w;

            if (DEBUG_TASKS)
                System.out.println (task + " is dispatched to FREE " + w);

            w.task = task;
            w.state = WorkerState.RUNNING_TASK;

            LockSupport.unpark (w);
        } 
        else if (workers.size () < maxNumWorkers || !task.queueSupported ()) {
            w = new Worker (workerId++);

            workers.add (w);

            if (DEBUG_TASKS)
                System.out.println (task + " is dispatched to NEW " + w + " Current # Workers: " + workers.size ());

            task.state = TaskState.RUNNING;
            task.worker = w;

            w.task = task;
            w.state = WorkerState.RUNNING_TASK;
            w.start ();
        }
        else {
            if (DEBUG_TASKS)
                System.out.println (task + " is BEING QUEUED");

            queue.linkLast (task);

            task.state = TaskState.QUEUED;
        }                        
    }

    public synchronized static QuickExecutorWithQueue    reuse () {
        usages++;
        return getGlobalInstance();
    }

    public synchronized static void             shutdown () {
        usages--;

        if (usages <= 0) {
            globalInstance.shutdown (true);
            globalInstance = null;
        }
    }

    private void                                shutdown (
        boolean                                     waitForCompleteShutdown
    )
    {
        if (shutdownInProgress)
            throw new IllegalStateException ("Shutdown in progress");
        
        LOGGER.info (this + " shutting down");
        
        Worker []               workerSnapshot;
        
        shutdownInProgress = true;

        synchronized (this) {
            workerSnapshot = workers.toArray (new Worker [workers.size ()]);
                       
            for (Worker w : workerSnapshot) {
                if (w.state == WorkerState.RUNNING_TASK)
                    w.interrupt ();
                else
                    LockSupport.unpark (w);
                
                w.state = WorkerState.TERMINATED;
            }

            workers.clear ();
            freePool.clear ();

            workerId = 1;
        }

        if (waitForCompleteShutdown) {
            for (Worker w : workerSnapshot) {
                try {
                    //
                    //  Keep interrupting until w is dead.
                    //  This works around ignored interrupts in
                    //      misbehaving tasks.
                    for (;;) {
                        w.join (JOIN_TIMEOUT_MS);

                        if (!w.isAlive ())
                            break;

                        LOGGER.warn("%s failed to terminate in %s ms, interrupting again ...").with(w).with(JOIN_TIMEOUT_MS);

                        w.interrupt ();
                    }
                } catch (InterruptedException x) {
                    LOGGER.warn().append("While shutting down ").append(this).append(x).commit();
                }
            }
        }

        shutdownInProgress = false;

        if (DEBUG_TASKS)
            System.out.println (this + ": shutdown completed.");
    }
}