package deltix.util.lang;

import deltix.gflog.Log;
import deltix.gflog.LogFactory;
import deltix.gflog.LogLevel;

/**
 * Logs exceptions to specified logger.
 */
public class LoggingExceptionHandler implements ExceptionHandler {
    private final Log logger;
    private final LogLevel level;
    private final String msg;

    public LoggingExceptionHandler(Log logger, LogLevel level) {
        this(logger, level, null);
    }

    public LoggingExceptionHandler(Log logger, String msg) {
        this(logger, LogLevel.ERROR, msg);
    }

    public LoggingExceptionHandler(Log logger) {
        this(logger, LogLevel.ERROR, null);
    }

    public LoggingExceptionHandler(Log logger, LogLevel level, String msg) {
        this.logger = logger;
        this.level = level;
        this.msg = msg;
    }

    public void handle(Throwable x) {
        logger.log(level).append(msg).append(x).commit();
    }

    /**
     * Logs to "deltix.util".
     */
    public static final LoggingExceptionHandler INSTANCE =
            new LoggingExceptionHandler(LogFactory.getLog("deltix.util"));
}
