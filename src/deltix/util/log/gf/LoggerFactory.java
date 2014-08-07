package deltix.util.log.gf;

import java.util.HashMap;
import java.util.Map;

public abstract class LoggerFactory {

    private static LoggerFactory instance;
    private static volatile boolean configured;

    private final Map<String, Logger> loggers = new HashMap<>();

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
        if (instance == null && !configured) // do not read every time volatile variable
            throw new IllegalStateException("Logger factory is not configured");

        return instance.getLog(name);
    }

    static void init(LoggerFactory factory) {
        assert factory != null;

        synchronized (LoggerFactory.class) {
            if (configured)
                throw new IllegalStateException("Logger factory is already configured");

            instance = factory;
            configured = true;
        }
    }

}
