package com.epam.deltix.util.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapAdaptor<K, V> extends
        XmlAdapter<MapAdaptor.MapEntryList<K, V>, HashMap<K, V>> {

    @Override
    public HashMap<K, V> unmarshal(MapEntryList<K, V> v) throws Exception {
        HashMap<K, V> map = new HashMap<K, V>();
        if (v.entries != null) {
            for (MapEntry<K, V> entry : v.entries) {
                map.put(entry.key, entry.value);
            }
        }

        return map;
    }

    @Override
    public MapEntryList<K, V> marshal(HashMap<K, V> map) throws Exception {
        
        MapEntryList<K, V> list = new MapEntryList<K, V>(new ArrayList<MapEntry<K, V>>());
        for (Map.Entry<K, V> entry : map.entrySet())
            list.entries.add(new MapEntry<K, V>(entry.getKey(), entry.getValue()));

        return list;
    }

    public static class MapEntryList<K, V> {

        protected MapEntryList() { } // for jaxb

        public MapEntryList(ArrayList<MapEntry<K, V>> entries) {
            this.entries = entries;
        }

        @XmlElements(@XmlElement(type=MapEntry.class))
        public ArrayList<MapEntry<K, V>> entries;
    }

    public static class MapEntry<K, V> {

        protected MapEntry() { } // for jaxb

        public MapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @XmlElement
        public K key;

        @XmlElement
        public V value;
    }
}


