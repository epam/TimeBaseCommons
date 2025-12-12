package com.epam.deltix.util.progress;

public interface IExecutionMonitor {

    long getStartTime();

    double getProgress();

    void abort();

    ExecutionStatus getStatus();

    boolean await(long timeout);
    boolean await();
}
