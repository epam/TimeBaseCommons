package deltix.qsrv.hf.pub;


/**
 * Service that returns current time (real or simulated).
 *
 * Possible extensions: ApproximateTimeService, MarketTimeService
 */
public interface TimeSource {
    /** @return the difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC. */
    long currentTimeMillis();
}
