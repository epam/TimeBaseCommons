package deltix.util.time;

import deltix.clock.Clocks;
import org.HdrHistogram.Histogram;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Attempts to measure time spent on single {@link #nanoTimeMethodToBeTested()} call with semi-realistic
 * background load.
 *
 * <p>To simulate the load runs a fixed number of threads that call {@link #nanoTimeMethodToBeTested()}
 * at fixed rate (approximately).
 *
 * <p>Main thread performs measurements in loop without any delays.
 *
 * <p>Fixed rate is achieved by execution of dummy code that is expected to have stable execution time.
 *
 * <p>What to look at?
 * <ul>
 *     <li>Look at mean and fixed percentile latencies.
 *     <li>Look at "Actual rate" to detect significant deviations from target rate.
 * </ul>
 * <p>What to try?
 * <ul>
 *     <li>Run with different number of background threads (including 0)
 *     <li>Run with different targetRate (from 50k to 1M)
 * </ul>
 */
public class FrequentTimePollingStressTest {
    private static final boolean USE_RAW_CLOCK = Boolean.getBoolean("deltix.util.time.FrequentTimePollingStressTest.useRawClock");

    @SuppressWarnings("unused")
    private static final MonotonicRealTimeSource monotonicTimeSource = MonotonicRealTimeSource.getInstance();

    private static long nanoTimeMethodToBeTested() {
        return USE_RAW_CLOCK ? Clocks.REALTIME.time() : monotonicTimeSource.currentTimeNanos();
    }

    public static void main(String[] args) throws InterruptedException {
        // Settings
        int backgroundThreads = getLongArg(args, 0, 4L).intValue();
        long targetRate = getLongArg(args, 1, 100_000L);
        long mainMeasurementIterations = getLongArg(args, 2, 200_000_000L);
        boolean enableTimePollFromBackgroundThread = getLongArg(args, 3, 1L) != 0;
        Long manualDummyIterationCount = getLongArg(args, 4, null);


        System.out.println("Background threads: " + backgroundThreads);
        System.out.println("Target rate: " + targetRate + " calls/s");
        System.out.println("Main measurement iterations: " + mainMeasurementIterations);
        System.out.println("Enable time poll from background thread: " + enableTimePollFromBackgroundThread);

        float dummyIterationCostNs = measureDummyIterationCost();
        System.out.println("Dummy iteration cost: " + dummyIterationCostNs + " ns");

        float targetCallPeriodNs = 1_000_000_000f / targetRate;
        System.out.println("Target call period: " + targetCallPeriodNs + " ns");

        float approxCallCost1 = measureApproxCallCost();
        System.out.println("Call cost estimate 1: " + approxCallCost1 + " ns");
        float approxCallCost2 = measureApproxCallCost();
        System.out.println("Call cost estimate 2: " + approxCallCost2 + " ns");
        float approxCallCost3 = measureApproxCallCost();
        System.out.println("Call cost estimate 3: " + approxCallCost3 + " ns");

        float extraDelayNeeded = Math.max(0, targetCallPeriodNs - approxCallCost3);
        System.out.println("Extra delay needed: " + extraDelayNeeded + " ns");

        long dummyIterationsPerCallEstimate = (long) (extraDelayNeeded / dummyIterationCostNs);
        System.out.println("Estimated dummy iterations per call: " + dummyIterationsPerCallEstimate);

        long dummyIterationsPerCall = manualDummyIterationCount != null ? manualDummyIterationCount : dummyIterationsPerCallEstimate;
        System.out.println("Dummy iterations per call: " + dummyIterationsPerCall);

        CountDownLatch backgroundWarmedUp = new CountDownLatch(backgroundThreads);
        AtomicBoolean stopBackground = new AtomicBoolean(false);
        for (int i = 0; i < backgroundThreads; i++) {
            new Thread(() -> {
                long t0 = Long.MIN_VALUE;
                long count = 0;
                boolean warmup = true;
                while (!stopBackground.get()) {
                    long value = enableTimePollFromBackgroundThread ? nanoTimeMethodToBeTested() : 1;
                    count++;
                    if (value == Long.MIN_VALUE) {
                        // Should not happen
                        System.out.println("Wrong value");
                    }
                    dummyOps(dummyIterationsPerCall);
                    if (warmup) {
                        if (count >= 100_000) {
                            backgroundWarmedUp.countDown();
                            count = 0;
                            t0 = System.nanoTime();
                            warmup = false;
                        }
                    }
                }
                long t1 = System.nanoTime();
                System.out.println("Actual rate: " + (count * 1_000_000_000d / (t1 - t0)) + " calls/s");
            }).start();
        }
        System.out.println("Warming up background threads...");
        backgroundWarmedUp.await();
        System.out.println("Starting main measurement...");

        runMeasurement(mainMeasurementIterations);

        stopBackground.set(true);
    }

    private static void runMeasurement(long mainMeasurementIterations) {
        int warmupCount = 1_000_000;
        if (mainMeasurementIterations <= warmupCount) {
            throw new IllegalArgumentException("mainMeasurementIterations < warmupCount");
        }
        Histogram seqMsgHistogram = new Histogram(3);
        long startTime = System.currentTimeMillis();
        long prevValue = nanoTimeMethodToBeTested();
        for (long i = 0; i < mainMeasurementIterations; i++) {
            long value = nanoTimeMethodToBeTested();

            if (i == warmupCount) {
                seqMsgHistogram.reset();
                System.out.println("Warmup done. Running measurements...");
                value = nanoTimeMethodToBeTested();
            }
            seqMsgHistogram.recordValue(value - prevValue);
            prevValue = value;
        }
        long endTime = System.currentTimeMillis();
        synchronized (System.out) {
            System.out.println("=========");
            seqMsgHistogram.outputPercentileDistribution(System.out, 1.0);
            System.out.println("=========");
            System.out.println("Mean   :" + seqMsgHistogram.getMean());
            System.out.println("50%    :" + seqMsgHistogram.getValueAtPercentile(50));
            System.out.println("90%    :" + seqMsgHistogram.getValueAtPercentile(90));
            System.out.println("99%    :" + seqMsgHistogram.getValueAtPercentile(99));
            System.out.println("99.9%  :" + seqMsgHistogram.getValueAtPercentile(99.9));
            System.out.println("99.99% :" + seqMsgHistogram.getValueAtPercentile(99.99));
            System.out.println("=========");
            System.out.println("Measurement took " + (endTime - startTime) + " ms");
            System.out.println("=========");
        }
    }

    /**
     * Nanoseconds per dummy iteration.
     */
    private static float measureDummyIterationCost() {
        int measurementCount = 10;
        int dummyIterationsPerMeasurement = 1_000_000_000;

        float[] measurements = new float[measurementCount];
        for (int i = 0; i < measurementCount; i++) {
            long t0 = System.nanoTime();
            dummyOps(dummyIterationsPerMeasurement);
            long t1 = System.nanoTime();
            float nsPerIteration = ((float)(t1 - t0)) / dummyIterationsPerMeasurement;
            measurements[i] = nsPerIteration;
            System.out.println("Dummy iteration cost: " + nsPerIteration + " ns");
        }
        // Avg on last 3 measurements
        return (measurements[measurementCount - 3] + measurements[measurementCount - 2] + measurements[measurementCount - 1]) / 3;
    }

    // Non-precise estimate of tested cost, so we can calculate right delay between calls to get the right rate.
    private static float measureApproxCallCost() {
        long val = 0;
        long count = 1_000_000;
        long t0 = System.nanoTime();
        for (int i = 0; i < count; i++) {
            val = val | nanoTimeMethodToBeTested();
        }
        long t1 = System.nanoTime();
        return ((float)(t1 - t0)) / count;
    }

    private static void dummyOps(long iterations) {
        long val = 0;
        for (long i = 0; i < iterations; i++) {
            val = ((val & ((1L << 60) - 1)) * 31 + i * 29);
        }
        if (val == Long.MAX_VALUE) {
            // Should not happen
            System.out.println("Error in dummy ops");
        }
    }

    private static Long getLongArg(String[] args, int pos, Long defaultValue) {
        if (args.length > pos) {
            return Long.parseLong(args[pos]);
        } else {
            return defaultValue;
        }
    }
}
