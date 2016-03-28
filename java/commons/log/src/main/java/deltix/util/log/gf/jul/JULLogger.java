package deltix.util.log.gf.jul;

import deltix.util.log.gf.AbstractLogger;
import deltix.util.log.gf.FormattedLogEntry;
import deltix.util.log.gf.Level;
import deltix.util.log.gf.LogEntry;

import java.util.Objects;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;


final class JULLogger extends AbstractLogger {

    private static final ThreadLocal<JULLogEntry> ENTRY = new ThreadLocal<JULLogEntry>() {
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
        return logEntry(level, null);
    }

    @Override
    protected FormattedLogEntry log(Level level, String template) {
        requireNonNull(template, "template is null");
        return logEntry(level, template);
    }

    @Override
    public boolean isLoggable(Level level) {
        return logger.isLoggable(getJULLogLevel(level));
    }

    @Override
    public void setLevel(Level level) {
        java.util.logging.Level logLevel = getJULLogLevel(level);
        logger.setLevel(logLevel);
    }

    private JULLogEntry logEntry(Level level, String template) {
        java.util.logging.Level julLogLevel = getJULLogLevel(level);

        JULLogEntry entry = ENTRY.get();
        if (!entry.isCommitted()) {
            System.err.println("JUL log entry is not committed properly at thread: " + Thread.currentThread() + ". Content: " + entry);
            entry.commit();
        }

        entry.setCommitted(false);
        entry.setTemplate(template);
        entry.setLogger(logger);
        entry.setLevel(julLogLevel);

        return entry;
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
