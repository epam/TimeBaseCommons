package com.epam.deltix.util.time;

import com.epam.deltix.qsrv.hf.pub.TimeSource;
import com.epam.deltix.util.annotations.TimestampMs;
import com.epam.deltix.util.annotations.TimestampNs;
import net.jcip.annotations.ThreadSafe;

/**
 * {@link TimeSource} implementation that uses {@link TimeKeeper} as time source.
 *
 * <p>Guarantied to return monotonously non-decreasing values.
 *
 * <p>
 *
 * @author Alexei Osipov
 */
@ThreadSafe
public class KeeperTimeSource implements TimeSource {
    public static final KeeperTimeSource INSTANCE = new KeeperTimeSource();

    private KeeperTimeSource() {
    }

    public static KeeperTimeSource getInstance() {
        return INSTANCE;
    }

    @Override
    @TimestampMs
    public long currentTimeMillis() {
        return TimeKeeper.currentTime;
    }

    @Override
    @TimestampNs
    public long currentTimeNanos() {
        return TimeKeeper.currentTimeNanos;
    }
}
