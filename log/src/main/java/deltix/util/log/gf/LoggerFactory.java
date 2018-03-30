package deltix.util.log.gf;

import deltix.util.log.gf.impl.GFLoggerFactory;
import deltix.util.log.gf.jul.JULLoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class LoggerFactory {

    private static final LoggerFactory INSTANCE = createInstance();

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

    private static LoggerFactory createInstance() {
        String loggerFactory = System.getProperty(LoggerConstants.LOGGER_FACTORY_KEY);
        if (loggerFactory != null) {
            try {
                return (LoggerFactory) Class.forName(loggerFactory).newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        String useGFLoggerProperty = System.getProperty(LoggerConstants.USE_GF_LOGGER_PROPERTY_KEY, "true").trim();
        boolean useGFLogger = Boolean.parseBoolean(useGFLoggerProperty);
        return useGFLogger ? new GFLoggerFactory() : new JULLoggerFactory();
    }

}
