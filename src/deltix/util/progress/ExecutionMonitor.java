package deltix.util.progress;

import org.apache.commons.lang.mutable.MutableBoolean;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: KarpovichA
 * Date: Aug 17, 2009
 * Time: 12:31:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExecutionMonitor implements IExecutionMonitor {

    private ArrayList<ExecutionMonitor> children = new ArrayList<ExecutionMonitor>();
    private ExecutionMonitor parent;

    private double progress;
    private long startTime = 0;
    private long weight = 1;

    private boolean isAborted = false;

    private CountDownLatch counter;

    public ExecutionMonitor() {
        setStartTime();
    }

    public double getProgress() {
        updateProgress();
        return progress;
    }

    public synchronized void setProgress(double v) {
        progress = v;
    }

    public synchronized long getStartTime() {
        return startTime;
    }

    public synchronized void setStartTime(long time) {
        startTime = time;
    }

    public synchronized void setStartTime() {
        startTime = System.currentTimeMillis();
    }

    public synchronized boolean isAborted() {
        return isAborted;
    }

    public synchronized void abort() {
        isAborted = true;
        
        if (hasChildren()) {
            for (ExecutionMonitor child : children)
                child.abort();
        }

    }

    public synchronized void onComplete(ExecutionMonitor child) {
        counter.countDown();
    }

    public synchronized void setComplete() {
        setProgress(1.0);
        if (hasParent())
            parent.onComplete(this);
        else
            onComplete(null);
    }

    public boolean waitForComplete(long timeout) {
        counter = new CountDownLatch(children.size() + 1);

        try {
            long time = 0;
            while (time <= timeout) {
                time += 1000;
                if (counter.await(Math.min(1000, timeout - time), TimeUnit.MILLISECONDS))
                    return true;
                if (isAborted())
                    return false;
            }
            return false;
            
        } catch (InterruptedException e) {
            return false;
        }
    }

    public synchronized long getWeight() {
        return weight;
    }

    public synchronized void setWeight(long weight) {
        this.weight = weight;
    }

    public synchronized void addMonitor(IExecutionMonitor monitor) {
        // TODO: refactor
        children.add((ExecutionMonitor) monitor);
        ((ExecutionMonitor) monitor).parent = this;
    }

    private synchronized void updateProgress()
    {
        if (startTime == 0)
            setStartTime();

        double minProgress = -1;

        if (hasChildren())
        {
            progress = 0;
            double totalWeight = 0;

            for (ExecutionMonitor child : children)
            {
                if (minProgress == -1 || child.progress < minProgress)
                    minProgress = child.progress;
                progress += (int)(child.progress * child.weight);
                totalWeight += child.weight;
            }

            progress = (int)(progress / totalWeight);
            //if (!CalculateMinChildEstimatedTime)
            minProgress = progress;
        }
        else
            minProgress = progress;

        // Make parent progress to update itself
        if (hasParent())
        {
            parent.updateProgress();
        }
//        else
//        {
//            // only parent sends Progress events
//            if (ProgressEvent != null && prevProgress != progress)
//            {
//                // Calculated Estimated time
//                TimeSpan estimatedTime = CalculateElapsedTime(minProgress);
//                //
//                ProgressEvent(this, new ProgressAgrs(estimatedTime, progress));
//            }
//
//            prevProgress = progress;
//        }
    }

    private boolean hasChildren() {
        return children.size() > 0;
    }

    private boolean hasParent() {
        return parent != null;
    }
}
