package deltix.util.xml;

import java.util.*;
import java.io.*;
import javax.xml.bind.*;
import javax.xml.bind.annotation.*;
import org.w3c.dom.Element;

/**
 *  Adapts arbitrary XHTML to JAXB.
 */
@XmlRootElement (name = "html", namespace="http://www.w3.org/1999/xhtml")
public class HTML {
    @XmlAnyElement 
    private List <Element>           mContent;

    public HTML () { }      // Make JAXB happy
    
    @Override
    public String               toString () {
        StringWriter    swr = new StringWriter ();
        
        try {
            JAXBContext     ctxt = JAXBContext.newInstance (HTML.class);
            Marshaller      m = ctxt.createMarshaller ();
            m.setProperty (Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            m.setProperty (Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
            m.marshal (this, swr);
        } catch (JAXBException x) {
            x.printStackTrace ();
        }
        return (swr.toString ());
    }
}
