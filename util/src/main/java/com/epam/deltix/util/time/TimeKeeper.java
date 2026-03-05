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
package com.epam.deltix.util.time;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;
import com.epam.deltix.util.lang.MathUtil;
import com.epam.deltix.util.lang.Util;

import java.util.EmptyStackException;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.concurrent.locks.LockSupport;

/**
 *
 */
public class TimeKeeper extends Thread {
    private static final Log LOG = LogFactory.getLog(TimeKeeper.class);
    public static void          main (String [] args) throws Exception {
        TimeKeeper.setMode (Mode.HIGH_RESOLUTION_SYNC_BACK);
        
        for (;;) {
            LockSupport.parkNanos (235327881);
            
            long        keepTime = TimeKeeper.currentTime;
            long        sysTime = System.currentTimeMillis ();
            
            System.out.print (GMT.formatDateTimeMillis (sysTime));
            
            if (sysTime == keepTime)
                System.out.println (": IN SYNC");
            else if (sysTime < keepTime)
                System.out.println (": Keeper is ahead by " + (keepTime - sysTime) + "ms");
            else
                System.out.println (": Keeper is behind by " + (sysTime - keepTime) + "ms");                                              
        }
    }
    
    
    private static final long           NOT_SCHEDULED = Long.MAX_VALUE;
    private static final boolean        DEBUG = false;
    private static final double         DISTORTION = 1;
    private static final long           IN_SYNC = -1;
    private static final long           RUNAWAY_THRESHOLD_MS = 1000;
    private static final long           M = 1000000;
    private static final long           PARK_LOW = 500000;
    private static final long           PARK_MEDIUM = 100000;
    private static final long           START_TIME = System.currentTimeMillis ();

    public enum Mode {
        /**
         *  Park for 0.5ms between time maintenance attempts. 
         *  This produces about 1ms accuracy.
         */
        LOW_RESOLUTION,
        
        /**
         *  Park for 20us between time maintenance attempts. 
         *  This produces about good accuracy on Linux but only
         *  about 1ms accuracy on Windows.
         */
        MEDIUM_RESOLUTION,
        
        /**
         *  Consume a CPU core (one per JVM process), but produce very
         *  precise time values.
         */
        HIGH_RESOLUTION_SYNC_BACK
    }
    
    private static abstract class Task implements Runnable, Comparable <Task> {
        private long    scheduledAtNanos = NOT_SCHEDULED;

        public final boolean    isScheduled () {
            return (INSTANCE.isScheduled (this));
        }

        public final int        compareTo (Task o) {
            return (MathUtil.compare (scheduledAtNanos, o.scheduledAtNanos));
        }

        public final boolean    scheduleAt (long ticks) {
            return (INSTANCE.schedule (this, ticks));
        }

        public final boolean    cancel () {
            return (INSTANCE.cancel (this));
        }
    }

    private static final class UnparkTask extends Task {
        private Thread              thread;

        public void             run () {
            LockSupport.unpark (thread);

            thread = null;

            unparkTaskPool.push (this);
        }
    }
        
    public static volatile long                 currentTime = START_TIME;
    public static volatile long                 currentTimeNanos = START_TIME * M;

    private static volatile Mode                mode = Mode.LOW_RESOLUTION;
    
    private long                                lastTimeMillis = START_TIME;
    private boolean                             setBackReported = false;
    private long                                offset;
    private long                                runawayAt = IN_SYNC;
    private static final Stack <UnparkTask>     unparkTaskPool = 
        new Stack <UnparkTask> ();

    private final PriorityQueue <Task> taskQueue = new PriorityQueue <Task> ();

    private boolean                 isScheduled (Task task) {
        synchronized (taskQueue) {
            return (task.scheduledAtNanos != Long.MAX_VALUE);
        }
    }

    private boolean                 schedule (Task task, long ticks) {
        if (ticks == Long.MAX_VALUE)
            throw new IllegalArgumentException ("ticks == NOT_SCHEDULED");

        synchronized (taskQueue) {
            boolean     isNew = task.scheduledAtNanos == NOT_SCHEDULED;

            if (!isNew) {
                boolean     ok = taskQueue.remove (task);

                assert ok;
            }

            task.scheduledAtNanos = ticks;

            taskQueue.offer (task);

            return (isNew);
        }
    }

    private boolean                 cancel (Task task) {
        synchronized (taskQueue) {
            if (task.scheduledAtNanos == NOT_SCHEDULED)
                return (false);

            boolean     ok = taskQueue.remove (task);

            assert ok;

            task.scheduledAtNanos = NOT_SCHEDULED;

            return (true);
        }
    }

