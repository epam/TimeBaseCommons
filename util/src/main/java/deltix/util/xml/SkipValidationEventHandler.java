package deltix.util.xml;

import deltix.util.lang.Util;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class SkipValidationEventHandler implements ValidationEventHandler {

    private Logger logger;

    public static final ValidationEventHandler  INSTANCE =
            new SkipValidationEventHandler (Util.LOGGER);

    public SkipValidationEventHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean handleEvent (ValidationEvent e) {
        logger.log(Level.WARNING, "Error processing xml: " + e.getMessage());
        return true;
    }
}