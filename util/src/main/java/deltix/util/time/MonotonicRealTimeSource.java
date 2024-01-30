package deltix.util.time;

import deltix.clock.Clocks;
import deltix.qsrv.hf.pub.TimeSource;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Shared monotonic realtime time source with up to nanosecond resolution (if available).
 *
 * <p>Time source that:
 * <ul>
 *     <li>Uses {@link deltix.clock.Clocks#REALTIME} as base time source</li>
 *     <li>Guarantied to return monotonously non-decreasing values</li>
 *     <li>Guaranties consistent time across multiple threads (instance users)</li>
 *     <li>Provided "millis" version is in sync with "nanos" value (rounded down) but is not very efficient</li>
 * </ul>
 */
@ThreadSafe
public class MonotonicRealTimeSource implements TimeSource {

    // Shared value
    private static final AtomicLong lastTimeNs = new AtomicLong(Long.MIN_VALUE);

    @Override
    public long currentTimeMillis() {
        // TODO: Consider using System.currentTimeMillis() directly Clocks.REALTIME is not available to avoid extra multiplication and division steps
        return currentTimeNanos() / 1_000_000L;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public long currentTimeNanos() {
        long currentTimeNanos = Clocks.REALTIME.time();
        while (true) {
            long prevVal = lastTimeNs.get();
            if (prevVal == currentTimeNanos) {
                return currentTimeNanos;
            }
            if (prevVal > currentTimeNanos) {
                return prevVal;
            }
            // currentTime > prevVal
            if (lastTimeNs.compareAndSet(prevVal, currentTimeNanos)) {
                return currentTimeNanos;
            }
        }
    }
}
