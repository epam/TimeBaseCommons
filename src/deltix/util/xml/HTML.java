package deltix.util.xml;

import deltix.util.lang.Util;
import java.util.*;
import java.io.*;
import java.util.logging.Level;
import javax.xml.bind.*;
import javax.xml.bind.annotation.*;
import org.w3c.dom.Element;

/**
 *  Adapts arbitrary XHTML to JAXB.
 *  Incorporate as follows:
 *<pre>@XmlElement (name = "html", namespace="http://www.w3.org/1999/xhtml")
 *private HTML fieldName;</pre>
 */
@XmlRootElement (name = "html", namespace="http://www.w3.org/1999/xhtml")
public class HTML {
    public static JAXBContext       context;
    
    static {
        try {
            context = JAXBContext.newInstance (HTML.class);
        } catch (JAXBException x) {
            Util.LOGGER.log (Level.SEVERE, "Failed to init JAXB for HTML", x);
        }
    }
    
    @XmlAnyElement 
    private List <Element>          mContent;

    public HTML () { }      // Make JAXB happy
    
    @Override
    public String               toString () {
        StringWriter    swr = new StringWriter ();
        
        if (context != null) {
            try {
                Marshaller      m = context.createMarshaller ();
                m.setProperty (Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
                m.setProperty (Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
                m.marshal (this, swr);
            } catch (JAXBException x) {
                Util.LOGGER.log (Level.SEVERE, "Failed to marshal HTML", x);
            }
        }
        
        return (swr.toString ());
    }
}
