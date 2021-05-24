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

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Able to read old (text) and new (xml) properties format. Uses new (xml) format on write.
 */
public class PropertiesAdapterCombi extends XmlAdapter<Element, Properties> {
    public static final String NS = "http://xml.deltixlab.com/internal/quantserver/2.0";
    private DocumentBuilder documentBuilder;

    @Override
    public Properties unmarshal(Element v) throws Exception {
        final Properties properties = new Properties();

        final NodeList list = v.getChildNodes();
        final int len = list.getLength();
        for (int i = 0; i < len; i++) {
            final Node node = list.item(i);
            final short type = node.getNodeType();
            if (type == Node.TEXT_NODE) {
                if (len > 1 && !node.getTextContent().trim().isEmpty()) {
                    throw new IllegalStateException("properties node - unexpected text element: " + node.getTextContent());
                }
                final ByteArrayInputStream in = new ByteArrayInputStream(node.getTextContent().getBytes());
                properties.load(in);
            } else if (type == Node.ELEMENT_NODE) {
                Element e = (Element) node;
                //System.out.println(e.getTagName() + " " + e.getTextContent());
                properties.put(e.getTagName(), e.getTextContent());
            } else
                throw new IllegalStateException("unexpected node type: " + type);
        }

        return properties;
    }

    @Override
    public Element marshal(Properties v) throws Exception {
        final Document document = getDocumentBuilder().newDocument();
        final Element element = document.createElementNS(NS, "properties");
        if (!v.isEmpty()) {
            final String[] keys = v.stringPropertyNames().toArray(new String[v.size()]);
            Arrays.sort(keys);
            for (String key : keys) {
                final String value = v.getProperty(key);
                if (value != null && !value.isEmpty()) {
                    final Element child = document.createElement(key);
                    element.appendChild(child);
                    child.appendChild(document.createTextNode(value));
                }
            }
        }

        return element;
    }

    private DocumentBuilder getDocumentBuilder() throws Exception {
        // Lazy load the DocumentBuilder as it is not used for unmarshalling.
        if (null == documentBuilder) {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            documentBuilder = dbf.newDocumentBuilder();
        }
        return documentBuilder;
    }
}