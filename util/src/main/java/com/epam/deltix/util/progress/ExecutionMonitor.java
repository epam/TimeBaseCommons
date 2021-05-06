package com.epam.deltix.util.progress;

public interface ExecutionMonitor {

    long getStartTime();
    long getEndTime();

    double getProgress();
    Throwable getError();

    void abort(Throwable error);

    ExecutionStatus getStatus();

    boolean await(long timeout);
    boolean await();
}
