package com.epam.deltix.util.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.TimeZone;

/**
 *
 */
public class TimeZoneAdapter extends XmlAdapter<String, TimeZone> {
    public TimeZone unmarshal(String s) throws Exception {
        return s != null ? TimeZone.getTimeZone(s) : null;
    }

    public String marshal(TimeZone tz) throws Exception {
        return tz != null ? tz.getID() : null;
    }
}
