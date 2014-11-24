package deltix.util.log.gf;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import org.gflogger.GFLog;
import org.gflogger.GFLogEntry;
import org.gflogger.GFLogFactory;
import org.gflogger.LogLevel;
import deltix.util.log.LoggerUtils;

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
            if (message == null)
                entry.append(message);
            else
                appendMessage(message, params, entry);
        }

        private static void appendExceptionIfAny(LogRecord record, GFLogEntry entry) {
            Throwable exception = record.getThrown();
            if (exception != null)
                entry.append(exception);
        }

        private static void appendMessage(String message, Object[] params, GFLogEntry entry) {
            for (int index = 0; index < message.length(); index++) {
                char character = message.charAt(index);
                if (character == '\'') {
                    int endQuoteIndex = message.indexOf('\'', index + 1);
                    if (endQuoteIndex == -1) {
                        entry.append('\''); // this is just a single quote
                    } else {
                        if (index + 1 == endQuoteIndex)
                            entry.append('\'');  // '' represents a single quote
                        else
                            entry.append(message, index + 1, endQuoteIndex);

                        index = endQuoteIndex;
                    }
                } else if (character == '{') {
                    int closeBraceIndex = message.indexOf('}', index + 1);
                    if (closeBraceIndex == -1)
                        throw new InvalidFormatException(message, index, "Missing close curly brace '}'");

                    if (index + 1 == closeBraceIndex)
                        throw new InvalidFormatException(message, index, "Missing argument number inside braces {}");

                    int paramIndex = getInteger(message, index + 1, closeBraceIndex);
                    if (paramIndex >= params.length)
                        throw new InvalidFormatException(message, index, "Formatting string refers to non-existing argument #" + paramIndex + " when only " + params.length + " arguments are passed");

                    entry.append(params[paramIndex]);
                    index = closeBraceIndex;
                } else {
                    entry.append(character);
                }
            }
        }

        private static int getInteger(String format, int start, int end) {
            int integer = 0;
            for (; start < end; start++) {
                char character = format.charAt(start);
                if (!Character.isDigit(character))
                    throw new InvalidFormatException(format, start, "Argument index contains non-digit character: '" + character + '\'');

                integer = 10 * integer + (character - '0');
            }

            return integer;
        }

        public static final class InvalidFormatException extends IllegalArgumentException {

            public InvalidFormatException(String format, int pos, String error) {
                super("Format error at position " + pos + ": " + error + ". Format string: \"" + format + '\"');
            }

        }

    }

}
