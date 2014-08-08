package deltix.util.log.gf;

import java.util.HashMap;
import java.util.Map;

import deltix.util.lang.StringUtils;
import deltix.util.lang.Util;
import deltix.util.log.gf.impl.GFLoggerFactory;
import deltix.util.log.gf.jul.JULLoggerFactory;

public abstract class LoggerFactory {

    private static final String USE_JUL_PROPERTY_KEY = "QuantServer.logging.gflog.useJUL";
    private static final LoggerFactory INSTANCE;

    static { // TODO: make configurable
        String useJULProperty = StringUtils.trim(Util.getSysProp(USE_JUL_PROPERTY_KEY));
        boolean useJUL = Boolean.parseBoolean(useJULProperty);
        INSTANCE = useJUL ? new JULLoggerFactory() : new GFLoggerFactory();
    }

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
