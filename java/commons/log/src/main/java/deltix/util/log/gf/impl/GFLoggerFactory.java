package deltix.util.log.gf.impl;

import org.gflogger.GFLogFactory;
import org.gflogger.GFLoggerBuilder;
import org.gflogger.LogLevel;
import org.gflogger.appender.ConsoleAppender;
import org.gflogger.appender.ConsoleAppenderFactory;
import org.gflogger.appender.SingleAppenderFactory;
import org.gflogger.config.xml.Configuration;
import org.gflogger.config.xml.Configurator;
import org.gflogger.config.xml.DLoggerServiceFactory;
import org.gflogger.config.xml.LoggerServiceFactory;

import deltix.util.log.gf.Logger;
import deltix.util.log.gf.LoggerFactory;

public final class GFLoggerFactory extends LoggerFactory {

    public static final String CONFIGURATION_LOADED_FLAG = "deltix.util.log.gf.impl.GFLoggerFactory.configuration.loaded";

    private static final int DEFAULT_ENTRIES = 1 << 10;
    private static final int DEFAULT_MESSAGE_SIZE = 1 << 13;
    private static final int DEFAULT_CONSOLE_APPENDER_BUFFER_SIZE = DEFAULT_MESSAGE_SIZE + 1024; // layout
    private static final String DEFAULT_LAYOUT_PATTERN = "%d{d MMM HH:mm:ss} %p %m%n";

    public GFLoggerFactory() {
        boolean configurationLoaded = Boolean.parseBoolean(System.getProperty(CONFIGURATION_LOADED_FLAG));
        if (!configurationLoaded) {
            Configuration configuration = createDefaultConfiguration();
            Configurator.configure(configuration);
            Runtime.getRuntime().addShutdownHook(createUnconfigurer());
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

    private static Thread createUnconfigurer() {
        return new Thread(){
            @Override
            public void run() {
                Configurator.unconfigure();
            }
        };
    }

}
