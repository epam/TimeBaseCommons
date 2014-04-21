package deltix.util.log.gf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import deltix.util.text.SimpleMessageFormat;

/**
 * Forwards messages from java.util.logging.Logger to deltix.util.log.gf.Logger
 */
public class ForwardingHandler extends Handler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForwardingHandler.class.getName());
    private static final Map<java.util.logging.Level, Level> JLEVEL_TO_LEVEL = Collections.unmodifiableMap(
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
        Level level = getLevel(record.getLevel());

        if (LOGGER.isLoggable(level)) {
            String msg = getMsg(record);
            LogEntry entry = LOGGER.level(level).append(msg);

            Throwable exception = record.getThrown();
            if (exception != null)
                entry.append(exception);

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

    private static Level getLevel(java.util.logging.Level jLevel) {
        assert jLevel != null;

        Level level = JLEVEL_TO_LEVEL.get(jLevel);
        if (level == null) {
            if (jLevel.intValue() < java.util.logging.Level.FINE.intValue())
                level = Level.TRACE;
            else if (jLevel.intValue() < java.util.logging.Level.INFO.intValue())
                level = Level.DEBUG;
            else if (jLevel.intValue() < java.util.logging.Level.WARNING.intValue())
                level = Level.INFO;
            else if (jLevel.intValue() < java.util.logging.Level.SEVERE.intValue())
                level = Level.WARN;
            else level = Level.ERROR;
        }

        return level;
    }

    private static String getMsg(LogRecord record) {
        StringBuilder buffer = new StringBuilder(512);

        String msg = record.getMessage();
        Object[] params = record.getParameters();
        if (params != null && params.length > 0)
            SimpleMessageFormat.format(buffer, msg, params);
        else
            buffer.append(msg);

        return buffer.toString();
    }

}
