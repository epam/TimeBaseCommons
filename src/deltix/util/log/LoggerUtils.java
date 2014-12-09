package deltix.util.log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.gflogger.LogLevel;


public final class LoggerUtils {

    private static final Map<Level, LogLevel> JUL_TO_GFLLEVEL = Collections.unmodifiableMap(
            new HashMap<Level, LogLevel>() {
                {
                    put(Level.ALL, LogLevel.TRACE);
                    put(Level.FINEST, LogLevel.TRACE);
                    put(Level.FINER, LogLevel.TRACE);
                    put(Level.FINE, LogLevel.DEBUG);
                    put(Level.CONFIG, LogLevel.DEBUG);
                    put(Level.INFO, LogLevel.INFO);
                    put(Level.WARNING, LogLevel.WARN);
                    put(Level.SEVERE, LogLevel.ERROR);
                    put(Level.OFF, LogLevel.FATAL);

                    // deltix level (TomcatCmd.LEVEL_STARTUP) - NB: do not reference TomcatCmd
                    put(new java.util.logging.Level("STARTUP", java.util.logging.Level.SEVERE.intValue() - 10) {
                    }, LogLevel.INFO);
                }
            }
    );

    private LoggerUtils() {
        throw new AssertionError("Not for you!");
    }

    public static LogLevel getGFLLevel(Level julLevel) {
        assert julLevel != null;

        LogLevel gflLevel = JUL_TO_GFLLEVEL.get(julLevel);
        if (gflLevel == null) {
            if (julLevel.intValue() < Level.FINE.intValue())
                gflLevel = LogLevel.TRACE;
            else if (julLevel.intValue() < Level.INFO.intValue())
                gflLevel = LogLevel.DEBUG;
            else if (julLevel.intValue() < Level.WARNING.intValue())
                gflLevel = LogLevel.INFO;
            else if (julLevel.intValue() < Level.SEVERE.intValue())
                gflLevel = LogLevel.WARN;
            else
                gflLevel = LogLevel.ERROR;
        }

        return gflLevel;
    }

    public static Level getJULLevel(LogLevel gflLevel) {
        switch (gflLevel) {
            case TRACE:
                return Level.FINEST;
            case DEBUG:
                return Level.FINE;
            case INFO:
                return Level.INFO;
            case WARN:
                return Level.WARNING;
            case ERROR:
                return Level.SEVERE;
            case FATAL:
                return Level.OFF;
            default:
                throw new IllegalArgumentException("Unsupported gflogger level: " + gflLevel);
        }
    }

}
