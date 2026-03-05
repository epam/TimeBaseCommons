/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.util.xml;

import java.io.StringWriter;
import java.io.StringReader;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.bind.Marshaller;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class XmlTools {
	private XmlTools() {}
	
	/** Converts DOM tree to formatted XML text */
    public static String xml2text (Document document, boolean indent) {
	    return xml2text(new DOMSource(document), indent);
	}
    
	/** Converts DOM element to formatted XML text */
    public static String xml2text (Element element, boolean indent) {
        return xml2text(new DOMSource(element), indent);
	}
    
	/** Converts DOM source to formatted XML text */
    public static String xml2text (DOMSource source, boolean indent) {
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			if (indent)
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        
	        StringWriter sw = new StringWriter();
	        transformer.transform(source, new StreamResult(sw));
	
	        
	        return sw.toString();
			
		} catch (TransformerException e) {
			e.printStackTrace();
			throw new RuntimeException (e);
		}
        
    }	
    
	public static String toXML(Marshaller marshaller, Object value) throws JAXBException {
        StringWriter writer = new StringWriter(8096);
        marshaller.marshal(value, writer);
        writer.flush();
        return writer.toString();
    }
    
    public static Object fromXML(Unmarshaller unmarshaller, String xmlValue) throws JAXBException {
        StringReader reader = new StringReader(xmlValue);
        try {
            return unmarshaller.unmarshal(reader);
        } finally {
            reader.close();
        }
    }
}