package com.epam.deltix.util.xml;

import com.epam.deltix.util.lang.Util;
import java.util.*;
import java.io.*;
import javax.xml.bind.*;
import javax.xml.bind.annotation.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
            Util.logException("Failed to init JAXB for HTML", x);
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
                Util.logException("Failed to marshal HTML", x);
            }
        }
        
        return (swr.toString ());
    }

    public String               getContent () {
        if (mContent == null)
            return ("");

        StringWriter                swr = new StringWriter ();
        StreamResult                result = new StreamResult(swr);
        TransformerFactory          transfac = TransformerFactory.newInstance ();

        try {
            Transformer                 trans = transfac.newTransformer ();

            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            for (Element e : mContent) {
                DOMSource   source = new DOMSource(e);
                trans.transform(source, result);
            }
        } catch (TransformerException x) {
            throw new RuntimeException (x);
        }

        return (swr.toString ());
    }
}
