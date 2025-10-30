package deltix.util.text;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CharSequenceParserTest {

    @Disabled("issues#19")
    @Test
    void testParseFloat() {
        String val = "123456789098765";
        float actual = CharSequenceParser.parseFloat(val);
        float expected = Float.parseFloat(val);
        assertEquals(expected, actual, Math.abs(expected) * 1e-6f);

        assertEquals(expected, CharSequenceParser.parseFloat(String.valueOf(expected)), Math.abs(expected) * 1e-6f);
        String val2 = "9.9999E26";
        float expected2 = Float.parseFloat(val2);
        assertEquals(expected2, CharSequenceParser.parseFloat(val2), Math.abs(expected2) * 1e-6f);
    }

    @Disabled("issues#19")
    @ParameterizedTest(name = "parseFloat({0})")
    @MethodSource("sourceFloat")
    void testParseFloat(String input) {
        float expected = Float.parseFloat(input);
        float actual = CharSequenceParser.parseFloat(input);
//        assertEquals(expected, actual);
        assertEquals(expected, actual, Math.abs(expected) * 1e-6f);
    }

    @Disabled("issues#19")
    @ParameterizedTest(name = "parseDouble({0} -> {1})")
    @MethodSource("source")
    void testParseDouble(String input, Double expected) {
        assertEquals(expected,  CharSequenceParser.parseDouble(input));
    }

    @ParameterizedTest(name = "parseDouble({0} -> {1})")
    @MethodSource("source")
    void testParseDouble2(String input, Double expected) {
        assertEquals(expected,  Double.parseDouble(input));
    }

    static Stream<Arguments> source() {
        return Stream.of(
                Arguments.of("0", 0.0),
                Arguments.of("-0", -0.0),
                Arguments.of("1", 1.0),
                Arguments.of("-1", -1.0),
                Arguments.of("1.12345678901234567", 1.12345678901234567),
                Arguments.of("-1.12345678901234567", -1.12345678901234567),
                Arguments.of("1.7976931348623157E308", 1.7976931348623157E308),
                Arguments.of("-1.7976931348623157E308", -1.7976931348623157E308),
                Arguments.of("2E308", Double.POSITIVE_INFINITY),
                Arguments.of("-2E308", Double.NEGATIVE_INFINITY),
                Arguments.of("Infinity", Double.POSITIVE_INFINITY),
                Arguments.of("-Infinity", Double.NEGATIVE_INFINITY),
                Arguments.of("1E-400", 0.0),
                Arguments.of("-1E-400", -0.0),
                Arguments.of("NaN", Double.NaN),
                Arguments.of("-4.9E-324", -4.9E-324),
                Arguments.of("4.9E-324", 4.9E-324),
                Arguments.of("3.1415E10", 3.1415E10),
                Arguments.of("3.0e10", 3.0E10),
                Arguments.of("3.0E66", 3.0E66),
                Arguments.of("3.1415E38", 3.1415E38),
                Arguments.of("3.1415E-38", 3.1415E-38)
        );
    }

    static Stream<Arguments> sourceFloat() {
        return Stream.of(
                Arguments.of("0"),
                Arguments.of("-0"),
                Arguments.of("1"),
                Arguments.of("-1"),
                Arguments.of("1.123456"),
                Arguments.of("-1.123456"),
                Arguments.of("2E308"),
                Arguments.of("-2E308"),
                Arguments.of("Infinity"),
                Arguments.of("-Infinity"),
                Arguments.of("1E-400"),
                Arguments.of("-1E-400"),
                Arguments.of("NaN"),
                Arguments.of("3.1415E10"),
                Arguments.of("3.1415E-10"),
                Arguments.of("3.0e10"),
                Arguments.of("3.1415E39"),
                Arguments.of("3.1415E39"),
                Arguments.of("0.0000000000000000000000000000000001234567"),
                Arguments.of("0.00000000000000001234567"),
                Arguments.of("1234567000000000000000000000000000000000"),
                Arguments.of("123456700000000000000000"),
                Arguments.of(String.valueOf(Float.MAX_VALUE)),
                Arguments.of(String.valueOf(Float.MIN_VALUE))
        );
    }

}