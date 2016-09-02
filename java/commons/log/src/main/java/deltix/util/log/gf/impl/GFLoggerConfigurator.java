package deltix.util.log.gf.impl;

import org.gflogger.GFLoggerBuilder;
import org.gflogger.LogLevel;
import org.gflogger.appender.ConsoleAppender;
import org.gflogger.appender.SingleAppenderFactory;
import org.gflogger.config.xml.Configuration;
import org.gflogger.config.xml.Configurator;
import static org.gflogger.config.xml.Configurator.XML_FILE_PATH_PROPERTY;
import org.gflogger.config.xml.DLoggerServiceFactory;
import org.gflogger.config.xml.LoggerServiceFactory;
import org.gflogger.config.xml.XmlConfigurationLoader;
import org.gflogger.helpers.LogLog;

import java.io.File;


public class GFLoggerConfigurator {

    private static final int DEFAULT_ENTRIES = 1 << 10;
    private static final int DEFAULT_MESSAGE_SIZE = 1 << 13;
    private static final int DEFAULT_CONSOLE_APPENDER_BUFFER_SIZE = DEFAULT_MESSAGE_SIZE + 1024; // layout
    private static final String DEFAULT_LAYOUT_PATTERN = "%d{d MMM HH:mm:ss} %p %m%n";

    private static boolean configured;

    public static synchronized void configureWithShutdown() {
        configure();
        addShutdownHook();
    }

    public static void configureWithShutdown(String file) throws Exception {
        configureWithShutdown(new File(file));
    }

    public static synchronized void configureWithShutdown(File file) throws Exception {
        configure(file);
        addShutdownHook();
    }

    public static synchronized void configureWithShutdown(Configuration configuration) {
        configure(configuration);
        addShutdownHook();
    }

    public static synchronized void configure() {
        if (!configured) {
            String file = System.getProperty(XML_FILE_PATH_PROPERTY);
            Configuration configuration = null;

            if (file != null) {
                try {
                    configuration = XmlConfigurationLoader.load(System.getProperties(), new File(file));
                    LogLog.info("GFLogger uses configuration from file: " + file);
                } catch (Exception e) {
                    LogLog.error("Can't load GFLogger configuration from file: " + file, e);
                }
            }

            if (configuration == null) {
                configuration = createDefaultConfiguration();
                LogLog.info(String.format("GFLogger uses default configuration: entries=%s, maxMessageSize=%s, appender=console, layout=%s",
                        DEFAULT_ENTRIES, DEFAULT_MESSAGE_SIZE, DEFAULT_LAYOUT_PATTERN));
            }

            configure(configuration);
        }
    }

    public static void configure(String file) throws Exception {
        configure(new File(file));
    }

    public static synchronized void configure(File file) throws Exception {
        unconfigure();
        Configurator.configure(file);
        configured = true;
    }

    public static synchronized void configure(Configuration configuration) {
        unconfigure();
        Configurator.configure(configuration);
        configured = true;
    }

    public static synchronized void unconfigure() {
        if (configured) {
            Configurator.unconfigure();
            configured = false;
        }
    }

    public static synchronized boolean isConfigured() {
        return configured;
    }

    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(GFLoggerConfigurator::unconfigure));
    }

    private static Configuration createDefaultConfiguration() {
        Configuration configuration = new Configuration();

        LoggerServiceFactory serviceFactory = new DLoggerServiceFactory();
        serviceFactory.setCount(DEFAULT_ENTRIES);
        serviceFactory.setMaxMessageSize(DEFAULT_MESSAGE_SIZE);
        configuration.setLoggerServiceFactory(serviceFactory);

        org.gflogger.appender.ConsoleAppenderFactory appenderFactory = new org.gflogger.appender.ConsoleAppenderFactory();
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
