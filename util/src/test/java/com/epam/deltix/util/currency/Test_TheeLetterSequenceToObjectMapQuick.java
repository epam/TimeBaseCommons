package com.epam.deltix.util.currency;

import com.epam.deltix.util.collections.ElementsEnumeration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class Test_TheeLetterSequenceToObjectMapQuick {

    @Test
    public void simple () {
        assertEquals("USD", CurrencyCodeList.numericToSymbolic(840));
        assertNull(CurrencyCodeList.numericToSymbolic(0));


        assertEquals(999, CurrencyCodeList.symbolicToNumeric("ZZZ", 999));
        assertEquals(999, CurrencyCodeList.symbolicToNumeric("[ZZZ]", 1, 4, (short)999));

        assertEquals(840, CurrencyCodeList.symbolicToNumeric("USD", 999));
        assertEquals(840, CurrencyCodeList.symbolicToNumeric("USD", 0,3, (short)999));
        assertEquals(840, CurrencyCodeList.symbolicToNumeric("[USD]", 1,4, (short)999));
        assertEquals(840, CurrencyCodeList.symbolicToNumeric("USD]", 0,3, (short)999));
        assertEquals(840, CurrencyCodeList.symbolicToNumeric("[USD", 1,4, (short)999));
        assertEquals(999, CurrencyCodeList.symbolicToNumeric("[USD]", 0,5, (short)999));


    }


    @Test
    public void testResetBefore() {
        ThreeLetterToObjectMapQuick<String> map = new ThreeLetterToObjectMapQuick<> ();
        map.put("KEY1", "VALUE1");
        ElementsEnumeration<String> e = map.keys();

        assertEquals ("KEY1", dump(e));
        e.reset();
        assertEquals ("KEY1", dump(e));
    }

    @Test
    public void testResetAfter() {
        ThreeLetterToObjectMapQuick<String> map = new ThreeLetterToObjectMapQuick<> ();

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
