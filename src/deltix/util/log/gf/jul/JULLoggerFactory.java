package deltix.util.log.gf.jul;


import deltix.util.log.TerseFormatter;
import deltix.util.log.gf.Logger;
import deltix.util.log.gf.LoggerFactory;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;

public final class JULLoggerFactory extends LoggerFactory {

    public static final String CONFIGURATION_LOADED_FLAG = "deltix.util.log.gf.jul.JULLoggerFactory.configuration.loaded";

    public JULLoggerFactory() {
        LogManager logManager = LogManager.getLogManager();
        boolean configurationLoaded = Boolean.parseBoolean(logManager.getProperty(CONFIGURATION_LOADED_FLAG));
        if (!configurationLoaded) {
             // Let's use terse log format by default (otherwise we will see JULLogger.commit() as log source for each log entry
            java.util.logging.Logger root = logManager.getLogger("");
            Handler [] handlers = root.getHandlers();
            for(Handler handler : handlers)
                root.removeHandler(handler);

            ConsoleHandler console = new ConsoleHandler();
            root.addHandler(console);
            console.setFormatter(new TerseFormatter());

        }
    }

    @Override
    protected Logger createLogger(String name) {
        return new JULLogger(java.util.logging.Logger.getLogger(name));
    }

}
