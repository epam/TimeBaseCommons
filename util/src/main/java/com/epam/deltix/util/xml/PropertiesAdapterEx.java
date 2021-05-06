package com.epam.deltix.util.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PropertiesAdapterEx extends XmlAdapter<PropertiesAdapterEx.PropertyList, Properties> {
    @Override
    public Properties unmarshal(PropertyList v) throws Exception {
        Properties result = new Properties();
        for (int i = 0; i < v.property.size(); i++) {
            PropertyEntry prop = v.property.get(i);
            result.setProperty(prop.key, prop.value);
        }
        return result;
    }

    @Override
    public PropertyList marshal(Properties v) throws Exception {
        PropertyList propList = new PropertyList(v.size());
        String[] keys = v.stringPropertyNames().toArray(new String[v.size()]);
        Arrays.sort(keys);
        for (String key : keys) {
            String value = v.getProperty(key);
            if (value != null && value.length() > 0)
                propList.property.add(new PropertyEntry(key, value));
        }
        return propList;
    }

    //////////////////////// HELPER CLASSES ///////////////////////

    @XmlType
    public static final class PropertyList {
        public PropertyList() {
            this(5);
        }

        public PropertyList(int capacity) {
            property = new ArrayList<PropertyEntry>(capacity);
        }

        @XmlElement
        List<PropertyEntry> property;
    }

    @XmlType
    public static final class PropertyEntry {
        @XmlAttribute
        String key;

        @XmlValue
        String value;

        public PropertyEntry() {
        }

        public PropertyEntry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
