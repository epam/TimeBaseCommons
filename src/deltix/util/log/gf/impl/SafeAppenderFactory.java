package deltix.util.log.gf.impl;


import org.gflogger.Appender;
import org.gflogger.LogEntryItem;
import org.gflogger.LoggerService;
import org.gflogger.appender.AbstractAppenderFactory;
import org.gflogger.appender.AppenderFactory;

public class SafeAppenderFactory extends AbstractAppenderFactory {

    private static final String MAX_ENTRIES_PER_SECOND_PROPERTY_KEY = "gflogger.safeAppender.maxEntriesPerSecond";
    private static final int DEFAULT_MAX_ENTRIES_PER_SECOND = 500;

    private AppenderFactory[] factories;

    public void setFactories(AppenderFactory[] factories) {
        this.factories = factories;
    }

    @Override
    public Appender createAppender(Class<? extends LoggerService> loggerServiceClass) {
        if (factories != null && factories.length > 0) {
            Appender[] appenders = new Appender[factories.length];

            for (int i = 0; i < appenders.length; i++)
                appenders[i] = factories[i].createAppender(loggerServiceClass);

            @SuppressWarnings("unchecked") Appender<LogEntryItem>[] apps = appenders;
            return new SafeAppender(apps, getMaxEntriesPerSecond());
        } else {
            return new NullAppender(multibyte);
        }
    }

    private static int getMaxEntriesPerSecond() {
        String maxEntriesPerSecondStr = System.getProperty(MAX_ENTRIES_PER_SECOND_PROPERTY_KEY);
        return maxEntriesPerSecondStr == null ?
                DEFAULT_MAX_ENTRIES_PER_SECOND :
                Integer.parseInt(maxEntriesPerSecondStr);
    }

}
