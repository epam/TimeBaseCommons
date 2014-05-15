package deltix.util.log.gf.impl;

import org.gflogger.LoggerService;

import deltix.util.lang.Util;

public class SafeAppenderFactory extends CompoundAppenderFactory {

    private static final String MAX_ENTRIES_PER_SECOND_PROPERTY_KEY = "gflogger.safeAppender.maxEntriesPerSecond";
    private static final int DEFAULT_MAX_ENTRIES_PER_SECOND = Util.getIntSystemProperty(MAX_ENTRIES_PER_SECOND_PROPERTY_KEY, 300, 1, Integer.MAX_VALUE);

    private int maxEntriesPerSecond = DEFAULT_MAX_ENTRIES_PER_SECOND;

    @Override
    public SafeAppender createAppender(Class<? extends LoggerService> loggerServiceClass) {
        SafeAppender appender = new SafeAppender(createAppenders(loggerServiceClass), maxEntriesPerSecond);

        appender.setLogLevel(logLevel);
        appender.setEnabled(enabled);
        appender.setIndex(index);

        return appender;
    }

    @SuppressWarnings("unused")
    public int getMaxEntriesPerSecond() {
        return maxEntriesPerSecond;
    }

    @SuppressWarnings("unused")
    public void setMaxEntriesPerSecond(int maxEntriesPerSecond) {
        this.maxEntriesPerSecond = maxEntriesPerSecond;
    }

}
