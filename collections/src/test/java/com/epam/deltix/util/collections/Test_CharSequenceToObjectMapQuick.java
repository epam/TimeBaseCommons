package com.epam.deltix.util.collections;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Test_CharSequenceToObjectMapQuick {

    @Test
    public void testResetBefore() {
        CharSequenceToObjectMapQuick<String> map = new CharSequenceToObjectMapQuick<> ();
        map.put("KEY1", "VALUE1");
        ElementsEnumeration<String> e = map.keys();

        assertEquals ("KEY1", dump(e));
        e.reset();
        assertEquals ("KEY1", dump(e));
    }

    @Test
    public void testResetAfter() {
        CharSequenceToObjectMapQuick<String> map = new CharSequenceToObjectMapQuick<> ();

        ElementsEnumeration<String> e = map.keys();
        map.put("KEY1", "VALUE1");
        e.reset();
        assertEquals ("KEY1", dump(e));
    }

    private static String dump(ElementsEnumeration<String> enumeration) {
        StringBuilder result = new StringBuilder();
        while(enumeration.hasMoreElements()) {
            if (result.length() > 0)
                result.append(", ");
            result.append(enumeration.nextElement());
        }
        return result.toString();
    }
}
