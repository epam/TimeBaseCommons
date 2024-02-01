package deltix.util.time;

import deltix.clock.Clocks;
import deltix.qsrv.hf.pub.TimeSource;
import deltix.util.annotations.TimestampMs;
import deltix.util.annotations.TimestampNs;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Shared non-decreasing realtime time source with up to nanosecond resolution (if available).
 *
 * <p>Time source that:
 * <ul>
 *     <li>Uses {@link deltix.clock.Clocks#REALTIME} as base time source</li>
 *     <li>Guarantied to return monotonously non-decreasing values</li>
 *     <li>Guaranties consistent time across multiple threads (instance users)</li>
 *     <li>Provided "millis" version is in sync with "nanos" value (rounded down) but is not very efficient</li>
 * </ul>
 *
 * <p>WARNING: This implementation is monotonic in the sense of non-decreasing returned values.
 * However, it is not monotonic in the same sense as Linux CLOCK_MONOTONIC (or {@link Clocks#MONOTONIC})
 * that assumes linear growth with time flow.
 */
@ThreadSafe
public class MonotonicRealTimeSource implements TimeSource {

    // Shared value
    private static final AtomicLong lastTimeNs = new AtomicLong(Long.MIN_VALUE);

    private static final MonotonicRealTimeSource INSTANCE = new MonotonicRealTimeSource();

    private MonotonicRealTimeSource() {
    }

    public static MonotonicRealTimeSource getInstance() {
        return INSTANCE;
    }

    @Override
    @TimestampMs
    public long currentTimeMillis() {
        // TODO: Consider using System.currentTimeMillis() directly Clocks.REALTIME is not available
        //  to avoid extra multiplication and division steps
        return currentTimeNanos() / 1_000_000L;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    @TimestampNs
    public long currentTimeNanos() {
        long currentTimeNanos = Clocks.REALTIME.time();
        while (true) {
            long prevVal = lastTimeNs.get();
            if (prevVal >= currentTimeNanos) {
                // Shared value is already ahead (or same). So we can use it and do not need to update shared value.
                return prevVal;
            }
            // currentTimeNanos > prevVal
            if (lastTimeNs.compareAndSet(prevVal, currentTimeNanos)) {
                return currentTimeNanos;
            }
        }
    }
}
