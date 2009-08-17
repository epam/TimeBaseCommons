package deltix.util.progress;

/**
 * Created by IntelliJ IDEA.
 * User: KarpovichA
 * Date: Aug 17, 2009
 * Time: 1:20:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IExecutionMonitor {
    
    double getProgress();
    
    long getStartTime();

    boolean isAborted();
    void abort();
    void setComplete();

    void addMonitor(IExecutionMonitor monitor);
}
