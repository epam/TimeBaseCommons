package com.epam.deltix.util.progress;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ExecutionMonitorImpl implements ExecutionMonitor {

    private ArrayList<ExecutionMonitorImpl> children = new ArrayList<ExecutionMonitorImpl>();
    private ExecutionMonitorImpl parent;
    private Throwable error;

    private double progress;
    private long startTime = 0;
    private long endTime = 0;
    private long weight = 1;

    private boolean isAborted = false;

    private CountDownLatch counter;

    public ExecutionMonitorImpl() {
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

    public synchronized long getEndTime() {
        return endTime;
    }

    public void start() {
        counter = new CountDownLatch(children.size() + 1);
        startTime = System.currentTimeMillis();
    }

//    public synchronized boolean isAborted() {
//        return isAborted;
//    }

    public synchronized void abort(Throwable error) {
        if (isAborted)
            return;

        isAborted = true;
        endTime = System.currentTimeMillis();

        if (hasParent()) {
            parent.abort(error);
        }
        else {
            this.error = error;
            for (ExecutionMonitorImpl child : children)
                child.abort(error);
        }
    }

    public Throwable getError() {
        return error;
    }

    public synchronized void onComplete(ExecutionMonitorImpl child) {
        counter.countDown();
        if (counter.getCount() == 0)
            endTime = System.currentTimeMillis();
    }

    public synchronized void setComplete() {        
        setProgress(1.0);
        if (hasParent())
            parent.onComplete(this);
        else
            onComplete(null);
    }

    public boolean await() {
         try {
            while (true) {
                if (counter.await(1000, TimeUnit.MILLISECONDS))
                    return true;
                if (getStatus() == ExecutionStatus.Aborted)
                    return false;
            }

        } catch (InterruptedException e) {
            return false;
        }
    }

    public boolean await(long timeout) {
        try {
            long time = 0;
            while (time <= timeout) {
                time += 1000;
                if (counter.await(Math.min(1000, timeout - time), TimeUnit.MILLISECONDS))
                    return true;
                if (getStatus() == ExecutionStatus.Aborted)
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

    public synchronized void addMonitor(ExecutionMonitorImpl monitor) {
        children.add(monitor);
        monitor.parent = this;
    }

    private synchronized void updateProgress()
    {
        if (startTime == 0) // not started 
            return;

        double minProgress = -1;

        if (hasChildren())
        {
            progress = 0;
            double totalWeight = 0;

            for (ExecutionMonitorImpl child : children)
            {
                if (minProgress == -1 || child.progress < minProgress)
                    minProgress = child.progress;
                progress += (child.progress * child.weight);
                totalWeight += child.weight;
            }

            progress = (progress / totalWeight);
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

    public synchronized ExecutionStatus getStatus() {
        if (isAborted)
            return error == null ? ExecutionStatus.Aborted : ExecutionStatus.Failed;
        if (counter != null && counter.getCount() > 0)
            return ExecutionStatus.Running;
        if (counter != null && counter.getCount() == 0)
            return ExecutionStatus.Completed;

        return ExecutionStatus.None;
    }
}
