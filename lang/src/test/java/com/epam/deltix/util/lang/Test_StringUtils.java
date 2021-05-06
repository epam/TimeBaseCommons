package com.epam.deltix.util.lang;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Andy
 *         Date: 12/17/13
 */
public class Test_StringUtils {

    @Test
    public void testCharSequenceIndexOf() {

        Assert.assertEquals(-1, StringUtils.indexOf("ABCDE", "F"));
        Assert.assertEquals(-1, StringUtils.indexOf("ABCDE", "ABCDEF"));
        Assert.assertEquals(-1, StringUtils.indexOf("ABCDE", "ABDE"));

        Assert.assertEquals(0, StringUtils.indexOf("ABCDE", "A"));
        Assert.assertEquals(0, StringUtils.indexOf("ABCDE", "AB"));
        Assert.assertEquals(0, StringUtils.indexOf("ABCDE", "ABC"));
        Assert.assertEquals(0, StringUtils.indexOf("ABCDE", "ABCD"));
        Assert.assertEquals(0, StringUtils.indexOf("ABCDE", "ABCDE"));

        Assert.assertEquals(1, StringUtils.indexOf("ABCDE", "B"));
        Assert.assertEquals(1, StringUtils.indexOf("ABCDE", "BC"));
        Assert.assertEquals(1, StringUtils.indexOf("ABCDE", "BCD"));
        Assert.assertEquals(1, StringUtils.indexOf("ABCDE", "BCDE"));
        Assert.assertEquals(-1, StringUtils.indexOf("ABCDE", "BCDEF"));

        try {
            Assert.assertEquals(-1, StringUtils.indexOf("ABCDE", ""));
            Assert.fail ("Failed to detect empty string");
        } catch (StringIndexOutOfBoundsException expected) {
            //
        }
    }

