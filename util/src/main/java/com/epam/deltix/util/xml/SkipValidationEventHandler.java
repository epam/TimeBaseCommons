package com.epam.deltix.util.xml;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

public class SkipValidationEventHandler implements ValidationEventHandler {

    private Log logger;

    public static final ValidationEventHandler  INSTANCE =
            new SkipValidationEventHandler (LogFactory.getLog(ValidationEventHandler.class));

    public SkipValidationEventHandler(Log logger) {
        this.logger = logger;
    }

    @Override
    public boolean handleEvent (ValidationEvent e) {
        logger.warn().append("Error processing xml: ").append(e.getMessage()).commit();
        return true;
    }
}