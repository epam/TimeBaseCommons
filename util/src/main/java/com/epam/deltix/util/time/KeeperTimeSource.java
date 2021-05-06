package com.epam.deltix.util.time;

import com.epam.deltix.qsrv.hf.pub.TimeSource;

/**
 * {@link TimeSource} implementation that uses {@link TimeKeeper} as time source.
 *
 * <p>Guarantied to return monotonously non-decreasing values.
 *
 * <p>
 *
 * @author Alexei Osipov
 */
public class KeeperTimeSource implements TimeSource {
    public static final KeeperTimeSource INSTANCE = new KeeperTimeSource();

    private KeeperTimeSource() {
    }

    @Override
    public long currentTimeMillis() {
        return TimeKeeper.currentTime;
    }

    @Override
    public long currentTimeNanos() {
        return TimeKeeper.currentTimeNanos;
    }
}
