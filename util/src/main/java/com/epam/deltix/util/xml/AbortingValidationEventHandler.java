package com.epam.deltix.util.xml;

import javax.xml.bind.*;

/**
 *  Causes the Unmarshaller to throw an exception when it runs into
 *  trouble.
 */
public class AbortingValidationEventHandler implements ValidationEventHandler {
    private AbortingValidationEventHandler () {        
    }
    
    public boolean      handleEvent (ValidationEvent event) {
        return (false);
    }
    
    public static final ValidationEventHandler  INSTANCE =
        new AbortingValidationEventHandler ();
}
