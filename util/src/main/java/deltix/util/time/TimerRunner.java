package deltix.util.time;

import deltix.util.LangUtil;
import deltix.util.lang.Util;

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
            // We catch Throwable to keep existing API behavior for possible onError() overrides.
            try {
                onError(e);
            } catch (Throwable ex) {
                Util.handleException(ex);
                LangUtil.propagateError(ex);
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
        LangUtil.propagateError(e);
    }

    /**
     *  Override this method to perform timer task, instead of overriding run ().
     */
    protected abstract void runInternal () throws Exception;
}
