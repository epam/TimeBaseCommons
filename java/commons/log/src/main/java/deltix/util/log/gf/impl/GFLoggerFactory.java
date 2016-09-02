package deltix.util.log.gf.impl;

import deltix.util.log.gf.Logger;
import deltix.util.log.gf.LoggerFactory;
import org.gflogger.GFLogFactory;


public final class GFLoggerFactory extends LoggerFactory {

    public GFLoggerFactory() {
        GFLoggerConfigurator.configureWithShutdown();
    }

    @Override
    protected Logger createLogger(String name) {
        return new GFLogger(GFLogFactory.getLog(name));
    }

}
