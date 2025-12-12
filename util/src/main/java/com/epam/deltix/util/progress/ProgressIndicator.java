package com.epam.deltix.util.progress;

/**
 *  An abstract entity capable of indicating the progress of a process.
 *  Implementations are required to be thread-safe.
 */
public interface ProgressIndicator {
    public void         setTotalWork (double v);
    
    public void         setWorkDone (double v);
    
    public void         incrementWorkDone (double inc);
}
