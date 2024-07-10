package deltix.util.time;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;
import deltix.qsrv.hf.pub.TimeSource;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

/**
 * Time source provided for applications that do not have own source of clock configuration.
 */
public class DefaultTimeSourceProvider {
    private static final Log LOG = LogFactory.getLog(DefaultTimeSourceProvider.class);

    /**
     * System property that can be used to configure time source. Supported values are defined in {@link #getTimeSourceByName(String)}.
     */
    public static final String TIME_SOURCE_SYS_PROP = "deltix.util.time.DefaultTimeSourceProvider.clock";

    private static volatile TimeSource configuredInstance;

    private DefaultTimeSourceProvider() {
    }

    /**
     * Provides default time source. If time source is not configured yet, then it will be configured during this call using system property.
     *
     * @param appName name of application or use case that will use this time source. Will be logged if triggers configuration.
     */
    public static TimeSource getTimeSourceForApp(String appName) {
        if (configuredInstance == null) {
            synchronized (DefaultTimeSourceProvider.class) {
                if (configuredInstance == null) {
                    // ...
                    String sysProperty = System.getProperty(TIME_SOURCE_SYS_PROP);
                    if (sysProperty == null) {
                        configuredInstance = getDefaultFallback();
                        LOG.info("Selected time source: %s (default, implicitly configured by %s)")
                                .with(configuredInstance.getClass().getSimpleName())
                                .with(appName);
                    } else {
                        configuredInstance = getTimeSourceByNameWithFallback(sysProperty);
                        LOG.info("Selected time source: %s (implicitly configured by %s)")
                                .with(configuredInstance.getClass().getSimpleName())
                                .with(appName);
                    }
                }
            }
        }
        return configuredInstance;
    }

    /**
     * Sets time source to be used by default.
     *
     * <p>Will throw exception if time source is already configured (directly or by call to {@link #getTimeSourceForApp(String)}).
     *
     * @param appName    name of application that configures this time source. Will be logged.
     * @param timeSource time source to use
     */
    public static void configure(String appName, TimeSource timeSource) {
        synchronized (DefaultTimeSourceProvider.class) {
            if (configuredInstance != null) {
                throw new IllegalStateException("Time source is already configured");
            }
            configuredInstance = timeSource;
            LOG.info("Selected time source: %s (explicitly configured by %s)")
                    .with(configuredInstance.getClass().getSimpleName())
                    .with(appName);
        }
    }

    private static TimeSource getDefaultFallback() {
        return KeeperTimeSource.getInstance();
    }

    /**
     * Converts time source name to time source instance.
     *
     * @return time source instance or null if name is unknown
     */
    @Nullable
    public static TimeSource getTimeSourceByName(String sourceName) {
        switch (sourceName) {
            case "MonotonicRealTimeSource":
                return MonotonicRealTimeSource.getInstance();
            case "KeeperTimeSource":
                return KeeperTimeSource.getInstance();
            default:
                return null;
        }
    }

    private static TimeSource getTimeSourceByNameWithFallback(String sysProperty) {
        TimeSource timeSourceByName = getTimeSourceByName(sysProperty);
        if (timeSourceByName == null) {
            LOG.warn().append("Unknown time source name: ").append(sysProperty).append(" (will use default)").commit();
            timeSourceByName = getDefaultFallback();
        }
        return timeSourceByName;
    }

    /**
     * ONLY FOR TESTING!
     */
    @VisibleForTesting
    static void unconfigure() {
        synchronized (DefaultTimeSourceProvider.class) {
            configuredInstance = null;
        }
    }
}
