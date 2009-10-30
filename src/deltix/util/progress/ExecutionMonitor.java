package deltix.util.progress;

public interface ExecutionMonitor {

    long getStartTime();
    long getEndTime();

    double getProgress();

    void abort(Throwable error);

    ExecutionStatus getStatus();

    boolean await(long timeout);
    boolean await();
}
