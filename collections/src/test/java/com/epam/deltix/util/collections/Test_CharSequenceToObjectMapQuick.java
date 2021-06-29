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