    private static final TimeKeeper    INSTANCE = new TimeKeeper ();

    private static volatile long        sleepUntilLateness = 0;
    
    private static long     nanoTime () {
        long                    nanoTime = System.nanoTime ();

        if (!DEBUG)
            return (nanoTime);

        return ((long) (nanoTime * DISTORTION));
    }
    
    private TimeKeeper () {
        super ("TimeKeeper");

        final long              nanoTime = nanoTime ();

        lastTimeMillis = System.currentTimeMillis ();
        offset = lastTimeMillis * M - nanoTime;                
    }

    /*
        Returns true if current time goes forward
     */
    private boolean         getSystemTimeNoRollBack () {
        final long              t = System.currentTimeMillis ();

        if (t == lastTimeMillis)
            return (false);

        if (t < lastTimeMillis) {
            //
            //  System clock was set back (usually by NTP)
            //
            if (!setBackReported) {
                LOG.warn ("System time was adjusted from %s -> %s").with(GMT.formatDateTimeMillis (t)).with(GMT.formatDateTimeMillis (lastTimeMillis));

                setBackReported = true;
            }

            return (false);
        }
        else {
            //
            //  System clock just went forward. We trust it right now
            //  (for about 1 microsecond).
            //
            setBackReported = false;
        }

        lastTimeMillis = t;
        return (true);
    }

    private boolean         isRunawayConfirmed () {
        return (runawayAt != IN_SYNC && lastTimeMillis >= runawayAt);
    }
    
    private void            set (long nanoTime) {
        assert nanoTime >= currentTimeNanos;
        assert !isRunawayConfirmed ();    // Else we should not be increasing.

        currentTimeNanos = nanoTime;
        currentTime = nanoTime / M;
    }

    /**
     *  This method performs time-keeping functions.
     *
     *  timeNanos = nanoTime + offset
     */
    private void            doAccurateTimeMaintenance () {
        long                    sysNanoTime = nanoTime ();
        long                    cpuTimeNanos = sysNanoTime + offset;
        boolean                 sysClockChanged = getSystemTimeNoRollBack ();        

        if (!sysClockChanged && !isRunawayConfirmed ()) {  
            // Keep ticking
            set (cpuTimeNanos);
            return;
        }

        long                    cpuTimeMillis = cpuTimeNanos / M;
        long                    keeperAhead = cpuTimeMillis - lastTimeMillis;

        if (keeperAhead < 0) {
            runawayAt = IN_SYNC;
            //
            //  Assumption: system clock is never early. Therefore,
            //  adjust offset just enough so that model time catches up right away
            //
            cpuTimeNanos = lastTimeMillis * M;

            if (DEBUG) {
                System.out.printf (
                    "TK: + %,d ns\n",
                    cpuTimeNanos - sysNanoTime - offset
                );
            }

            offset = cpuTimeNanos - sysNanoTime;

            set (cpuTimeNanos);
            return;
        }

        if (keeperAhead == 0) {
            runawayAt = IN_SYNC;
            set (cpuTimeNanos);
            return;
        }
        //
        //  keeper is ahead by at least 1ms
        //
        if (runawayAt == IN_SYNC) {
            if (DEBUG) {
                System.out.print (
                    "TK: Ahead, raising runaway suspicion\n"                    
                );
            }
            
            runawayAt = lastTimeMillis + RUNAWAY_THRESHOLD_MS;
        }
        
        if (lastTimeMillis >= runawayAt) {
            //
            //  Keeper has been consistently ahead for RUNAWAY_THRESHOLD_MS.
            //  Keeper time can never go back, however.
            //
            long        maxCompliantTimeNanos = lastTimeMillis * M + (M - 1);
            long        minPossibleOffset = currentTimeNanos - sysNanoTime;
            long        targetOffset = maxCompliantTimeNanos - sysNanoTime;

            if (targetOffset >= minPossibleOffset) {
                if (DEBUG) {
                    System.out.printf (
                        "TK: -%,d (FINAL)\n",
                        offset - targetOffset
                    );
                }
                
                offset = targetOffset;
                runawayAt = IN_SYNC;
                
                set (maxCompliantTimeNanos);
            }
            else {
                if (DEBUG) {
                    System.out.printf (
                        "TK: STAYING PUT; -%,d (STILL RUNAWAY)\n", 
                        offset - minPossibleOffset
                    );
                }
                
                offset = minPossibleOffset;                          
            }
        }
        else
            set (cpuTimeNanos);
    }

