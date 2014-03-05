package deltix.util.log.gf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import deltix.util.lang.Util;
import deltix.util.text.SimpleMessageFormat;

/**
 * Forwards messages from java.util.logging.Logger to deltix.util.log.gf.Logger
 */
public class ForwardingHandler extends Handler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForwardingHandler.class.getName());
    private static final Map<java.util.logging.Level, Level> jLevelToLevel = Collections.unmodifiableMap(
            new HashMap<java.util.logging.Level, Level>() {
                {
                    put(java.util.logging.Level.ALL, Level.TRACE);
                    put(java.util.logging.Level.FINEST, Level.TRACE);
                    put(java.util.logging.Level.FINER, Level.TRACE);
                    put(java.util.logging.Level.FINE, Level.DEBUG);
                    put(java.util.logging.Level.CONFIG, Level.DEBUG);
                    put(java.util.logging.Level.INFO, Level.INFO);
                    put(java.util.logging.Level.WARNING, Level.WARN);
                    put(java.util.logging.Level.SEVERE, Level.ERROR);
                    put(java.util.logging.Level.OFF, Level.FATAL);

                    // deltix level (TomcatCmd.LEVEL_STARTUP) - NB: do not reference TomcatCmd
                    put(new java.util.logging.Level("STARTUP", java.util.logging.Level.SEVERE.intValue() - 10) { }, Level.INFO);
                }
            }
    );

    @Override
    public void publish(LogRecord record) {
        java.util.logging.Level jLevel = record.getLevel();
        Level level = jLevelToLevel.get(jLevel);
        if (level == null)
            throw new IllegalArgumentException("Log level is not supported: " + jLevel);

        if (!LOGGER.isLoggable(level))
            throw new IllegalStateException("GF Logging is not properly configured. All levels must be loggable.");

        String msg = getMsg(record);
        LogEntry entry = LOGGER.level(level).append(msg);

        Throwable throwable = record.getThrown();
        if (throwable != null) {
            entry.append(Util.NATIVE_LINE_BREAK);
            entry.append(throwable);
        }

        entry.commit();
    }

    private String getMsg(LogRecord record) {
        StringBuilder buffer = new StringBuilder(512);

        String msg = record.getMessage();
        Object[] params = record.getParameters();
        if (params != null && params.length > 0)
            SimpleMessageFormat.format(buffer, msg, params);
        else
            buffer.append(msg);

        return buffer.toString();
    }

    @Override
    public void flush() {
        // skip
    }

    @Override
    public void close() {
        // skip
    }
}
