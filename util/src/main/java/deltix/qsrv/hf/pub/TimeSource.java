package deltix.qsrv.hf.pub;


import deltix.util.annotations.TimestampMs;
import deltix.util.annotations.TimestampNs;

/**
 * Service that returns current time (real or simulated).
 *
 * Possible extensions: ApproximateTimeService, MarketTimeService
 */
public interface TimeSource {
    /** @return the difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC. */
    @TimestampMs
    long currentTimeMillis();

    /**
     * @return current time in nanoseconds
     */
    @TimestampNs
    default long currentTimeNanos() {
        return currentTimeMillis() * 1_000_000L;
    }
}
