package deltix.util.log.gf;

import java.util.HashMap;
import java.util.Map;

import deltix.util.log.gf.impl.GFLoggerFactory;

public abstract class LoggerFactory {

    private static final LoggerFactory INSTANCE = new GFLoggerFactory();

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
        return INSTANCE.getLog(name);
    }
}
