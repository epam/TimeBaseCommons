package deltix.util.log.gf.impl;

import deltix.util.log.gf.FormattedLogEntry;
import org.gflogger.GFLog;
import org.gflogger.LogLevel;

import deltix.util.log.gf.AbstractLogger;
import deltix.util.log.gf.Level;
import deltix.util.log.gf.LogEntry;


final class GFLogger extends AbstractLogger {

    private static final ThreadLocal<GFLogEntry> ENTRY = new ThreadLocal<GFLogEntry>() {
        @Override
        protected GFLogEntry initialValue() {
            return new GFLogEntry();
        }
    };

    private static final ThreadLocal<FormattedGFLogEntry> FORMATTED_ENTRY = new ThreadLocal<FormattedGFLogEntry>() {
        @Override
        protected FormattedGFLogEntry initialValue() {
            return new FormattedGFLogEntry();
        }
    };

    private final GFLog logger;

    GFLogger(GFLog logger) {
        this.logger = logger;
    }

    @Override
    protected LogEntry log(Level level) {
        GFLogEntry entry = ENTRY.get();
        org.gflogger.GFLogEntry gfEntry;

        switch (level) {
            case TRACE:
                gfEntry = logger.trace();
                break;
            case DEBUG:
                gfEntry = logger.debug();
                break;
            case INFO:
                gfEntry = logger.info();
                break;
            case WARN:
                gfEntry = logger.warn();
                break;
            case ERROR:
                gfEntry = logger.error();
                break;
            case FATAL:
                gfEntry = logger.fatal();
                break;
            default:
                throw new IllegalArgumentException("Invalid level " + level);
        }

        entry.setEntry(gfEntry);
        return entry;
    }

    @Override
    protected FormattedLogEntry log(Level level, String template) {
        FormattedGFLogEntry entry = FORMATTED_ENTRY.get();
        org.gflogger.FormattedGFLogEntry gfEntry;

        switch (level) {
            case TRACE:
                gfEntry = logger.trace(template);
                break;
            case DEBUG:
                gfEntry = logger.debug(template);
                break;
            case INFO:
                gfEntry = logger.info(template);
                break;
            case WARN:
                gfEntry = logger.warn(template);
                break;
            case ERROR:
                gfEntry = logger.error(template);
                break;
            case FATAL:
                gfEntry = logger.fatal(template);
                break;
            default:
                throw new IllegalArgumentException("Invalid level " + level);
        }

        entry.setEntry(gfEntry);
        return entry;
    }

    @Override
    public boolean isLoggable(Level level) {
        switch (level) {
            case TRACE:
                return logger.isTraceEnabled();
            case DEBUG:
                return logger.isDebugEnabled();
            case INFO:
                return logger.isInfoEnabled();
            case WARN:
                return logger.isWarnEnabled();
            case ERROR:
                return logger.isErrorEnabled();
            case FATAL:
                return logger.isFatalEnabled();
            default:
                throw new IllegalArgumentException("Invalid level " + level);
        }
    }

    @Override
    public void setLevel(Level level) {
        LogLevel logLevel = getLogLevel(level);
        logger.setLogLevel(logLevel);
    }

    private static LogLevel getLogLevel(Level level) {
        switch (level) {
            case TRACE:
                return LogLevel.TRACE;
            case DEBUG:
                return LogLevel.DEBUG;
            case INFO:
                return LogLevel.INFO;
            case WARN:
                return LogLevel.WARN;
            case ERROR:
                return LogLevel.ERROR;
            case FATAL:
                return LogLevel.FATAL;
            default:
                throw new IllegalArgumentException("Invalid level " + level);
        }
    }

}
