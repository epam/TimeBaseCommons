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
package com.epam.deltix.util.io;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Andy
 *         Date: 10/17/12 9:27 PM
 */
public class Test_TokenReplacingReader {

    private final static TokenReplacingReader.ITokenResolver NULL_RESOLVER = token -> null;

    @Test
    public void testPartialRead () throws Exception {
        String text =
            "<jplugin minversion=\"1\">\n" +
            "  <getfrom svn=\"https://gw.deltixlab.com:444/svn/src/internal/plugins/trade/quickfix\">\n" +
            "    <src folder=\"src\"/>";

        Reader reader = new TokenReplacingReader(new StringReader(text), NULL_RESOLVER);
        char[] buffer = new char[512];
        int bytesRead = reader.read(buffer, 0, 64);
        bytesRead+= reader.read(buffer, bytesRead, buffer.length - bytesRead);

        String result = new String(buffer, 0, bytesRead);
        assertEquals(text, result);
    }

    @Test
    public void testSimpleReplacement() throws Exception {
        assertReplacementEquals("value", "${key}", "key=value");
        assertReplacementEquals("value1,value2", "${key1},${key2}", "key1=value1;key2=value2");
        assertReplacementEquals("{key1},{key2}", "{key1},{key2}", "key1=value1;key2=value2"); // missing dollar sign
    }

    @Test
    public void testStrangeCase1() throws Exception {
        assertReplacementEquals(
            "ExecutionServer.dllSearchPath=C:\\projects\\4.4\\QuantServer\\..\\QuantOffice\\Bin",
            "ExecutionServer.dllSearchPath=${deltix_home}\\..\\QuantOffice\\Bin",
            "deltix_home=C:\\projects\\4.4\\QuantServer");
    }




    private static void assertReplacementEquals(String expectedResult, String text, String dictionary) throws IOException, InterruptedException {
        MapBasedTokenReplacer replacer = new MapBasedTokenReplacer (dictionary);
        String actualResult = replace(text, replacer);
        assertEquals(expectedResult, actualResult);
    }

    private static String replace (String text, TokenReplacingReader.ITokenResolver resolver) throws IOException, InterruptedException {
        Reader reader = new TokenReplacingReader(new StringReader(text), resolver);
        return IOUtil.readFromReader(reader);
    }

    //${deltix_home}\\..\\QuantOffice\\Bin

    static class MapBasedTokenReplacer implements TokenReplacingReader.ITokenResolver {

        private final Map<String,String> map = new HashMap<>();

        MapBasedTokenReplacer (String dictionary) {
            for (String nameValuePair : dictionary.split(";")) {
                String [] nameValue = nameValuePair.split("=");
                map.put(nameValue[0], nameValue[1]);
            }
        }

        @Override
        public String resolveToken(String token) {
            return map.get(token);
        }
    }

}