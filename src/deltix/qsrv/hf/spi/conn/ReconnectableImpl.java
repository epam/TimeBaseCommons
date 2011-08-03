package deltix.qsrv.hf.spi.conn;

import deltix.util.lang.Util;
import deltix.util.time.TimerRunner;
import net.jcip.annotations.GuardedBy;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Helps implement the {@link deltix.qsrv.hf.spi.conn.Disconnectable} interface, including reconnect
 *  capability.
 */
public class ReconnectableImpl extends DisconnectableEventHandler {
    public interface Reconnector {
        /**
         *  Try and reconnect. If successful, this method must call
         *  {@link ReconnectableImpl#connected} on <tt>helper</tt>. After that, the return
         *  value is irrelevant. If unsucessful, this method can either throw
         *  an exception, or return <tt>true</tt> to reschedule the reconnect,
         *  or, in rare instances, return <tt>false</tt> to give up.
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
    private volatile Logger                     logger = Util.LOGGER;
    private volatile Level                      logLevel = Level.FINE;
    private volatile String                     logprefix = getDefaultPrefix(getClass());

    @GuardedBy ("this")
    private Reconnector                         reconnector = null;

    private volatile boolean                    isConnected = false;

    @GuardedBy ("this")
    private long                                timeDisconnected;

    @GuardedBy ("this")
    private int                                 numReconnectAttempts;

    @GuardedBy ("this")
    private long                                currentReconnectInterval;    

    @GuardedBy ("this")
    private TimerTask                           reconnectTask;

    @GuardedBy ("this")
    private String                              lastExceptionAsString;

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

    public void                             setLogPrefix(String logprefix) {
        this.logprefix = logprefix;
    }

    public void                             connected () {
        synchronized (this) {
            if (reconnectTask != null)
                reconnectTask.cancel ();

            isConnected = true;
            lastExceptionAsString = null;
        }

        Logger          lg = logger;

        if (lg != null)
            lg.log (logLevel, "[{0}] Connected", logprefix);

        onReconnected();
    }

    public void                             disconnected () {
        synchronized (this) {
            isConnected = false;
            timeDisconnected = System.currentTimeMillis ();
        }

        Logger          lg = logger;

        if (lg != null)
            lg.log (logLevel, "[{0}] Disconnected", logprefix);

        onDisconnected();
    }

    public boolean             isConnected () {
        return (isConnected);
    }
    
    private synchronized void               tryReconnect () throws Exception {
        reconnectTask = null;
        
        boolean     reschedule = false;
        
        if (!isConnected && reconnector != null) {
            try {
                reschedule = 
                    reconnector.tryReconnect (
                        numReconnectAttempts,
                        System.currentTimeMillis () - timeDisconnected,
                        this
                    );
            } catch (Exception x) {
                String          check = x.toString ();
                Logger          lg = logger;

                if (lg != null) {
                    //  Prevent verbose output
                    if (check.equals (lastExceptionAsString))
                        lg.log (logLevel, "[" + logprefix + "] Reconnect failed due to: " + lastExceptionAsString);
                    else {
                        lg.log (logLevel, "[" + logprefix + "] Reconnect failed", x);
                        lastExceptionAsString = check;
                    }
                }

                reschedule = true;
            }

            numReconnectAttempts++;
        }

        if (!isConnected && reschedule) {
            ReconnectIntervalAdjuster   adj = adjuster;

            if (adj != null)
                currentReconnectInterval =
                    adj.nextInterval (
                        numReconnectAttempts,
                        timeDisconnected,
                        currentReconnectInterval
                    );

            scheduleTask ();
        }
    }

    private void                            scheduleTask () {
        assert Thread.holdsLock (this);

        reconnectTask =
            new TimerRunner() {
                @Override
                public void     runInternal () {
                    try {
                        tryReconnect ();
                    } catch (Throwable x) {
                        Util.LOGGER.log (Level.SEVERE, "[" + logprefix + "] Unexpected", x);
                    }
                }
            };

        Util.GLOBAL_TIMER.schedule (reconnectTask, currentReconnectInterval);

        Logger          lg = logger;

        if (lg != null)
            lg.log (logLevel, "[" + logprefix + "] Next reconnect in " + currentReconnectInterval + " ms");
    }

    public synchronized void                scheduleReconnect () {
        if (reconnector == null)
            throw new IllegalStateException ("[" + logprefix + "] Call setReconnector() first.");

        if (reconnectTask != null)
            reconnectTask.cancel ();
        
        numReconnectAttempts = 0;
        currentReconnectInterval = initialReconnectInterval;
        scheduleTask ();
    }

    public synchronized void                cancelReconnect () {
        if (reconnectTask != null)
            reconnectTask.cancel ();
    }

    private static String getDefaultPrefix(Class<?> current) {
        StackTraceElement[] elems = Thread.currentThread().getStackTrace();
        if (elems == null || elems.length <= 1)
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
