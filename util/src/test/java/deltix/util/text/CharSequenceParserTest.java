package deltix.util.text;


import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.CharBuffer;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CharSequenceParserTest {

    @Test
    public void testParseDoubleSpecialCase() {
        assertEquals(1233451.3124, CharSequenceParser.parseDouble("123,345,1.3124"));
        assertEquals(123.34513124, CharSequenceParser.parseDouble("123,345,1.3124e-4"));
        assertEquals(-0.0, CharSequenceParser.parseDouble("-4.9E-324"));
        assertEquals(0.0, CharSequenceParser.parseDouble("4.9E-324"));
        assertEquals(Double.POSITIVE_INFINITY, CharSequenceParser.parseDouble("Inf"));
        assertEquals(Double.NEGATIVE_INFINITY, CharSequenceParser.parseDouble("-Inf"));

        //??
        assertEquals(1234.5, CharSequenceParser.parseDouble("1,2,3,4.5"));
        assertEquals(123, CharSequenceParser.parseDouble(",123"));
        assertEquals(123, CharSequenceParser.parseDouble("123,"));
        assertEquals(1234, CharSequenceParser.parseDouble("1,,234"));
        assertEquals(1234, CharSequenceParser.parseDouble("1,,234"));
        assertEquals(1234, CharSequenceParser.parseDouble("1,,234"));

        assertEquals(0.0, CharSequenceParser.parseDouble("E10"));
        assertEquals(0.0, CharSequenceParser.parseDouble("e-10"));
        //??
        assertThrows(NumberFormatException.class, () -> CharSequenceParser.parseDouble("1e-2147483648"));
//        "0.0000000000000000000000000000000001234567"
//        "0.123456789012345678901234567890123456789012345678901234567890"
    }

    @ParameterizedTest(name = "parseDouble({0})")
    @MethodSource("source")
    void testParseDouble(String input) {
        double actual = CharSequenceParser.parseDouble(input);
        double expected = Double.parseDouble(input);
        if (Double.isInfinite(expected)) {
            assertTrue(Double.isInfinite(actual));
        } else if (Double.isNaN(expected)) {
            assertTrue(Double.isNaN(actual));
        } else {
            assertEquals(expected, actual, Math.abs(expected) * 1E-15);
        }
    }

    @ParameterizedTest(name = "testParseDoubleSubStr({0})")
    @MethodSource("source")
    void testParseDoubleSubStr(String input) {
        String input2 = "str" + input + "end1";
        double expected = Double.parseDouble(input);
        double actual = CharSequenceParser.parseDouble(input2, 3, 3 + input.length());
        if (Double.isInfinite(expected)) {
            assertTrue(Double.isInfinite(actual));
        } else if (Double.isNaN(expected)) {
            assertTrue(Double.isNaN(actual));
        } else {
            assertEquals(expected, actual, Math.abs(expected) * 1E-15);
        }
    }


    @ParameterizedTest(name = "parseFloat({0})")
    @MethodSource("sourceFloat")
    void testParseFloat(String input) {
        float expected = Float.parseFloat(input);
        float actual = CharSequenceParser.parseFloat(input);
        if (Float.isInfinite(expected)) {
            assertTrue(Float.isInfinite(actual));
        } else if (Float.isNaN(expected)) {
            assertTrue(Float.isNaN(actual));
        } else {
            assertEquals(expected, actual, Math.abs(expected) * 1e-6f);
        }
    }

    static Stream<Arguments> source() {
        return Stream.of(
                Arguments.of("0.0e10"),
                Arguments.of("0e-10"),
                Arguments.of("0.0"),
                Arguments.of("0.0000"),
                Arguments.of("-0.0"),
                Arguments.of("-0"),
                Arguments.of("1"),
                Arguments.of("-1"),
                Arguments.of("1.12345678901234567"),
                Arguments.of("-1.12345678901234567"),
                Arguments.of("1.7976931348623157E308"),
                Arguments.of("-1.7976931348623157E308"),
                Arguments.of("2E308"),
                Arguments.of("-2E308"),
                Arguments.of("Infinity"),
                Arguments.of("-Infinity"),
                Arguments.of("1E-400"),
                Arguments.of("-1E-400"),
                Arguments.of("NaN"),
                Arguments.of("+NaN"),
                Arguments.of("-NaN"),
                Arguments.of("3.1415E10"),
                Arguments.of("3.0e10"),
                Arguments.of("3.0E66"),
                Arguments.of("3.1415E38"),
                Arguments.of("3.1415E-38"),
                Arguments.of("1234567890987654321.1234567890987654321"),
                Arguments.of("1e309"),
                Arguments.of("1e-325"),
                Arguments.of("1e2147483647"),
                Arguments.of("9007199254740993"),
                Arguments.of("1.123456"),
                Arguments.of("-1.123456"),
                Arguments.of("3.1415E-10"),
                Arguments.of("3.4028235e+38"),
                Arguments.of("-3.4028235e+38"),
                Arguments.of("3.1415E39"),
                Arguments.of("-3.1415E39"),
                Arguments.of("0.00000000000000001234567"),
                Arguments.of("123456700000000000000000"),
                Arguments.of("0.00000000000000000000000000000000000000000000000000001"),
                Arguments.of(String.valueOf(Float.MAX_VALUE)),
                Arguments.of(String.valueOf(Float.MIN_VALUE)),
                Arguments.of(".123"),
                Arguments.of("123."),
                Arguments.of(".123e10"),
                Arguments.of("-.123"),
                Arguments.of("+.123")
        );
    }

    @ParameterizedTest(name = "testParseInvDoubleSunStr({0})")
    @MethodSource("invalidSource")
    void testParseInvDoubleSunStr(String input) {
        assertThrows(NumberFormatException.class, () -> CharSequenceParser.parseDouble(input));
    }

    @ParameterizedTest(name = "testParseFloatFail({0})")
    @MethodSource("invalidSource")
    void testParseDoubleFail(String input) {
        assertThrows(NumberFormatException.class, () -> CharSequenceParser.parseFloat(input));
    }

    static Stream<Arguments> invalidSource() {
        return Stream.of(
                Arguments.of("1Infinity"),
                Arguments.of("Infinity1"),
                Arguments.of("123Infinity312"),
                Arguments.of("123InfinityE312"),
                Arguments.of("-Infinity1"),
                Arguments.of("-1Infinity"),
                Arguments.of("-1E-400Inf"),
                Arguments.of("123NaN"),
                Arguments.of("NaN123"),
                Arguments.of(""),
                Arguments.of(" "),
                Arguments.of("1e"),
                Arguments.of("1E"),
                Arguments.of("1e+"),
                Arguments.of("1e-"),
                Arguments.of("++1"),
                Arguments.of("--1"),
                Arguments.of("+-1"),
                Arguments.of("-+1"),
                Arguments.of("1.2.3"),
                Arguments.of("1.2e3.4"),
                Arguments.of("1.2e3e4"),
                Arguments.of("NaNaNa"),
                Arguments.of("Infinit"),
                Arguments.of("infinity"),
                Arguments.of("nan"),
                Arguments.of("1.#INF"),
                Arguments.of("1.#IND")
        );
    }


    static Stream<Arguments> sourceFloat() {
        List<Arguments> collect = source().collect(Collectors.toList());
        collect.add(Arguments.of("0.0000000000000000000000000000000001234567"));
        collect.add(Arguments.of("0.123456789012345678901234567890123456789012345678901234567890"));
        return collect.stream();
    }


    @Test
    public void testDifferentCharSequences() {
        String str = "123.456";
        StringBuilder sb = new StringBuilder(str);
        StringBuffer sbuf = new StringBuffer(str);
        CharBuffer cb = CharBuffer.wrap(str);

        double expected = 123.456;
        assertEquals(expected, CharSequenceParser.parseDouble(str));
        assertEquals(expected, CharSequenceParser.parseDouble(sb));
        assertEquals(expected, CharSequenceParser.parseDouble(sbuf));
        assertEquals(expected, CharSequenceParser.parseDouble(cb));
    }
}