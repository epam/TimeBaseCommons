package com.epam.deltix.qsrv.util.log;

import com.epam.deltix.util.io.UncheckedIOException;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

/**
 * Description: deltix.util.log.RollingFileHandler
 * Date: Jul 27, 2009
 *
 * @author Nickolay Dul
 */
public class RollingFileHandler extends FileHandler {
    private static final Level DEFAULT_PUSH_LEVEL = Level.SEVERE;
    private static final long DEFAULT_PUSH_PERIOD = 1000; // 1s

    private Level pushLevel;
    private long pushPeriod;

    private long lastPushTime;

    public RollingFileHandler(String pattern, int limit, int count, boolean append,
                              Level pushLevel, long pushPeriod)
        throws IOException, SecurityException {
        super(pattern, limit, count, append);
        initialize(pushLevel, pushPeriod);
    }

    public RollingFileHandler() throws IOException, SecurityException {
        super();
        LogManager manager = LogManager.getLogManager();
        initialize(getPushLevel(manager, DEFAULT_PUSH_LEVEL), 
                   getPushPeriod(manager, DEFAULT_PUSH_PERIOD));
    }

    private void initialize(Level pushLevel, long pushPeriod) {
        this.pushLevel = pushLevel;
        this.pushPeriod = pushPeriod;

        lastPushTime = System.currentTimeMillis();
    }

    @Override
    public void publish(LogRecord record) {
        super.publish(record);

        // flush buffered output if needed
        if ((record.getLevel().intValue() >= pushLevel.intValue()) ||
            (System.currentTimeMillis() - lastPushTime) >= pushPeriod) {
            push();
        }
    }

    @Override
    public void flush() {
        // do nothing
    }

    /**
     * Flush any buffered output to the file stream.
     */
    public void push() {
        // perform flushing
        super.flush();
        // remember last push time
        lastPushTime = System.currentTimeMillis();
    }

    public Level getPushLevel() {
        return pushLevel;
    }

    public void setPushLevel(Level pushLevel) {
        this.pushLevel = pushLevel;
    }

    public long getPushPeriod() {
        return pushPeriod;
    }

    public void setPushPeriod(long pushPeriod) {
        this.pushPeriod = pushPeriod;
    }

    public RollingFileHandler copy(String pattern) {
        try {
            LogManager logManager = LogManager.getLogManager();
            String handlerClassName = RollingFileHandler.class.getName();
            // read predefined FileHandler properties if any
            int limit = parseInt(logManager.getProperty(handlerClassName + ".limit"), 10000000);
            int count = parseInt(logManager.getProperty(handlerClassName + ".count"), 30);

            RollingFileHandler handler = new RollingFileHandler(pattern, limit, count, false, pushLevel, pushPeriod);
            handler.setLevel(getLevel());
            handler.setFormatter(getFormatter());
            handler.setFilter(getFilter());
            return handler;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    ////////////////////////// FACTORY METHOD ///////////////////////

    private static int parseInt(String value, int defaultValue) {
        value = value != null ? value.trim() : null;
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

    private static Level getPushLevel(LogManager manager, Level defaultLevel) {
        String pushLevelValue = manager.getProperty(RollingFileHandler.class.getName() + ".push");
        return pushLevelValue != null ? Level.parse(pushLevelValue) : defaultLevel;
    }

    private static long getPushPeriod(LogManager manager, long defaultPushPeriod) {
        String pushPeriodValue = manager.getProperty(RollingFileHandler.class.getName() + ".period");
        return pushPeriodValue != null ? Long.parseLong(pushPeriodValue) : defaultPushPeriod;
    }
}
