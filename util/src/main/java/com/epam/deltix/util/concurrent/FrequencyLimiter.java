package com.epam.deltix.util.concurrent;

import com.epam.deltix.util.lang.Util;

import java.util.Timer;
import java.util.TimerTask;

/**
 *  Limits the frequency of an action by delaying it by a specified period.
 */
public abstract class FrequencyLimiter {
    private final Timer                 timer;
    private TimerTask                   task = null;

    public FrequencyLimiter (Timer timer) {
        this.timer = timer;
    }

    /**
     *  Override to define reaction to exceptions thrown from {@link #run}.
     *  Default implementation logs with Severe level.
     *
     *  @param x    The exception thrown.
     */
    protected void                      onError (Throwable x) {
        Util.logException(this + " failed", x);
    }

    /**
     *  Override to define action to perform.
     */
    protected abstract void             run () throws Exception;

    /**
     *  Override to define action delay (in ms). Default implementation returns
     *  1000 (= 1s).
     *
     *  @return  Action delay in milliseconds.
     */
    protected long                      getDelay () {
        return (1000);
    }

    public void                         execute () {
        try {
            run ();
        } catch (Throwable x) {
            onError (x);
        } finally {
            synchronized (this) {
                task = null;
            }
        }
    }

    /**
     *  Arms this action, if it's not yet armed.
     */
    public synchronized void            arm () {
        if (task != null)
            return;

        task =
            new TimerTask () {
                @Override
                public void             run () {
                    execute ();
                }
            };

        timer.schedule (task, getDelay ());
    }

    /**
     *  Disarms this action, if it's armed.
     */
    public synchronized boolean         disarm () {
        if (task == null)
            return (false);

        task.cancel ();
        task = null;
        return (true);
    }   
}
