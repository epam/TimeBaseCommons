package com.epam.deltix.util.time;

import com.epam.deltix.qsrv.hf.pub.TimeSource;
import com.epam.deltix.util.annotations.TimestampMs;
import com.epam.deltix.util.annotations.TimestampNs;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Basic time source that executes System.currentTimeMillis() in the calling thread and
 * guarantied to return monotonously non-decreasing values.
 *
 * <p>Nanosecond precision is not provided ({@link #currentTimeNanos()} returns upscaled millisecond values).
 *
 * @author Alexei Osipov
 */
@ThreadSafe
public class BasicTimeSource implements TimeSource {
    private static final long NANOS_IN_MS = 1_000_000;

    private static final AtomicLong lastTime = new AtomicLong(Long.MIN_VALUE);

    public static final BasicTimeSource INSTANCE = new BasicTimeSource();

    private BasicTimeSource() {
    }

    public static BasicTimeSource getInstance() {
        return INSTANCE;
    }

    @Override
    @TimestampMs
    public long currentTimeMillis() {
        long currentTime = System.currentTimeMillis();
        while (true) {
            long prevVal = lastTime.get();
            if (prevVal >= currentTime) {
                // Shared value is already ahead (or same). So we can use it and do not need to update shared value.
                return prevVal;
            }
            // currentTime > prevVal
            if (lastTime.compareAndSet(prevVal, currentTime)) {
                return currentTime;
            }
        }
    }

    @Override
    @TimestampNs
    public long currentTimeNanos() {
        return currentTimeMillis() * NANOS_IN_MS;
    }
}
