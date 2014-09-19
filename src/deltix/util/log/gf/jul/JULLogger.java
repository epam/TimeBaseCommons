package deltix.util.log.gf.jul;

import java.util.logging.Logger;

import deltix.util.log.gf.AbstractLogger;
import deltix.util.log.gf.Level;
import deltix.util.log.gf.LogEntry;

final class JULLogger extends AbstractLogger {

    private static final ThreadLocal<JULLogEntry> THREAD_LOCAL_ENTRY = new ThreadLocal<JULLogEntry>() {

        @Override
        protected JULLogEntry initialValue() {
            return new JULLogEntry();
        }

    };

    private final Logger logger;

    JULLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    protected LogEntry log(Level level) {
        java.util.logging.Level julLogLevel = getJULLogLevel(level);

        JULLogEntry entry = THREAD_LOCAL_ENTRY.get();
        if (!entry.isCommitted()) {
            System.err.println("JUL log entry is not committed properly at thread: " + Thread.currentThread() + ". Content: " + entry);
            entry.commit();
        }

        entry.clear();
        entry.setLogger(logger);
        entry.setLevel(julLogLevel);

        return entry;
    }

    @Override
    public boolean isLoggable(Level level) {
        return logger.isLoggable(getJULLogLevel(level));
    }

    private static java.util.logging.Level getJULLogLevel(Level level) {
        switch (level) {
            case TRACE:
                return java.util.logging.Level.FINEST;
            case DEBUG:
                return java.util.logging.Level.FINE;
            case INFO:
                return java.util.logging.Level.INFO;
            case WARN:
                return java.util.logging.Level.WARNING;
            case ERROR:
            case FATAL:
                return java.util.logging.Level.SEVERE;
            default:
                throw new IllegalArgumentException(level + " is not supported");
        }
    }

}
