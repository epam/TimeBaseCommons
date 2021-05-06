package com.epam.deltix.util.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Properties;

/**
 * Converts a java.util.Properties object to a String in the XML file.
 */
public class PropertiesAdapter extends XmlAdapter<String, Properties> {
    public Properties unmarshal(String s) throws Exception {
        Properties properties = new Properties();
        ByteArrayInputStream in = new ByteArrayInputStream(s.getBytes());
        properties.load(in);
        return properties;
    }

    public String marshal(Properties properties) throws Exception {
        if (properties == null) return null;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        properties.store(out, null);

        return new String(out.toByteArray());
    }
}