    @Test
    public void testLike() {
        LikeTest[] smokeTests = {
                new LikeTest(null, "123", false),
                new LikeTest("123", null, false),
                new LikeTest(null, null, false),
                new LikeTest("", "", true),
                new LikeTest("", "%", true),
                new LikeTest("", "_", false),
                new LikeTest("%", "%", true),
                new LikeTest("_", "_", true),
                new LikeTest("%", "", false),
                new LikeTest("_", "", false),
                new LikeTest("TTTT", "", false),
                new LikeTest("", "TTTT", false),
                new LikeTest("", "%%%%", true),
                new LikeTest("%%%%", "", false),
                new LikeTest("", "____", false),
                new LikeTest("____", "", false),
                new LikeTest("T", "T", true),
                new LikeTest("TEST", "TEST", true),
                new LikeTest("   ", "   ", true),
                new LikeTest("   ", "      ", false),
                new LikeTest("TEST", "TSET", false),
                new LikeTest("TEST", "TEST ", false),
                new LikeTest("TEST ", "TEST", false),
                new LikeTest("T", "TT", false),
                new LikeTest("TT", "T", false),
                new LikeTest("TTT", "_", false)
        };

        LikeTest[] wildcardTests = {
                //'%' tests
                new LikeTest("TEST", "%", true),
                new LikeTest("TEST", "TEST%", true),
                new LikeTest("TEST", "%TEST", true),
                new LikeTest("TEST123", "TEST%", true),
                new LikeTest("456TEST", "%TEST", true),
                new LikeTest("TEST123", "TEST%%", true),
                new LikeTest("456TEST", "%%TEST", true),
                new LikeTest("TEST123", "TEST%%%%%", true),
                new LikeTest("TESTTESTTEST", "T%T%T%T", true),
                new LikeTest("456TEST", "%%%%%%%%TEST", true),
                new LikeTest("TESTTESTTESTTEST", "%T%%%E%S%%%%%%T%%%%%%%%", true),
                new LikeTest("%T%%%E%S%%%%%%T%%%%%%%%", "%T%%%E%S%%%%%%T%%%%%%%%", true),
                new LikeTest("TEST%", "TEST123", false),

                //'_' tests
                new LikeTest("1", "_", true),
                new LikeTest("TEST", "____", true),
                new LikeTest("TEST1", "TEST_", true),
                new LikeTest("2TEST", "_TEST", true),
                new LikeTest("TEST123", "TEST___", true),
                new LikeTest("456TEST", "___TEST", true),
                new LikeTest("1TE2ST345", "_TE_ST___", true),
                new LikeTest("_TE_ST___", "_TE_ST___", true),
                new LikeTest("_TEST", "2TEST", false),

                //combined
                new LikeTest("TEST123TEST", "TEST%TES_", true),
                new LikeTest("3E33TEST123TEST", "_E%TEST___TES%", true),
                new LikeTest("TEST123TEST5678T234DDT2333", "TEST___T%T____T%T____", true),
                new LikeTest("BAAABBBAAABAAA", "%AAA_AAA%", true),
                new LikeTest("BAAABBBAAABAAA", "%AAA__AAA%", false),
                new LikeTest("TEST123TOST", "TEST%TES_", false),
                new LikeTest("3E33TEST123TOST", "_E%TEST___TES%", false),
                new LikeTest("HBSC-ac3r3bn/12a+", "HBSC-%", true),
                new LikeTest("LBSC-ac3r3bn/12a+", "HBSC-%", false),
                new LikeTest("oqiwejfpo1h31h3p1hmonkey3p9r1h43p1h34pf1h3p4i1po34rj21po34ij", "%__monkey__%", true)
        };

        LikeTest[] caseInsensitiveTests = {
                //do we need case insensitive?
                new LikeTest("TEST", "Test", true),
                new LikeTest("Test", "TEST", true),
                new LikeTest("TeStEr", "TeStEr", true)
        };

        LikeTest[] caseSensitiveTests = {
                //do we need case insensitive?
                new LikeTest("TEST", "Test", false),
                new LikeTest("Test", "TEST", false),
                new LikeTest("TeStEr", "TeStEr", true)
        };

        LikeTest[] quotationTests = {
                new LikeTest("TEST!", "TEST\\_", false),
                new LikeTest("TEST_", "TEST\\_", true),
                new LikeTest("!TEST", "\\_TEST", false),
                new LikeTest("_TEST", "\\_TEST", true),
                new LikeTest("_TE!S_T", "\\_TE_S\\_T", true),
                new LikeTest("TEST!!!", "TEST\\%", false),
                new LikeTest("TEST%%%", "TEST\\%\\%\\%", true),
                new LikeTest("TEST%%", "TEST\\%\\%\\%", false),
                new LikeTest("!!!TEST", "\\%TEST", false),
                new LikeTest("%%%TEST", "\\%\\%\\%TEST", true),
                new LikeTest("TE123S%T!!!", "TE%S\\%T%", true),
                new LikeTest("TESTTE%STTEST", "T%T%\\%%T%T", true)
        };

        LikeTest[] specialCharsTests = {
                new LikeTest("TEST~!@#$%^&*()-=_+[]{},./|<>? \t\r\n", "TEST~!@#$\\%^&*()-=\\_+[]{},./|<>? \t\r\n", true)
        };

        LikeTest[] backSlashTests = {
                new LikeTest("TE\\ST", "TE\\ST", true),
                new LikeTest("TE\\ST", "TE\\\\ST", false)
        };

        LikeTest[] backSlashTestsForRegex = {
                new LikeTest("TE\\ST", "TE\\ST", false),
                new LikeTest("TE\\ST", "TE\\\\ST", true)
        };

        WildcardMatcher wildcardMatcher = new WildcardMatcher() {
            @Override
            public boolean wildcardMatch(String expression, String pattern) {
                return StringUtils.wildcardMatch(expression, pattern, false);
            }
        };

        WildcardMatcher wildcardMatcherCaseSensitive = new WildcardMatcher() {
            @Override
            public boolean wildcardMatch(String expression, String pattern) {
                return StringUtils.wildcardMatch(expression, pattern, true);
            }
        };

        WildcardMatcher regexWildcardMatcher = new WildcardMatcher() {
            @Override
            public boolean wildcardMatch(String expression, String pattern) {
                return StringUtils.wildcardMatchRegexp(expression, pattern, false);
            }
        };

        testLike(wildcardMatcher, smokeTests);
        testLike(wildcardMatcher, wildcardTests);
        testLike(wildcardMatcher, caseInsensitiveTests);
        testLike(wildcardMatcher, quotationTests);
        testLike(wildcardMatcher, specialCharsTests);
        testLike(wildcardMatcher, backSlashTests);

        testLike(wildcardMatcherCaseSensitive, caseSensitiveTests);

        testLike(regexWildcardMatcher, smokeTests);
        testLike(regexWildcardMatcher, wildcardTests);
        testLike(regexWildcardMatcher, caseInsensitiveTests);
        testLike(regexWildcardMatcher, quotationTests);
        testLike(regexWildcardMatcher, specialCharsTests);
        testLike(regexWildcardMatcher, backSlashTestsForRegex);
    }

    private void testLike(WildcardMatcher wildcardMatcher, LikeTest[] tests) {
        for (LikeTest test : tests) {
            boolean actual = wildcardMatcher.wildcardMatch(test.expression, test.pattern);
            Assert.assertEquals(
                    test.expression + " like " + test.pattern +
                            ". Actual: " + actual +
                            ". Expected: " + test.result,
                    test.result,
                    actual);
        }
    }

    private interface WildcardMatcher {
        boolean wildcardMatch(String expression, String pattern);
    }

    private class LikeTest {
        String expression;
        String pattern;
        boolean result;

        private LikeTest(String expression, String pattern, boolean result) {
            this.expression = expression;
            this.pattern = pattern;
            this.result = result;
        }
    }

}
