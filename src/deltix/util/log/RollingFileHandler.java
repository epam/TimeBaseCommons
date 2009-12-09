package deltix.util.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Formatter;

import deltix.util.lang.StringUtils;

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

    ////////////////////////// FACTORY METHOD ///////////////////////

    private static Level getPushLevel(LogManager manager, Level defaultLevel) {
        String pushLevelValue = manager.getProperty(RollingFileHandler.class.getName() + ".push");
        return pushLevelValue != null ? Level.parse(pushLevelValue) : defaultLevel;
    }

    private static long getPushPeriod(LogManager manager, long defaultPushPeriod) {
        String pushPeriodValue = manager.getProperty(RollingFileHandler.class.getName() + ".period");
        return pushPeriodValue != null ? Long.parseLong(pushPeriodValue) : defaultPushPeriod;
    }

    public static RollingFileHandler createByConfig(String defaultPattern,
                                                    int defaultLimit,
                                                    int defaultCount,
                                                    Level defaultLevel,
                                                    boolean append,
                                                    Formatter defaultFormatter) {
        try {
            LogManager logManager = LogManager.getLogManager();
            String handlerClassName = FileHandler.class.getName();
            // read predefined FileHandler properties if any
            String pattern = logManager.getProperty(handlerClassName + ".pattern");
            String limitValue = logManager.getProperty(handlerClassName + ".limit");
            String countValue = logManager.getProperty(handlerClassName + ".count");
            String levelValue = logManager.getProperty(handlerClassName + ".level");

            Formatter formatter = defaultFormatter;
            String formatterClass = StringUtils.trim(logManager.getProperty(handlerClassName + ".formatter"));
            if (formatterClass != null) {
                try {
                    Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(formatterClass);
                    formatter = (Formatter) clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            RollingFileHandler handler =
                new RollingFileHandler(pattern != null ? pattern : defaultPattern,
                                       limitValue != null ? Integer.parseInt(limitValue) : defaultLimit,
                                       countValue != null ? Integer.parseInt(countValue) : defaultCount, append,
                                       getPushLevel(logManager, DEFAULT_PUSH_LEVEL),
                                       getPushPeriod(logManager, DEFAULT_PUSH_PERIOD));
            handler.setLevel(levelValue != null ? Level.parse(levelValue) : defaultLevel);

            // set formatter
            handler.setFormatter(formatter);

            return handler;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
