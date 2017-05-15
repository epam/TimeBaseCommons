package deltix.util.log.gf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import deltix.util.log.gf.impl.GFLoggerFactory;
import deltix.util.log.gf.jul.JULLoggerFactory;

public abstract class LoggerFactory {

    private static final LoggerFactory INSTANCE;

    static {
        String useGFLoggerProperty = System.getProperty(LoggerConstants.USE_GF_LOGGER_PROPERTY_KEY, "true").trim();
        boolean useGFLogger = Boolean.parseBoolean(useGFLoggerProperty);
        INSTANCE = useGFLogger ? new GFLoggerFactory() : new JULLoggerFactory();
    }

    private final Map<String, Logger> loggers = new ConcurrentHashMap<>(512, 0.5f, 1);

    protected LoggerFactory() {
    }

    private Logger getLog(String name) {
        Logger logger = loggers.get(name);

        if (logger == null)
            synchronized (loggers) {
                logger = loggers.get(name);
                if (logger == null) {
                    logger = createLogger(name);
                    loggers.put(name, logger);
                }
            }

        return logger;
    }

    protected abstract Logger createLogger(String name);

    public static Logger getLogger(String name) {
        return INSTANCE.getLog(name);
    }

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

}
