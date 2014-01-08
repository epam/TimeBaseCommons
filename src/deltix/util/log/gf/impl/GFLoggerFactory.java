package deltix.util.log.gf.impl;

import org.gflogger.GFLogFactory;

import deltix.util.log.gf.Logger;
import deltix.util.log.gf.LoggerFactory;

public final class GFLoggerFactory extends LoggerFactory {

    @Override
    protected Logger createLogger(String name) {
        return new GFLogger(GFLogFactory.getLog(name));
    }
}
