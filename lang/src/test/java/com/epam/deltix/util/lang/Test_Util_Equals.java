package deltix.util.lang;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.CharBuffer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@code Util.equals} methods:
 * <ol>
 *   <li>{@link Util#equals(CharSequence, CharSequence)}</li>
 *   <li>{@link Util#equals(StringBuilder, StringBuilder)}</li>
 *   <li>{@link Util#equals(String, CharSequence)}</li>
 *   <li>{@link Util#equals(CharSequence, String)}</li>
 *   <li>{@link Util#equals(String, String)}</li>
 *   <li>{@link Util#equals(CharSequence, CharSequence, int)}</li>
 * </ol>
 *
 * @author Generated
 */
@SuppressWarnings({"RedundantCast", "StringOperationCanBeSimplified"})
@Tag("utils")
public class Test_Util_Equals {

    // ==================================================================================
    // Helper: a minimal CharSequence that is NOT String and NOT StringBuilder.
    // This ensures we exercise the generic char-by-char comparison path.
    // ==================================================================================

    /**
     * A trivial CharSequence wrapper that is neither String nor StringBuilder,
     * used to exercise the generic fallback comparison path in
     * {@link Util#equals(CharSequence, CharSequence)}.
     */
    private static CharSequence plain(String s) {
        // CharBuffer.wrap produces a CharSequence that is not String or StringBuilder
        return CharBuffer.wrap(s);
    }

    // ==================================================================================
    // 1. equals(CharSequence, CharSequence) — null combinations
    // ==================================================================================

    @Test
    void csCs_bothNull() {
        assertTrue(Util.equals((CharSequence) null, (CharSequence) null));
    }

    @Test
    void csCs_firstNullSecondNonNull() {
        assertFalse(Util.equals((CharSequence) null, (CharSequence) "abc"));
    }

    @Test
    void csCs_firstNonNullSecondNull() {
        assertFalse(Util.equals((CharSequence) "abc", (CharSequence) null));
    }

    // ==================================================================================
    // 1. equals(CharSequence, CharSequence) — same reference
    // ==================================================================================

    @Test
    void csCs_sameReference_string() {
        String s = "hello";
        assertTrue(Util.equals((CharSequence) s, (CharSequence) s));
    }

    @Test
    void csCs_sameReference_stringBuilder() {
        StringBuilder sb = new StringBuilder("hello");
        assertTrue(Util.equals((CharSequence) sb, (CharSequence) sb));
    }

    @Test
    void csCs_sameReference_plain() {
        CharSequence cs = plain("hello");
        assertTrue(Util.equals(cs, cs));
    }

    // ==================================================================================
    // 1. equals(CharSequence, CharSequence) — all type combinations (equal content)
    //
    // Types: String, StringBuilder, plain CharSequence (CharBuffer)
    // Matrix: 3 x 3 = 9 combinations
    // ==================================================================================

    static Stream<Arguments> csCs_equalContent() {
        String value = "test123";
        // Suppliers for each type (fresh instances each row to avoid reference equality)
        return Stream.of(
                Arguments.of("String x String",         (CharSequence) new String(value),              (CharSequence) new String(value)),
                Arguments.of("String x StringBuilder",  (CharSequence) new String(value),              (CharSequence) new StringBuilder(value)),
                Arguments.of("String x CharBuffer",     (CharSequence) new String(value),              plain(value)),
                Arguments.of("StringBuilder x String",  (CharSequence) new StringBuilder(value),       (CharSequence) new String(value)),
                Arguments.of("StringBuilder x SB",      (CharSequence) new StringBuilder(value),       (CharSequence) new StringBuilder(value)),
                Arguments.of("StringBuilder x CharBuf", (CharSequence) new StringBuilder(value),       plain(value)),
                Arguments.of("CharBuffer x String",     plain(value),                                  (CharSequence) new String(value)),
                Arguments.of("CharBuffer x SB",         plain(value),                                  (CharSequence) new StringBuilder(value)),
                Arguments.of("CharBuffer x CharBuffer",  plain(value),                                 plain(value))
        );
    }

    @ParameterizedTest(name = "csCs equal: {0}")
    @MethodSource("csCs_equalContent")
    void csCs_equalContent(String label, CharSequence s1, CharSequence s2) {
        assertTrue(Util.equals(s1, s2), label);
    }

    // ==================================================================================
    // 1. equals(CharSequence, CharSequence) — all type combinations (different content)
    // ==================================================================================

    static Stream<Arguments> csCs_differentContent() {
        String a = "abc";
        String b = "xyz";
        return Stream.of(
                Arguments.of("String x String",         (CharSequence) new String(a),             (CharSequence) new String(b)),
                Arguments.of("String x StringBuilder",  (CharSequence) new String(a),             (CharSequence) new StringBuilder(b)),
                Arguments.of("String x CharBuffer",     (CharSequence) new String(a),             plain(b)),
                Arguments.of("StringBuilder x String",  (CharSequence) new StringBuilder(a),      (CharSequence) new String(b)),
                Arguments.of("StringBuilder x SB",      (CharSequence) new StringBuilder(a),      (CharSequence) new StringBuilder(b)),
                Arguments.of("StringBuilder x CharBuf", (CharSequence) new StringBuilder(a),      plain(b)),
                Arguments.of("CharBuffer x String",     plain(a),                                 (CharSequence) new String(b)),
                Arguments.of("CharBuffer x SB",         plain(a),                                 (CharSequence) new StringBuilder(b)),
                Arguments.of("CharBuffer x CharBuffer",  plain(a),                                plain(b))
        );
    }

    @ParameterizedTest(name = "csCs different: {0}")
    @MethodSource("csCs_differentContent")
    void csCs_differentContent(String label, CharSequence s1, CharSequence s2) {
        assertFalse(Util.equals(s1, s2), label);
    }

    // ==================================================================================
    // 1. equals(CharSequence, CharSequence) — different lengths
    // ==================================================================================

    static Stream<Arguments> csCs_differentLength() {
        return Stream.of(
                Arguments.of("String x String",         (CharSequence) "ab",                     (CharSequence) "abc"),
                Arguments.of("SB x SB",                 (CharSequence) new StringBuilder("ab"),  (CharSequence) new StringBuilder("abc")),
                Arguments.of("CharBuf x CharBuf",       plain("ab"),                             plain("abc")),
                Arguments.of("String x SB",             (CharSequence) "ab",                     (CharSequence) new StringBuilder("abc")),
                Arguments.of("CharBuf x String",        plain("ab"),                             (CharSequence) "abc")
        );
    }

    @ParameterizedTest(name = "csCs different length: {0}")
    @MethodSource("csCs_differentLength")
    void csCs_differentLength(String label, CharSequence s1, CharSequence s2) {
        assertFalse(Util.equals(s1, s2), label);
    }

    // ==================================================================================
    // 1. equals(CharSequence, CharSequence) — empty strings
    // ==================================================================================

    @Test
    void csCs_bothEmpty_string() {
        assertTrue(Util.equals((CharSequence) "", (CharSequence) new String("")));
    }

    @Test
    void csCs_bothEmpty_sb() {
        assertTrue(Util.equals((CharSequence) new StringBuilder(), (CharSequence) new StringBuilder()));
    }

    @Test
    void csCs_bothEmpty_mixed() {
        assertTrue(Util.equals((CharSequence) "", (CharSequence) new StringBuilder()));
    }

    @Test
    void csCs_emptyVsNonEmpty() {
        assertFalse(Util.equals((CharSequence) "", (CharSequence) "a"));
    }

    // ==================================================================================
    // 1. equals(CharSequence, CharSequence) — differ only at the end (reverse-iteration)
    // ==================================================================================

    @Test
    void csCs_differAtEnd_plainCharSequences() {
        // Exercise the reverse-iteration heuristic in the generic path
        assertFalse(Util.equals(plain("abcX"), plain("abcY")));
    }

    @Test
    void csCs_differAtStart_plainCharSequences() {
        assertFalse(Util.equals(plain("Xbcd"), plain("Ybcd")));
    }

    // ==================================================================================
    // 1b. equals(StringBuilder, StringBuilder) — null combinations
    // ==================================================================================

    @Test
    void sbSb_bothNull() {
        assertTrue(Util.equals((StringBuilder) null, (StringBuilder) null));
    }

    @Test
    void sbSb_firstNullSecondNonNull() {
        assertFalse(Util.equals((StringBuilder) null, new StringBuilder("abc")));
    }

    @Test
    void sbSb_firstNonNullSecondNull() {
        assertFalse(Util.equals(new StringBuilder("abc"), (StringBuilder) null));
    }

    // ==================================================================================
    // 1b. equals(StringBuilder, StringBuilder) — same reference
    // ==================================================================================

    @Test
    void sbSb_sameReference() {
        StringBuilder sb = new StringBuilder("hello");
        assertTrue(Util.equals(sb, sb));
    }

    // ==================================================================================
    // 1b. equals(StringBuilder, StringBuilder) — equal content
    // ==================================================================================

    @Test
    void sbSb_equalContent() {
        assertTrue(Util.equals(new StringBuilder("test123"), new StringBuilder("test123")));
    }

    @Test
    void sbSb_equalContent_singleChar() {
        assertTrue(Util.equals(new StringBuilder("x"), new StringBuilder("x")));
    }

    // ==================================================================================
    // 1b. equals(StringBuilder, StringBuilder) — different content
    // ==================================================================================

    @Test
    void sbSb_differentContent() {
        assertFalse(Util.equals(new StringBuilder("abc"), new StringBuilder("xyz")));
    }

    @Test
    void sbSb_differAtEnd() {
        assertFalse(Util.equals(new StringBuilder("abcX"), new StringBuilder("abcY")));
    }

    @Test
    void sbSb_differAtStart() {
        assertFalse(Util.equals(new StringBuilder("Xbcd"), new StringBuilder("Ybcd")));
    }

    // ==================================================================================
    // 1b. equals(StringBuilder, StringBuilder) — different lengths
    // ==================================================================================

    @Test
    void sbSb_differentLength() {
        assertFalse(Util.equals(new StringBuilder("ab"), new StringBuilder("abc")));
        assertFalse(Util.equals(new StringBuilder("abc"), new StringBuilder("ab")));
    }

    @Test
    void sbSb_differentLength_prefixMatch() {
        // "abc" is a prefix of "abcdef" — should still be false
        assertFalse(Util.equals(new StringBuilder("abc"), new StringBuilder("abcdef")));
    }

    // ==================================================================================
    // 1b. equals(StringBuilder, StringBuilder) — empty strings
    // ==================================================================================

    @Test
    void sbSb_bothEmpty() {
        assertTrue(Util.equals(new StringBuilder(), new StringBuilder()));
    }

    @Test
    void sbSb_emptyVsNonEmpty() {
        assertFalse(Util.equals(new StringBuilder(), new StringBuilder("a")));
        assertFalse(Util.equals(new StringBuilder("a"), new StringBuilder()));
    }

    // ==================================================================================
    // 2. equals(String, CharSequence) — null combinations
    // ==================================================================================

    @Test
    void strCs_bothNull() {
        assertTrue(Util.equals((String) null, (CharSequence) null));
    }

    @Test
    void strCs_firstNullSecondNonNull() {
        assertFalse(Util.equals((String) null, (CharSequence) "abc"));
    }

    @Test
    void strCs_firstNonNullSecondNull() {
        assertFalse(Util.equals("abc", (CharSequence) null));
    }

    // ==================================================================================
    // 2. equals(String, CharSequence) — same reference
    // ==================================================================================

    @Test
    void strCs_sameReference() {
        String s = "hello";
        // s2 is the same object cast to CharSequence
        assertTrue(Util.equals(s, (CharSequence) s));
    }

    // ==================================================================================
    // 2. equals(String, CharSequence) — equal content with different CharSequence types
    // ==================================================================================

    static Stream<Arguments> strCs_equalContent() {
        String value = "hello";
        return Stream.of(
                Arguments.of("String x String",         value, (CharSequence) new String(value)),
                Arguments.of("String x StringBuilder",  value, (CharSequence) new StringBuilder(value)),
                Arguments.of("String x CharBuffer",     value, plain(value))
        );
    }

    @ParameterizedTest(name = "strCs equal: {0}")
    @MethodSource("strCs_equalContent")
    void strCs_equalContent(String label, String s1, CharSequence s2) {
        assertTrue(Util.equals(s1, s2), label);
    }

    // ==================================================================================
    // 2. equals(String, CharSequence) — different content
    // ==================================================================================

    static Stream<Arguments> strCs_differentContent() {
        return Stream.of(
                Arguments.of("String x String",         "abc", (CharSequence) "xyz"),
                Arguments.of("String x StringBuilder",  "abc", (CharSequence) new StringBuilder("xyz")),
                Arguments.of("String x CharBuffer",     "abc", plain("xyz"))
        );
    }

    @ParameterizedTest(name = "strCs different: {0}")
    @MethodSource("strCs_differentContent")
    void strCs_differentContent(String label, String s1, CharSequence s2) {
        assertFalse(Util.equals(s1, s2), label);
    }

    // ==================================================================================
    // 2. equals(String, CharSequence) — different lengths
    // ==================================================================================

    @Test
    void strCs_differentLength() {
        assertFalse(Util.equals("ab", (CharSequence) new StringBuilder("abc")));
        assertFalse(Util.equals("abc", (CharSequence) new StringBuilder("ab")));
    }

    // ==================================================================================
    // 2. equals(String, CharSequence) — empty
    // ==================================================================================

    @Test
    void strCs_bothEmpty() {
        assertTrue(Util.equals("", (CharSequence) new StringBuilder()));
    }

    @Test
    void strCs_emptyVsNonEmpty() {
        assertFalse(Util.equals("", (CharSequence) "x"));
        assertFalse(Util.equals("x", (CharSequence) ""));
    }

    // ==================================================================================
    // 3. equals(CharSequence, String) — null combinations
    // ==================================================================================

    @Test
    void csStr_bothNull() {
        assertTrue(Util.equals((CharSequence) null, (String) null));
    }

    @Test
    void csStr_firstNullSecondNonNull() {
        assertFalse(Util.equals((CharSequence) null, "abc"));
    }

    @Test
    void csStr_firstNonNullSecondNull() {
        assertFalse(Util.equals((CharSequence) "abc", (String) null));
    }

    // ==================================================================================
    // 3. equals(CharSequence, String) — equal content with different CharSequence types
    //    (delegates to equals(String, CharSequence) with swapped args)
    // ==================================================================================

    static Stream<Arguments> csStr_equalContent() {
        String value = "world";
        return Stream.of(
                Arguments.of("String x String",         (CharSequence) new String(value),          value),
                Arguments.of("StringBuilder x String",  (CharSequence) new StringBuilder(value),   value),
                Arguments.of("CharBuffer x String",     plain(value),                              value)
        );
    }

    @ParameterizedTest(name = "csStr equal: {0}")
    @MethodSource("csStr_equalContent")
    void csStr_equalContent(String label, CharSequence s1, String s2) {
        assertTrue(Util.equals(s1, s2), label);
    }

    // ==================================================================================
    // 3. equals(CharSequence, String) — different content
    // ==================================================================================

    static Stream<Arguments> csStr_differentContent() {
        return Stream.of(
                Arguments.of("String x String",         (CharSequence) "abc",                     "xyz"),
                Arguments.of("StringBuilder x String",  (CharSequence) new StringBuilder("abc"),  "xyz"),
                Arguments.of("CharBuffer x String",     plain("abc"),                             "xyz")
        );
    }

    @ParameterizedTest(name = "csStr different: {0}")
    @MethodSource("csStr_differentContent")
    void csStr_differentContent(String label, CharSequence s1, String s2) {
        assertFalse(Util.equals(s1, s2), label);
    }

    // ==================================================================================
    // 3. equals(CharSequence, String) — different lengths
    // ==================================================================================

    @Test
    void csStr_differentLength() {
        assertFalse(Util.equals((CharSequence) new StringBuilder("ab"), "abc"));
        assertFalse(Util.equals((CharSequence) new StringBuilder("abc"), "ab"));
    }

    // ==================================================================================
    // 3. equals(CharSequence, String) — empty
    // ==================================================================================

    @Test
    void csStr_bothEmpty() {
        assertTrue(Util.equals((CharSequence) new StringBuilder(), ""));
    }

    @Test
    void csStr_emptyVsNonEmpty() {
        assertFalse(Util.equals((CharSequence) "", "x"));
        assertFalse(Util.equals((CharSequence) "x", ""));
    }

    // ==================================================================================
    // 4. equals(String, String) — null combinations
    // ==================================================================================

    @Test
    void strStr_bothNull() {
        assertTrue(Util.equals((String) null, (String) null));
    }

    @Test
    void strStr_firstNullSecondNonNull() {
        assertFalse(Util.equals((String) null, "abc"));
    }

    @Test
    void strStr_firstNonNullSecondNull() {
        assertFalse(Util.equals("abc", (String) null));
    }

    // ==================================================================================
    // 4. equals(String, String) — equal and different content
    // ==================================================================================

    @Test
    void strStr_equalContent() {
        assertTrue(Util.equals(new String("abc"), new String("abc")));
    }

    @Test
    void strStr_differentContent() {
        assertFalse(Util.equals("abc", "xyz"));
    }

    @Test
    void strStr_sameReference() {
        String s = "sameRef";
        assertTrue(Util.equals(s, s));
    }

    @Test
    void strStr_differentLength() {
        assertFalse(Util.equals("ab", "abc"));
        assertFalse(Util.equals("abc", "ab"));
    }

    @Test
    void strStr_bothEmpty() {
        assertTrue(Util.equals("", ""));
    }

    @Test
    void strStr_emptyVsNonEmpty() {
        assertFalse(Util.equals("", "a"));
        assertFalse(Util.equals("a", ""));
    }

    // ==================================================================================
    // 5. equals(CharSequence, CharSequence, int maxLength) — null combinations
    // ==================================================================================

    @Test
    void csCs_maxLen_bothNull() {
        assertTrue(Util.equals(null, null, 5));
    }

    @Test
    void csCs_maxLen_firstNull() {
        assertFalse(Util.equals(null, "abc", 5));
    }

    @Test
    void csCs_maxLen_secondNull() {
        assertFalse(Util.equals("abc", null, 5));
    }

    // ==================================================================================
    // 5. equals(CharSequence, CharSequence, int maxLength) — same reference
    // ==================================================================================

    @Test
    void csCs_maxLen_sameReference() {
        CharSequence s = "hello";
        assertTrue(Util.equals(s, s, 3));
    }

    // ==================================================================================
    // 5. equals(CharSequence, CharSequence, int maxLength) — equal within maxLength
    // ==================================================================================

    @Test
    void csCs_maxLen_equalWithinLimit() {
        // "abcXXX" and "abcYYY" are equal within the first 3 characters
        assertTrue(Util.equals("abcXXX", "abcYYY", 3));
    }

    @Test
    void csCs_maxLen_equalWithinLimit_sb() {
        assertTrue(Util.equals(new StringBuilder("abcXXX"), new StringBuilder("abcYYY"), 3));
    }

    @Test
    void csCs_maxLen_equalExactly() {
        // Both strings have length 3, maxLength = 3
        assertTrue(Util.equals("abc", "abc", 3));
    }

    @Test
    void csCs_maxLen_equalShorterThanLimit() {
        // Both strings shorter than maxLength — full comparison, equal
        assertTrue(Util.equals("ab", "ab", 10));
    }

    // ==================================================================================
    // 5. equals(CharSequence, CharSequence, int maxLength) — different within maxLength
    // ==================================================================================

    @Test
    void csCs_maxLen_differWithinLimit() {
        // Differ at position 1, within maxLength 3
        assertFalse(Util.equals("aXc", "aYc", 3));
    }

    @Test
    void csCs_maxLen_differAtFirstChar() {
        assertFalse(Util.equals("Xbc", "Ybc", 1));
    }

    // ==================================================================================
    // 5. equals(CharSequence, CharSequence, int maxLength) — different lengths
    //    but same content within maxLength
    // ==================================================================================

    @Test
    void csCs_maxLen_differentLengthBothExceedLimit() {
        // "abcde" (len=5) vs "abcfg" (len=5) — same length, differ after 3
        assertTrue(Util.equals("abcde", "abcfg", 3));
    }

    @Test
    void csCs_maxLen_differentLengthsOneExceedsLimit() {
        // "abc" (len=3) vs "abcde" (len=5) — truncated lengths 3 vs 3 — equal
        assertTrue(Util.equals("abc", "abcde", 3));
    }

    @Test
    void csCs_maxLen_differentLengthsBothWithinLimit() {
        // "ab" (len=2) vs "abc" (len=3), maxLength=10 — lengths differ → not equal
        assertFalse(Util.equals("ab", "abc", 10));
    }

    // ==================================================================================
    // 5. equals(CharSequence, CharSequence, int maxLength) — maxLength = 0 (unlimited)
    // ==================================================================================

    @Test
    void csCs_maxLen_zeroMeansUnlimited_equal() {
        assertTrue(Util.equals("hello", "hello", 0));
    }

    @Test
    void csCs_maxLen_zeroMeansUnlimited_different() {
        assertFalse(Util.equals("hello", "world", 0));
    }

    @Test
    void csCs_maxLen_zeroMeansUnlimited_differentLength() {
        assertFalse(Util.equals("hi", "hello", 0));
    }

    // ==================================================================================
    // 5. equals(CharSequence, CharSequence, int maxLength) — empty strings
    // ==================================================================================

    @Test
    void csCs_maxLen_bothEmpty() {
        assertTrue(Util.equals("", "", 5));
    }

    @Test
    void csCs_maxLen_emptyVsNonEmpty() {
        assertFalse(Util.equals("", "a", 5));
        assertFalse(Util.equals("a", "", 5));
    }

    // ==================================================================================
    // 5. equals(CharSequence, CharSequence, int maxLength) — maxLength = 1
    // ==================================================================================

    @Test
    void csCs_maxLen_one_sameFirstChar() {
        assertTrue(Util.equals("abc", "axyz", 1));
    }

    @Test
    void csCs_maxLen_one_differentFirstChar() {
        assertFalse(Util.equals("abc", "xyz", 1));
    }

    // ==================================================================================
    // Symmetry checks — ensure equals(a,b) == equals(b,a) for the generic overload
    // ==================================================================================

    static Stream<Arguments> symmetryPairs() {
        return Stream.of(
                Arguments.of((CharSequence) "abc",                         (CharSequence) new StringBuilder("abc")),
                Arguments.of(plain("abc"),                                 (CharSequence) "abc"),
                Arguments.of((CharSequence) new StringBuilder("abc"),      plain("abc")),
                Arguments.of((CharSequence) null,                          (CharSequence) "abc"),
                Arguments.of((CharSequence) "abc",                         (CharSequence) "xyz")
        );
    }

    @ParameterizedTest(name = "symmetry: {0} vs {1}")
    @MethodSource("symmetryPairs")
    void csCs_symmetry(CharSequence s1, CharSequence s2) {
        assertEquals(Util.equals(s1, s2), Util.equals(s2, s1));
    }
}
