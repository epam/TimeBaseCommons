package deltix.util.log.gf.jul;

import deltix.util.log.gf.AbstractLogger;
import deltix.util.log.gf.FormattedLogEntry;
import deltix.util.log.gf.Level;
import deltix.util.log.gf.LogEntry;
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
    public void setLevel(Level level) {
        java.util.logging.Level logLevel = getJULLogLevel(level);
        logger.setLevel(logLevel);
    }

    @Override
    public Level getLevel() {
        int level = logger.getLevel().intValue();
        if (level < java.util.logging.Level.FINER.intValue())
            return Level.TRACE;
        else if (level < java.util.logging.Level.INFO.intValue())
            return Level.DEBUG;
        else if (level < java.util.logging.Level.WARNING.intValue())
            return Level.INFO;
        else if (level < java.util.logging.Level.SEVERE.intValue())
           return Level.WARN;
        else
            return Level.ERROR;
    }

    @Override
    public boolean isLoggable(Level level) {
        return logger.isLoggable(getJULLogLevel(level));
    }

    @Override
    protected LogEntry logEntry(Level level) {
        return entry(level, null);
    }

    @Override
    protected FormattedLogEntry logEntry(Level level, String template) {
        requireNonNull(template, "Template can be null");
        return entry(level, template);
    }

    private JULLogEntry entry(Level level, String template) {
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
