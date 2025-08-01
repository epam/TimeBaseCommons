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
package com.epam.deltix.qsrv.hf.spi.conn;

import com.epam.deltix.util.time.GlobalTimer;
import com.epam.deltix.util.time.TimerRunner;
import net.jcip.annotations.GuardedBy;

import java.util.Objects;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Helps implement the {@link deltix.qsrv.hf.spi.conn.Disconnectable} interface, including reconnect
 *  capability.
 */
public class ReconnectableImpl extends DisconnectableEventHandler {
//    protected static final Log DEFAULT_LOG = LogFactory.getLog(ReconnectableImpl.class);
    protected static final Logger DEFAULT_LOG = Logger.getLogger(ReconnectableImpl.class.getName());

    public interface Reconnector {
        /**
         *  Try and reconnect. If successful, this method must call
         *  {@link ReconnectableImpl#connected} on <code>helper</code>. After that, the return
         *  value is irrelevant. If unsucessful, this method can either throw
         *  an exception, or return <code>true</code> to reschedule the reconnect,
         *  or, in rare instances, return <code>false</code> to give up.
         *
         * @return  Whether reconnection should be rescheduled.
         * @throws java.lang.Exception
         *          If thrown, reconnect will be rescheduled. Therefore, this
         *          method can freely throw exceptions due to reconnect failure.
         *
         */
        public boolean      tryReconnect (
            int                 numAttempts,
            long                timeSinceDisconnected,
            ReconnectableImpl helper
        )
            throws Exception;
    }

    public interface ReconnectIntervalAdjuster {
        public long         nextInterval (
            int                 numAttempts,
            long                timeSinceDisconnected,
            long                lastInterval
        );
    }

    public static class LinearIntervalAdjuster implements ReconnectIntervalAdjuster {
        private final long      increment;
        private final long      limit;

        public LinearIntervalAdjuster (long increment, long limit) {
            this.increment = increment;
            this.limit = limit;
        }

        public long         nextInterval (
            int                 numAttempts,
            long                timeSinceDisconnected,
            long                lastInterval
        )
        {
            long    next = lastInterval + increment;
            return (next > limit ? limit : next);
        }        
    }

    public static class ExpIntervalAdjuster implements ReconnectIntervalAdjuster {
        private final long      limit;
        private final double    factor;

        public ExpIntervalAdjuster (double factor, long limit) {
            if (factor < 1)
                throw new IllegalArgumentException ("factor: " + factor);

            this.limit = limit;
            this.factor = factor;
        }

        public long         nextInterval (
            int                 numAttempts,
            long                timeSinceDisconnected,
            long                lastInterval
        )
        {
            if (lastInterval >= limit)
                return (limit);
            
            long    next = (long) (lastInterval * factor);

            return (next > limit ? limit : next);
        }
    }

    private volatile long                       initialReconnectInterval = 5000;
    private volatile ReconnectIntervalAdjuster  adjuster = null;
    private volatile Logger                     logger = DEFAULT_LOG;
//    private volatile LogLevel                   logLevel = LogLevel.TRACE;
    private volatile Level                      logLevel = Level.FINE;
    private volatile String                     logprefix;


    private final Object lockObject;

    @GuardedBy("lockObject")
    private Reconnector                         reconnector = null;

    private volatile boolean                    isConnected = false;

    @GuardedBy("lockObject")
    private long                                timeDisconnected;

    @GuardedBy("lockObject")
    private int                                 numReconnectAttempts;

    @GuardedBy("lockObject")
    private long                                currentReconnectInterval;

    @GuardedBy("lockObject")
    private TimerTask                           reconnectTask;

    @GuardedBy("lockObject")
    private String                              lastExceptionAsString;

    public ReconnectableImpl() {
        this.logprefix = getDefaultPrefix(getClass());
        this.lockObject = this;
    }

    public ReconnectableImpl(String logprefix) {
        this.logprefix = logprefix;
        this.lockObject = this;
    }

    /**
     * @param lockObject an object to use as lock for synchronization instead of "this"
     */
    public ReconnectableImpl(String logprefix, Object lockObject) {
        this.logprefix = logprefix;
        // Use this as lock object if not provided. That's preserves old behavior.
        this.lockObject = Objects.requireNonNull(lockObject, "lockObject may not be null");
    }

    public ReconnectIntervalAdjuster        getAdjuster () {
        return adjuster;
    }

    public void                             setAdjuster (
        ReconnectIntervalAdjuster               adjuster
    )
    {
        this.adjuster = adjuster;
    }

    public Reconnector                      getReconnector () {
        return reconnector;
    }

    public void                             setReconnector (Reconnector reconnector) {
        this.reconnector = reconnector;
    }

    public long                             getInitialReconnectInterval () {
        return initialReconnectInterval;
    }

    public void                             setInitialReconnectInterval (
        long                                    initialReconnectInterval
    )
    {
        this.initialReconnectInterval = initialReconnectInterval;
    }

    public int getNumReconnectAttempts() {
        synchronized (lockObject) {
            return numReconnectAttempts;
        }
    }

