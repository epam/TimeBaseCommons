package deltix.util.lang;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Logs exceptions to specified logger.
 */
public class LoggingExceptionHandler implements ExceptionHandler {
    private final Logger                logger;
    private final Level                 level;
    private final String                msg;

    public LoggingExceptionHandler (Logger logger, Level level) {
        this (logger, level, null);
    }

    public LoggingExceptionHandler (Logger logger, String msg) {
        this (logger, Level.SEVERE, msg);
    }

    public LoggingExceptionHandler (Logger logger) {
        this (logger, Level.SEVERE, null);
    }

    public LoggingExceptionHandler (Logger logger, Level level, String msg) {
        this.logger = logger;
        this.level = level;
        this.msg = msg;
    }
    
    public void                 handle (Throwable x) {
        logger.log (level, msg, x);
    }
    
    /**
     *  Logs to "deltix.util".
     */
    public static final LoggingExceptionHandler     INSTANCE =
        new LoggingExceptionHandler (Util.LOGGER);
}