    private void            doApproximateTimeMaintenance () {
        if (getSystemTimeNoRollBack ()) {
            currentTime = System.currentTimeMillis ();
            currentTimeNanos = currentTime * M;
        }
    }

    private void            runTasks () {
        for (;;) {
            Task            task;

            synchronized (taskQueue) {
                task = taskQueue.peek ();

                if (task == null)
                    break;

                if (currentTimeNanos < task.scheduledAtNanos)
                    break;

                Task        check = taskQueue.poll ();

                assert check == task;

                task.scheduledAtNanos = NOT_SCHEDULED;
            }

            task.run ();
        }
    }

    @Override
    public void             run () {
        int         exceptionCount = 0;

        for (;;) {
            try {
                switch (mode) {
                    case HIGH_RESOLUTION_SYNC_BACK:
                        doAccurateTimeMaintenance ();
                        break;
                        
                    case LOW_RESOLUTION:
                        doApproximateTimeMaintenance ();
                        LockSupport.parkNanos (PARK_LOW);
                        break;
                        
                    case MEDIUM_RESOLUTION:
                        doAccurateTimeMaintenance ();
                        LockSupport.parkNanos (PARK_MEDIUM);
                        break;
                }

                runTasks ();
            } catch (Exception x) {
                if (exceptionCount++ > 500) {
                    LOG.error("TimeKeeper has logged 500 errors. Shutting down.");
                    System.exit (1);
                }

                LOG.error("Exception in TimeKeeper: %s").with(x);
            }
        }
    }

    static {
        if (Util.IS_WINDOWS_OS) {
            // Force the Windows system clock into high resolution mode.
            //
            // In Windows, the default clock resolution is 15.625ms (1s divided by 64).
            // For many of TimeBase use cases is a way too low resolution.
            // The simplest way to get a higher timer resolution from Java is to have a thread that "sleeps" forever.
            // Under the hood Java will use timeBeginPeriod(1)/timeEndPeriod(1) WinAPI calls to increase timer resolution.
            //
            // Workaround from  https://bugs.openjdk.org/browse/JDK-6435126
            //
            // Related articles:
            // https://randomascii.wordpress.com/2013/07/08/windows-timer-resolution-megawatts-wasted/
            // https://hazelcast.com/blog/locksupport-parknanos-under-the-hood-and-the-curious-case-of-parking-part-ii-windows/
            //
            // See also class TimerFreqTest
            //
            Thread  magic =
                new Thread ("Windows System Clock Speeder-Upper") {
                    @Override
                    @SuppressWarnings ("SleepWhileInLoop")
                    public void run() {
                        for (;;) {
                            try {
                                Thread.sleep (Integer.MAX_VALUE);
                            } catch(InterruptedException ex) {
                            }
                        }
                    }
                };

            magic.setDaemon (true);
            magic.start ();
        }

        INSTANCE.setDaemon (true);
        INSTANCE.start ();
    }
    //
    //  PUBLIC API
    //
    public static void      parkNanos (long nanos) {
        switch (mode) {
            case HIGH_RESOLUTION_SYNC_BACK: 
                parkUntil (currentTimeNanos + nanos);
                break;
                
            default:
                LockSupport.parkNanos (nanos);
                break;
        }                
    }

    public static void      parkUntil (long nanoTime) {
        UnparkTask  u;

        try {
            u = unparkTaskPool.pop ();
        } catch (EmptyStackException x) {
            u = new UnparkTask ();
        }

        u.thread = Thread.currentThread ();

        boolean       ha = false;
        
        switch (mode) {
            case HIGH_RESOLUTION_SYNC_BACK: 
                ha = true;
                break;
        }
        
        long                heuristicDeadline = nanoTime;

        if (ha)
            heuristicDeadline -= (sleepUntilLateness + 30);

        u.scheduleAt (heuristicDeadline);

        LockSupport.park ();

        if (ha) {
            final long        observedLateness = currentTimeNanos - nanoTime;
            //
            //  The following is not synchronized, but should be adequate (and safe).
            //
            final long        v = sleepUntilLateness;
            //
            //  Move by only 1 tick at a time, no matter what magnitude.
            //
            if (observedLateness < v)
                sleepUntilLateness = v - 1;
            else if (observedLateness > sleepUntilLateness)
                sleepUntilLateness = v + 1;
        }
    }

    public static Mode          getMode () {
        return mode;
    }

    public static void          setMode (Mode flag) {
        if (flag == null)
            throw new IllegalArgumentException ("null");
        
        mode = flag;
    }
    
    
}