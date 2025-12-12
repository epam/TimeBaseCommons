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

package com.epam.deltix.util.parserts.synthetic;

import com.epam.deltix.util.lang.Util;
import com.epam.deltix.util.parsers.synthetic.SyntheticInstrumentParser;
import com.epam.deltix.util.parsers.synthetic.SyntheticInstrumentRule;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class Test_SyntheticInstrumentRuleParser {

    @Test
    public void testSimple () {
        assertRule("A+B", "A+B");
        assertRule("AA+BB", "AA+BB");
        assertRule("AAA+BBB", "AAA+BBB");
        assertRule("GEU3+GEZ3+GEH4+GEM4+GEU4+GEZ4+GEH5+GEM5+GEU5+GEZ5+GEH6+GEM6", "GEU3+GEZ3+GEH4+GEM4+GEU4+GEZ4+GEH5+GEM5+GEU5+GEZ5+GEH6+GEM6"); //CME
        assertRule("GEU3-2*GEZ3+GEH4", "GEU3-2*GEZ3+GEH4");

        assertRule("1*2BBH1-1*2BBM1", "1*2BBH1-1*2BBM1");    // ICE
        assertRule("1*2BBH1-1*2BBM1", "1*\"2BBH1\"-1*\"2BBM1\"");    // ICE
    }

    @Test
    public void testLeadingSign () {
        assertRule("A+B",  "+A+B");
        assertRule("-A+B", "-A+B");

        assertRule("-A+2*B", "-A+2*B");
        assertBadRule("-A", "-A");
    }

    @Test
    public void testLeadingZeros () {
        assertRule("A+B",  "+000001*A+0001*B");
        assertRule("-A-B", "-000001*A-0001*B");
    }

    @Test
    public void testSign () {
        assertRule("A-B",  "A-B");
        assertRule("A-B-C",  "A-B-C");
        assertRule("A-B+C",  "A-B+C");
        assertRule("-A-B",  "-A-B");
    }


    @Test
    public void testSymbolsStartWithDigit () {
        assertRule("1*1A+B",  "1A+1*B");
        assertRule("A+1*1B",  "1*A+1B");

        assertRule("1*1A+B",  "1*1A+1*B");
        assertRule("A+1*1B",  "1*A+1*1B");

        assertRule("1*1A+B",  "1*\"1A\"+1*B");
        assertRule("A+1*1B",  "1*A+1*\"1B\"");
    }

    @Test
    public void testMissingStuff () {
        assertBadRule("Rule cannot be empty",  null);
        assertBadRule("Rule cannot be empty",  "");
        assertBadRule("Unexpected end of rule: symbol is missing after sign",  "-");
        assertBadRule("Unexpected end of rule: symbol is missing after sign",  "+");
        assertBadRule("Rule must specify at least two legs",  "A");
        assertBadRule("Unexpected end of rule: symbol is missing",  "0");
        assertBadRule("Unexpected end of rule: symbol is missing",  "1");
    }

    @Test
    public void testMultipleSigns () {
        assertRule("A+B",  "++A+B");
        assertRule("A+B",  "A++B");

        assertRule("A+B",  "--A+B");
        assertRule("A+B",  "A--B");

        assertRule("A+B", "--A+B");
        assertRule("A+B", "A--B");
        assertRule("A-B", "A---B");
        assertRule("A+B",  "--A+B");
        assertRule("-A+B",  "---A+B");
        assertRule("A-B", " A + -B ");
        assertRule("-A+2*B-C",   "-A+2*B+-C");
    }

    @Test
    public void testTerminationWithSign () {
        assertBadRule("Unexpected end of rule: symbol is missing after sign",  "A+B+");
        assertBadRule("Unexpected end of rule: symbol is missing after sign",  "A+B-");
    }

    @Test
    public void testSeparatorAfterSymbol() {
        assertBadRule("Unexpected character at position 4 (Leg symbol cannot contain '*')",  "2*3*A+1B");

        assertBadRule("Unexpected character at position 2 (Leg symbol cannot contain '*')",  "A*1+B");
        assertBadRule("Unexpected character at position 4 (Leg symbol cannot contain '*')",  "A+B*1");
        assertBadRule("Unexpected character at position 4 (Leg symbol cannot contain '*')",  "1*A*1+B");
        assertBadRule("Unexpected character at position 6 (Leg symbol cannot contain '*')",  "A+1*B*1");

        //assertRule("A+B", "A*1+B"); duplicate test above
        //assertRule("A+B", "A+B*1"); duplicate test above
    }

    @Test
    public void testTerminationWithRatio () {
        assertBadRule("Unexpected end of rule: symbol is missing",  "A+1");
        assertBadRule("Unexpected end of rule: symbol is missing",  "A-1");
        assertBadRule("Unexpected end of rule: symbol is missing",  "A+0");
        assertBadRule("Unexpected end of rule: symbol is missing",  "A-0");
    }

    @Test
    public void testZeroRatio () {
        // Risk Rules rely on the fact that leg ratios must not be zero
        assertBadRule("Unexpected character at position 2 (Ratio cannot be zero)",  "0*A+B");
        assertBadRule("Unexpected character at position 3 (Ratio cannot be zero)",  "+0*A+B");
        assertBadRule("Unexpected character at position 3 (Ratio cannot be zero)",  "-0*A+B");
        assertBadRule("Unexpected character at position 4 (Ratio cannot be zero)",  "A+0*B");
        assertBadRule("Unexpected character at position 10 (Ratio cannot be zero)",  "A+0000000*B");
        assertBadRule("Unexpected character at position 10 (Ratio cannot be zero)",  "A-0000000*B");
    }

    @Test
    public void testDuplicateSymbol () {
        // UHF Risk rules rely on the fact that there are no duplicates among legs
        assertBadRule("Duplicate leg symbol: \"A\"",  "A-A");
        assertBadRule("Duplicate leg symbol: \"A\"",  "A+B-A");
    }

    @Test
    public void testOtherQQLSyntax() {
        assertBadRule ("NGV3+NGV3", "'NGV3'+'NGV3'");
        assertBadRule ("1NGV+2NGV", "'1NGV'+'2NGV'");
        assertBadRule ("NGV3=NGX3", "NGV3=NGX3");
        assertBadRule ("A+NGV3=NGX3+C", "A + (NGV3 = NGX3) + C");
        assertBadRule ("A+NGV3=NGX3+C", "A + NGV3 = NGX3 + C");
        assertRule ("this+is+built-in", "this + is + built-in");
        assertRule ("this+is+built-in", "this + \"is\" + built-\"in\"");
    }

    @Test
    public void testArithmeticFunctions() {
        assertBadRule ("NGV3/NGX3", "NGV3 / NGX3");
        assertBadRule ("NGV3*NGX3", "NGV3 * NGX3");
        assertBadRule ("NGV3*2*NGX3", "NGV3 * 2 * NGX3");
        assertBadRule ("12*NGV3-NGX3", "2*6*NGV3-NGX3");
    }

    @Test
    public void testFractionalCoeffs() {
        assertRule ("0.7*A-B", "0.7*A-B");
        assertRule ("A-1.5*B", "A-1.5*B");
    }

    @Test
    public void testDoubleQuotes() {
        assertRule("2*A+1*1B","2*A+1B");
        assertRule ("1*2A+B", "\"2A\"+B");
        assertRule ("BRNFMH0015!-BRNFMH0014!", "\"BRNFMH0015!\"-\"BRNFMH0014!\"");
    }

    @Test
    public void testParentheses() {
        assertBadRule ("A+B-C-D", "(A+B)- (C+D)");
        assertBadRule ("A+B-2*C-2*D", "A+B-2*(C+D))");
        assertBadRule("A+3*B+3*C", "A + 3 * (B + C)");
    }

    //@Test
    //public void testUpperCase() {
    //    assertRule ("Q1+Q2", "q1+q2");
    //    assertRule ("q1+Q2", "\"q1\"+q2");
    //}

    @Test
    public void testSpaces () {
        SyntheticInstrumentRule actualRule = SyntheticInstrumentParser.createParserAndParse("\"BNX FMM0017\"-\"BNX FMZ0019\"");
        Assert.assertEquals (2, actualRule.symbols.length);
        Assert.assertEquals ("BNX FMM0017", actualRule.symbols[0]);
        Assert.assertEquals ("BNX FMZ0019", actualRule.symbols[1]);

        assertBadRule ("<correct me>", "'BNX FMM0017'-'BNX FMZ0019'");

        assertRule ("A+B+2*C", " A + B + 2 * C ");
        assertRule ("A+B+2*C", "    A   +   B   +   2   *   C");
        assertRule ("AB CD+EF GH", "\"AB CD\"+\"EF GH\"");
        assertRule("GEU3+GEZ3+GEH4+GEM4+GEU4+GEZ4+GEH5+GEM5+GEU5+GEZ5+GEH6+GEM6",
                "   GEU3 + GEZ3+GEH4+GEM4+GEU4+GEZ4      + GEH5+GEM5+GEU5+               GEZ5+GEH6+     GEM6    ");
    }

    private void assertRule(String expectedResult, String inputTextRule) {
        try {
            SyntheticInstrumentRule actualRule = SyntheticInstrumentParser.createParserAndParse(inputTextRule);
            Assert.assertEquals(expectedResult, actualRule.toString());
        } catch (IllegalArgumentException e) {
            Assert.fail(String.format("Parser was supposed to successfully finish the work on rule \"%s but it didn't\n%s", inputTextRule, e.getMessage()));
        }
    }

    private void assertBadRule (String expectedError, String inputTextRule) {
        try {
            SyntheticInstrumentRule actualRule = SyntheticInstrumentParser.createParserAndParse(inputTextRule);
            Assert.fail(String.format("Parser was supposed to fail on rule \"%s\" but it didn't. Instead it produced: %s",
                    inputTextRule,
                    actualRule.toString()));

        } catch (IllegalArgumentException e) {
            if (!Util.QUIET)
                System.out.println(String.format("Rule '%s' is not valid \n%s", inputTextRule, e.getMessage()));
        }
    }
}