    public Level                            getLogLevel () {
        return logLevel;
    }

    public void                             setLogLevel (Level logLevel) {
        this.logLevel = logLevel;
    }

    public Logger                           getLogger () {
        return logger;
    }

    public void                             setLogger (Logger logger) {
        this.logger = logger;
    }

//    public void                             setLazyLogger(LazyLogger lazyLogger) {
//        this.lazyLogger = lazyLogger;
//    }

    public void                             setLogPrefix(String logprefix) {
        this.logprefix = logprefix;
    }

    public void                             connected () {
        synchronized (lockObject) {
            if (reconnectTask != null)
                reconnectTask.cancel ();

            isConnected = true;
            lastExceptionAsString = null;
        }

        //logger.log (logLevel, "[%s] Connected").with(logprefix);
        log ("[{0}] Connected", logprefix);

        onReconnected();
    }

    public void                             disconnected () {
        synchronized (lockObject) {
            isConnected = false;
            timeDisconnected = System.currentTimeMillis ();
        }

        //logger.log(logLevel, "[%s] Disconnected").with(logprefix);
        log("[{0}] Disconnected", logprefix);

        onDisconnected();
    }

    private void log(String msg, Object ... params) {
        Logger          lg = logger;
        if (lg != null && lg.isLoggable(logLevel))
            lg.log (logLevel, msg, params);
    }

    private void log(String msg, Object param) {
        Logger          lg = logger;

        if (lg != null && lg.isLoggable(logLevel))
            lg.log (logLevel, msg, param);
    }

    private void log(String msg, Throwable x) {
        Logger          lg = logger;

        if (lg != null && lg.isLoggable(logLevel))
            lg.log (logLevel, msg, x);
    }

    @Override
    public boolean isConnected() {
        synchronized (lockObject) {
            return isConnected;
        }
    }

    private void tryReconnect() {
        synchronized (lockObject) {
            reconnectTask = null;

            boolean reschedule = false;

            if (!isConnected && reconnector != null) {
                try {
                    reschedule =
                            reconnector.tryReconnect(
                                    numReconnectAttempts,
                                    System.currentTimeMillis() - timeDisconnected,
                                    this
                            );
                } catch (Throwable x) {
                    String check = x.toString();
                    if (check.equals(lastExceptionAsString)) {
                        //logger.log (logLevel, "[%s] Reconnect failed due to: %s").with(logprefix).with(lastExceptionAsString);
                        log("[{0}] Reconnect failed due to: {1}", logprefix, lastExceptionAsString);
                    } else {
                        //logger.log (logLevel, "[%s] Reconnect failed: %s").with(logprefix).with(x);
                        log("[" + logprefix + "] Reconnect failed", x);
                        lastExceptionAsString = check;
                    }

                    reschedule = true;
                }

                numReconnectAttempts++;
            }

            if (!isConnected && reschedule) {
                ReconnectIntervalAdjuster adj = adjuster;

                if (adj != null)
                    currentReconnectInterval =
                            adj.nextInterval(
                                    numReconnectAttempts,
                                    timeDisconnected,
                                    currentReconnectInterval
                            );

                scheduleTask();
            }
        }
    }

    @GuardedBy("lockObject")
    private void                            scheduleTask () {
        assert Thread.holdsLock(lockObject);

        reconnectTask =
            new TimerRunner() {
                @Override
                public void     runInternal () {
                    try {
                        tryReconnect ();
                    } catch (Throwable x) {
                        //logger.error("[%s] Unexpected: %s").with(logprefix ).with(x);
                        logger.log (Level.SEVERE, "[" + logprefix + "] Unexpected", x);
                    }
                }
            };

        GlobalTimer.INSTANCE.schedule (reconnectTask, currentReconnectInterval);

        //logger.log(logLevel, "[%s] Next reconnect in %s").with(logprefix).with(currentReconnectInterval);
        log("[{0}] Next reconnect in {1}", logprefix, currentReconnectInterval);
    }

    public void scheduleReconnect() {
        synchronized (lockObject) {
            if (reconnector == null)
                throw new IllegalStateException ("[" + logprefix + "] Call setReconnector() first.");

            if (reconnectTask != null)
                reconnectTask.cancel ();

            numReconnectAttempts = 0;
            currentReconnectInterval = initialReconnectInterval;
            scheduleTask ();
        }
    }

    public void cancelReconnect() {
        synchronized (lockObject) {
            if (reconnectTask != null) {
                reconnectTask.cancel();
            }
        }
    }

    private static String getDefaultPrefix(Class<?> current) {
        StackTraceElement[] elems = Thread.currentThread().getStackTrace();
        if (elems.length <= 1)
            return current.getSimpleName();

        String currentClassName = current.getName();
        for (int i = 1; i < elems.length; i++) {
            String className = elems[i].getClassName();
            if (className != null && !currentClassName.equals(className)) {
                int index = className.lastIndexOf('.');
                return index >= 0 ? className.substring(index + 1) : className;
            }
        }
        return current.getSimpleName();
    }
}