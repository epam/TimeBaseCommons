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
package com.epam.deltix.util.text;

import org.junit.Test;
import org.junit.Assert;

import java.text.MessageFormat;


/**
 * @author Andy
 *         Date: Jul 22, 2009 5:19:17 PM
 */
public class Test_SimpleMessageFormat {


    @Test
    public void simple () {
        assertValid ("Foo {0}", "Foo Bar", "Bar");
        assertValid ("{0}{1}", "Foo152", "Foo", 152);
        assertValid ("{0} {1}", "Foo 152", "Foo", 152);
        assertValid (" {0} {1} ", " Foo 152 ", "Foo", 152);
    }

       @Test
    public void weird () {
        assertValid ("", "");
        assertValid (" ", " ");
        assertValid ("}", "}");
        assertValid ("{0}}", "Foo}", "Foo");
        assertValid ("{0}", "Foo", "Foo", 1, 2, 3, 4, 5);

        assertValid ("ab } de", "ab } de");
           


    }

    @Test
    public void testMissingBrace () {
        assertInvalid ("{ Foo", "Format error at position 0: Missing right brace '}'. Format string: \"{ Foo\"", 123);
        assertInvalid ("Foo {", "Format error at position 4: Missing right brace '}'. Format string: \"Foo {\"", 123);
    }

    @Test
    public void testMissingArgument () {
       assertInvalid ("{}", "Format error at position 0: Missing argument number inside braces {}. Format string: \"{}\"", 123);
    }

    @Test
    public void testBadArgument () {
       assertInvalid ("{1}", "Format error at position 0: Formatting string refers to non-existing argument #1 when only 1 arguments are passed. Format string: \"{1}\"", 123);
    }

    @Test
    public void testNonDigitArgument () {
       assertInvalid ("{A}", "Format error at position 1: Argument index contains non-digit character: 'A'. Format string: \"{A}\"", 123);
    }

    @Test
    public void testGoodQuotes () {
       assertValid ("''", "'");
       assertValid (" ''", " '");
       assertValid ("'' ", "' ");
       assertValid ("''''", "''");

       assertValid ("'''{'0}''", "'{0}'");
       assertValid ("'''{0}'''", "'{0}'");

       assertValid ("'{braces}'", "{braces}");
       assertValid ("'{braces}' {0}", "{braces} nobraces", "nobraces");
    }

// Previous behavior:
//    @Test
//    public void testBadQuotes () {
//       assertInvalid ("'",   "Format error at position 0: String contains single quote, which is a special escape character. Use '' to print single quote. Format string: \"'\"", 123);
//       assertInvalid (" '",  "Format error at position 1: String contains single quote, which is a special escape character. Use '' to print single quote. Format string: \" '\"", 123);
//       assertInvalid ("' ",  "Format error at position 0: String contains single quote, which is a special escape character. Use '' to print single quote. Format string: \"' \"", 123);
//       assertInvalid (" ' ", "Format error at position 1: String contains single quote, which is a special escape character. Use '' to print single quote. Format string: \" ' \"", 123);
//       assertInvalid ("'''", "Format error at position 2: String contains single quote, which is a special escape character. Use '' to print single quote. Format string: \"'''\"", 123);
//    }


    @Test
    public void singleQuoteTest() {
        assertValid("Order amount can't be less than 1", "Order amount can't be less than 1");
        assertValid("'", "'");
        assertValid(" '", " '");
        assertValid("' ", "' ");
        assertValid(" ' ", " ' ");

        // now we even allow this:
        assertValid("This can't be true don't you think?", "This cant be true dont you think?");

        assertValid(" '' ", " ' "); // double quote -> first one is escape character
        assertValid("'''", "''");
    }

    @Test
    public void testQuotesAndBraces () {
        assertValid ("arg {0} then 'quotes'", "arg Foo then quotes", "Foo");
        assertValid ("'quotes' then arg {0}", "quotes then arg Foo", "Foo");
    }


    @Test
    public void testManyArguments () {

        assertValid ("{0} {1} {2} {3} {4} {5} {6} {7} {8} {9} {10} {11} {12}",
                "zero one two three four five six seven eight nine ten eleven twelve",
                "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve");
    }


    @Test
    public void testMessageFormatJavadocCases () {
        assertInvalid ("ab {0'}' de", "Format error at position 5: Argument index contains non-digit character: '''. Format string: \"ab {0'}' de\"", 123);
    }



    @Test
    public void performance () {
        int cnt = 100000;

        String msg2 = "String:{0} number:{1}";
        String msg1 = "String:%s number:%.2f";
        Object [] args = { "Hello", 3.14 };

//        System.out.println (String.format(msg1, args));
//        System.out.println (MessageFormat.format(msg2, args));
//        System.out.println (SimpleMessageFormat.format(msg2, args));

        long t1 = System.currentTimeMillis();

        long len1 = 0;
        for (int i=0; i < cnt; i++)
            len1 += String.format(msg1, args).length();


        long t2 = System.currentTimeMillis();

        long len2 = 0;
        for (int i=0; i < cnt; i++)
            len2 += MessageFormat.format(msg2, args).length();

        long t3 = System.currentTimeMillis();

        long len3 = 0;
        for (int i=0; i < cnt; i++)
            len3 += SimpleMessageFormat.format(msg2, args).length();

        long t4 = System.currentTimeMillis();

        System.out.println ("String:       " + (t2-t1) + " result:" + len1);
        System.out.println ("MessageFormat:" + (t3-t2) + " result:" + len2);
        System.out.println ("SimpleMessageFormat:" + (t4-t3) + " result:" + len3);
    }

    private void assertValid(String format, String expected, Object ... args) {
        String actual = SimpleMessageFormat.format (format, args);
        Assert.assertEquals ("Result doesn't match", expected, actual);
    }

    private void assertInvalid(String format, String expectedError, Object ... args) {
        try {
            String actual = SimpleMessageFormat.format (format, args);
            Assert.fail ("Formatting was expected to fail with " + expectedError + " but instead returned " + actual);
        } catch (Exception e) {
            Assert.assertEquals ("Unexpected error message", expectedError, e.getMessage());
        }
    }
}