package com.epam.deltix.util.time;

import com.epam.deltix.qsrv.hf.pub.TimeSource;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Basic time source that executes System.currentTimeMillis() in the calling thread and
 * guarantied to return monotonously non-decreasing values.
 *
 * <p>Nanosecond precision is not provided ({@link #currentTimeNanos()} returns upscaled millisecond values).
 *
 * @author Alexei Osipov
 */
public class BasicTimeSource implements TimeSource {
    private static final long NANOS_IN_MS = 1_000_000;

    private static final AtomicLong lastTime = new AtomicLong(Long.MIN_VALUE);

    public static final BasicTimeSource INSTANCE = new BasicTimeSource();

    private BasicTimeSource() {
    }

    @Override
    public long currentTimeMillis() {
        long currentTime = System.currentTimeMillis();
        while (true) {
            long prevVal = lastTime.get();
            if (prevVal == currentTime) {
                return currentTime;
            }
            if (prevVal > currentTime) {
                return prevVal;
            }
            // currentTime > prevVal
            if (lastTime.compareAndSet(prevVal, currentTime)) {
                return currentTime;
            }
        }
    }

    @Override
    public long currentTimeNanos() {
        return currentTimeMillis() * NANOS_IN_MS;
    }
}
