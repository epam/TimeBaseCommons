package deltix.util.time;

import deltix.qsrv.hf.pub.TimeSource;
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
    public long currentTimeMillis() {
        return TimeKeeper.currentTime;
    }

    @Override
    public long currentTimeNanos() {
        return TimeKeeper.currentTimeNanos;
    }
}
