package com.epam.deltix.util.time;

import com.epam.deltix.util.lang.Util;

/**
 *  Isolates java.util.Timer from exceptions thrown by TimerTasks.
 */
public abstract class TimerRunner extends java.util.TimerTask {
    /**
     *  Override {@link #runInternal} instead.
     */
    @Override
    public final void      run () {
        try {
            runInternal();
        }
        catch (Throwable e) {
            try {
                onError(e);
            } catch (Throwable ex) {
                Util.handleException(ex);
            }
        }
    }

    /**
     *  Override to handle errors thrown by {@link #runInternal}.
     *
     *  @param e    The exception.
     */
    protected void          onError (Throwable e) {
        Util.handleException (e);
    }

    /**
     *  Override this method to perform timer task, instead of overriding run ().
     */
    protected abstract void runInternal () throws Exception;
}
