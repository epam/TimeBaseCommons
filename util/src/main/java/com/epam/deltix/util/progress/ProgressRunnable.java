package com.epam.deltix.util.progress;

/**
 *
 */
public interface ProgressRunnable {
    public void     run (MsgProgressIndicator progress)
        throws InterruptedException;        
}
