package com.epam.deltix.util.lang;

import com.epam.deltix.util.lang.Util;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Andy
 *         Date: 6/4/12 11:15 AM
 */
@SuppressWarnings("CodeBlock2Expr")
@Tag("utils")
public class Test_Util {

    @Test
    public void testArrayAddObjectArgument () {
        String[] input = {"a", "b", "c"};
        String[] output = Util.arrayadd(input, "d");
        assertEquals("[a, b, c, d]", Arrays.toString(output));
    }

    @Test
    void doubleUntilAtLeast_whenInputIsZero_shouldReturnLimit() {
        // Test the special case where 'a' is 0
        assertEquals(100, Util.doubleUntilAtLeast(0, 100));
        assertEquals(0, Util.doubleUntilAtLeast(0, 0));
        assertEquals(-50, Util.doubleUntilAtLeast(0, -50));
    }

    @ParameterizedTest(name = "doubleUntilAtLeast({0}, {1}) should be {2}")
    @MethodSource("doubleUntilAtLeast_data")
    void doubleUntilAtLeast_basic(int a, int limit, int expected) {
        assertEquals(expected, Util.doubleUntilAtLeast(a, limit));
    }

    @ParameterizedTest(name = "growArraySize({0}, {1}) should be {2}")
    @MethodSource("doubleUntilAtLeast_data")
    void growArraySize_basic(int a, int limit, int expected) {
        assertEquals(expected, Util.growArraySize(a, limit));
    }

    static Stream<Arguments> doubleUntilAtLeast_data() {
        return Stream.of(
                Arguments.of(3, 20, 24),     // 3 -> 6 -> 12 -> 24
                Arguments.of(5, 18, 20),     // 5 -> 10 -> 20
                Arguments.of(10, 10, 10),    // a == limit
                Arguments.of(25, 20, 25)     // a > limit
        );
    }

    @Test
    void doubleUntilAtLeast_overflow() {
        int halfMax = Integer.MAX_VALUE / 2;

        assertThrows(IllegalArgumentException.class, () -> {
            Util.doubleUntilAtLeast(halfMax, Integer.MAX_VALUE);
        });
    }

    @Test
    void doubleUntilAtLeast_overflowBelowArrayLimit() {
        int halfMax = Integer.MAX_VALUE / 2;

        assertThrows(IllegalArgumentException.class, () -> {
            Util.doubleUntilAtLeast(halfMax + 1, Integer.MAX_VALUE - 20);
        });
    }

    @Test
    void growArraySize_overflow() {
        int halfMax = Integer.MAX_VALUE / 2;

        assertThrows(IllegalArgumentException.class, () -> {
            Util.doubleUntilAtLeast(halfMax, Integer.MAX_VALUE);
        });
    }

    @Test
    void growArraySize_noOverflowBelowArrayLimit() {
        int halfMax = Integer.MAX_VALUE / 2;
        int limit = Integer.MAX_VALUE - 20;

        int maxArraySize = Integer.MAX_VALUE - 8;
        assertEquals(maxArraySize, Util.growArraySize(halfMax + 1, limit));
    }
}
