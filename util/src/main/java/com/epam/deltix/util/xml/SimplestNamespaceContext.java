package com.epam.deltix.util.xml;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;


/** 
 * Trivial implementation of NamespaceContext that supports the following XML namespaces:
 * <ul>
 * <li>xmlns:xml=http://www.w3.org/XML/1998/namespace
 * <li>xmlns:xmlnl=http://www.w3.org/2000/xmlns/
 * <li>xmlns:xsi=http://www.w3.org/2001/XMLSchema-instance
 * </ul>
 */
public class SimplestNamespaceContext implements NamespaceContext {

	private static final String XSI_PREFIX = "xsi";


    public String   getNamespaceURI (String prefix) {
    	if (prefix == null)
            throw new IllegalArgumentException ("null prefix");

    	if (prefix.equals(XMLConstants.XML_NS_PREFIX)) // "xml"
            return XMLConstants.XML_NS_URI;
        
        if (prefix.equals(XMLConstants.XMLNS_ATTRIBUTE)) // "xmlns"
            return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;

        if (prefix.equals(XSI_PREFIX)) // "xsi"
            return XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI;

        return XMLConstants.NULL_NS_URI;
    }

    public String getPrefix(String namespaceURI) {
        if (namespaceURI == null)
            throw new IllegalArgumentException ("null uri");
        
        if (namespaceURI.equals (XMLConstants.XML_NS_URI)) // xml
            return XMLConstants.XML_NS_PREFIX;
        
        if (namespaceURI.equals (XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) // xmlns
            return XMLConstants.XMLNS_ATTRIBUTE;

        if (namespaceURI.equals (XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI)) // xmlns
            return XSI_PREFIX;

        return null;
    }

    
    public Iterator<String> getPrefixes(String namespaceURI) {
        if (namespaceURI == null)
           throw new IllegalArgumentException ("null uri");

        List<String> result = new LinkedList<String> ();
        String prefix = null;
        if (namespaceURI.equals (XMLConstants.XML_NS_URI)) // xml
        	prefix = XMLConstants.XML_NS_PREFIX;
        else
        if (namespaceURI.equals (XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) // xmlns
            prefix = XMLConstants.XMLNS_ATTRIBUTE;
        else
        if (namespaceURI.equals (XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI)) // xmlns
        	prefix = XSI_PREFIX;
        
        if (prefix != null)
            result.add (prefix);
        
        return result.iterator();
    }    




}

