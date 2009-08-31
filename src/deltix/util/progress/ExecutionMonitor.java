package deltix.util.progress;

public interface ExecutionMonitor {

    long getStartTime();

    double getProgress();

    void abort();

    ExecutionStatus getStatus();

    boolean await(long timeout);
    boolean await();
}
