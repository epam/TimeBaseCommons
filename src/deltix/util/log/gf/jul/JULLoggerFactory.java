package deltix.util.log.gf.jul;


import deltix.util.log.gf.Logger;
import deltix.util.log.gf.LoggerFactory;

public final class JULLoggerFactory extends LoggerFactory {

    @Override
    protected Logger createLogger(String name) {
        return new JULLogger(java.util.logging.Logger.getLogger(name));
    }

}
