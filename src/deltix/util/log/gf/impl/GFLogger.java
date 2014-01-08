package deltix.util.log.gf.impl;

import org.gflogger.GFLog;

import deltix.util.log.gf.AbstractLogger;
import deltix.util.log.gf.Level;
import deltix.util.log.gf.LogEntry;

final class GFLogger extends AbstractLogger {

    private final GFLog logger;
    private final ThreadLocal<GFLogEntry> threadLocalEntry = new ThreadLocal<GFLogEntry>() {
        @Override
        protected GFLogEntry initialValue() {
            return new GFLogEntry();
        }
    };

    GFLogger(GFLog logger) {
        this.logger = logger;
    }

    @Override
    protected LogEntry log(Level level) {
        GFLogEntry entry = threadLocalEntry.get();
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
                throw new IllegalArgumentException("Invalid level: " + level);
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
                throw new IllegalArgumentException("Invalid level: " + level);
        }
    }
}
