package deltix.util.log.gf;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

import org.gflogger.GFLog;
import org.gflogger.GFLogEntry;
import org.gflogger.GFLogFactory;
import org.gflogger.LogLevel;
import deltix.util.log.LoggerUtils;
import deltix.util.text.SimpleMessageFormat;

/**
 * Forwards messages from JUL to GFL
 */
public class ForwardingHandler extends Handler {

    @Override
    public void publish(LogRecord record) {
        GFLog logger = GFLogFactory.getLog(record.getLoggerName());
        LogLevel level = LoggerUtils.getGFLLevel(record.getLevel());
        GFLogEntry entry = log(level, logger);

        if (entry != null) {
            Formatter.format(record, entry);
            entry.commit();
        }
    }

    @Override
    public void flush() {
        // skip
    }

    @Override
    public void close() {
        // skip
    }

    private static GFLogEntry log(LogLevel level, GFLog logger) {
        switch (level) {
            case TRACE:
                return logger.isTraceEnabled() ? logger.trace() : null;
            case DEBUG:
                return logger.isDebugEnabled() ? logger.debug() : null;
            case INFO:
                return logger.isInfoEnabled() ? logger.info() : null;
            case WARN:
                return logger.isWarnEnabled() ? logger.warn() : null;
            case ERROR:
                return logger.isErrorEnabled() ? logger.error() : null;
            case FATAL:
                return logger.isFatalEnabled() ? logger.fatal() : null;
            default:
                throw new IllegalArgumentException(level.toString());
        }
    }

    private static final class Formatter {

        private Formatter() {
            throw new AssertionError("Not for you!");
        }

        public static void format(LogRecord record, GFLogEntry entry) {
            appendMessage(record, entry);
            appendExceptionIfAny(record, entry);
        }

        private static void appendMessage(LogRecord record, GFLogEntry entry) {
            String message = record.getMessage();
            Object[] params = record.getParameters();
            if (message == null || params == null || params.length == 0)
                entry.append(message);
            else
                SimpleMessageFormat.format(entry, message, params);
        }

        private static void appendExceptionIfAny(LogRecord record, GFLogEntry entry) {
            Throwable exception = record.getThrown();
            if (exception != null)
                entry.append(exception);
        }

    }

}
