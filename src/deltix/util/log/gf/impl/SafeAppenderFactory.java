package deltix.util.log.gf.impl;

import org.gflogger.LoggerService;

public class SafeAppenderFactory extends CompoundAppenderFactory {

    static final String MAX_ENTRIES_PER_SECOND_PROPERTY_KEY = "gflogger.safeAppender.maxEntriesPerSecond";
    static final int DEFAULT_MAX_ENTRIES_PER_SECOND = 300;

    int maxEntriesPerSecond = getDefaultMaxEntriesPerSecond();

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

    static int getDefaultMaxEntriesPerSecond() {
        String maxEntriesPerSecondStr = System.getProperty(MAX_ENTRIES_PER_SECOND_PROPERTY_KEY);
        return maxEntriesPerSecondStr == null ?
                DEFAULT_MAX_ENTRIES_PER_SECOND :
                Integer.parseInt(maxEntriesPerSecondStr);
    }

}
