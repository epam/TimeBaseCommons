package deltix.util.log.gf.impl;

import deltix.util.log.gf.Logger;
import deltix.util.log.gf.LoggerFactory;
import org.gflogger.GFLogFactory;
import org.gflogger.GFLoggerBuilder;
import org.gflogger.LogLevel;
import org.gflogger.appender.ConsoleAppender;
import org.gflogger.appender.ConsoleAppenderFactory;
import org.gflogger.appender.SingleAppenderFactory;
import org.gflogger.config.xml.Configuration;
import org.gflogger.config.xml.DLoggerServiceFactory;
import org.gflogger.config.xml.LoggerServiceFactory;
import org.gflogger.helpers.LogLog;


public final class GFLoggerFactory extends LoggerFactory {

    private static final int DEFAULT_ENTRIES = 1 << 10;
    private static final int DEFAULT_MESSAGE_SIZE = 1 << 13;
    private static final int DEFAULT_CONSOLE_APPENDER_BUFFER_SIZE = DEFAULT_MESSAGE_SIZE + 1024; // layout
    private static final String DEFAULT_LAYOUT_PATTERN = "%d{d MMM HH:mm:ss} %p %m%n";

    public GFLoggerFactory() {
        if (!GFLoggerConfigurator.isConfigured()) {
            Configuration configuration = createDefaultConfiguration();
            GFLoggerConfigurator.configureWithShutdown(configuration);

            LogLog.info(String.format("Using default Garbage Free Logger configuration: entries=%s, maxMessageSize=%s, appender=console, layout=%s",
                    DEFAULT_ENTRIES, DEFAULT_MESSAGE_SIZE, DEFAULT_LAYOUT_PATTERN));
        }
    }

    @Override
    protected Logger createLogger(String name) {
        return new GFLogger(GFLogFactory.getLog(name));
    }

    private static Configuration createDefaultConfiguration() {
        Configuration configuration = new Configuration();

        LoggerServiceFactory serviceFactory = new DLoggerServiceFactory();
        serviceFactory.setCount(DEFAULT_ENTRIES);
        serviceFactory.setMaxMessageSize(DEFAULT_MESSAGE_SIZE);
        configuration.setLoggerServiceFactory(serviceFactory);

        ConsoleAppenderFactory appenderFactory = new ConsoleAppenderFactory();
        appenderFactory.setBufferSize(DEFAULT_CONSOLE_APPENDER_BUFFER_SIZE);
        appenderFactory.setImmediateFlush(true);
        appenderFactory.setLayoutPattern(DEFAULT_LAYOUT_PATTERN);

        SingleAppenderFactory appenderFactoryWrapper = new SingleAppenderFactory(ConsoleAppender.NAME, appenderFactory);
        configuration.addAppenderFactory(appenderFactoryWrapper);

        GFLoggerBuilder rootLogger = new GFLoggerBuilder(LogLevel.INFO, null, appenderFactoryWrapper);
        configuration.addLoggerBuilder(rootLogger);

        return configuration;
    }

}
