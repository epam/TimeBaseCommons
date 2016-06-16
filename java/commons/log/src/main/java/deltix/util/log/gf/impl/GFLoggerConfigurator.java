package deltix.util.log.gf.impl;

import org.gflogger.config.xml.Configuration;
import org.gflogger.config.xml.Configurator;

import java.io.File;


public class GFLoggerConfigurator {

    private static boolean configured;

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

}
