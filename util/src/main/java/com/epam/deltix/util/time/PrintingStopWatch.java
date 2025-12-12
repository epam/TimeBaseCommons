package com.epam.deltix.util.time;

import java.util.concurrent.TimeUnit;

/**
 * <p>Utility class for simple performance measurements (mainly for debug only).
 * Allows to measure how often specific fragment of code executed and how much time it takes.
 *
 * <p>Measurement results are printed to System.out.
 * <p>Measurement are taken over REPORT_INTERVAL_NANOS. So you usually get one log entry per instance of {@link PrintingStopWatch}
 * per REPORT_INTERVAL_NANOS.
 *
 * <p>Note: you have to set {@code ENABLED} to {@code true} before using this class. And set it to {@code false} when you finished.
 * <p>Normally you should delete all instances of this class from your code when you finished performance tests.
 *
 * @author Alexei Osipov
 */
public final class PrintingStopWatch {
    private static final boolean ENABLED = false; // NOTE: MUST NOT BE ENABLED IN PROD!

    private static final long REPORT_INTERVAL_NANOS = TimeUnit.SECONDS.toNanos(10); // Edit this value if you want other report interval.

    private long minTime = Long.MAX_VALUE; // Nanos
    private long maxTime = 0;  // Nanos

    private long startTime; // Nanos
    private long totalTime = 0; // Nanos
    private long prevTotalTime = 0; // Nanos

    private long lastReport = 0; // Nanos
    private int counter = 0;
    private int prevCounter = 0;

    private final String name;

    /**
     * @param name Name for the StopWatch. Will be included into the printed log.
     */
    public PrintingStopWatch(String name) {
        this.name = name;
    }

    /**
     * Call this method to start measurement.
     */
    public void start() {
        if (!ENABLED) {
            return;
        }
        startTime = System.nanoTime();
    }

    /**
     * Call this method to stop measurement.
     * <p>If previous measurement report
     */
    public void stop() {
        if (!ENABLED) {
            return;
        }

        long stopTime = System.nanoTime();
        long timeForLastInvocation = stopTime - startTime;
        totalTime += timeForLastInvocation;
        counter++;

        if (timeForLastInvocation < minTime) {
            minTime = timeForLastInvocation;
        }
        if (timeForLastInvocation > maxTime) {
            maxTime = timeForLastInvocation;
        }

        if (stopTime - lastReport > REPORT_INTERVAL_NANOS) {
            printReport(stopTime);
        }
    }

    private void printReport(long stopTime) {
        long totalTimeMs = TimeUnit.NANOSECONDS.toMillis(totalTime);
        long timeDelta = totalTime - prevTotalTime;
        long countDelta = counter - prevCounter;
        System.out.println("Watch=" + name + ". TT = " + totalTimeMs + " ms. Avg. time/invocation = " + (timeDelta / countDelta) + " ns. Min = " + minTime + " ns. Max = " + maxTime + " ns. Count = " + countDelta);
        lastReport = stopTime;
        prevTotalTime = totalTime;
        prevCounter = counter;
        minTime = Long.MAX_VALUE;
        maxTime = 0;
    }
}