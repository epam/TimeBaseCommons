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
package com.epam.deltix.util.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.text.DecimalFormat;

@SuppressWarnings({"WeakerAccess", "unused"})
public class StringUtils {
    public static final String REGEXP_WHITESPACE       = "[ \\t\\n\\r]+";

    public static final String nanosMicrosPatternChars = "UuNn";
    public static final char   NANOS_CHARACTER         = 'n';
    public static final char   MICROS_CHARACTER        = 'u';
    public static final String patternChars            = "GyMdkHmsSEDFwWahKzZ";
    public static final int[]  precisions              = new int[]{6, 9};

    public static       String precisionTemplate       = "(\\d{%s}\\z)|(\\d{3},\\d{3}\\z)|(\\d{3},\\d{3},\\d{3}\\z)";

    public static String []     JAVA_KEYWORDS = {
        "abstract", "assert",
        "boolean", "break", "byte",
        "case", "catch", "char", "class", "const", "continue",
        "default", "do", "double",
        "else", "enum", "extends",
        "false",  "final", "finally", "float", "for",
        "goto",
        "if", "implements", "import", "instanceof", "int", "interface",
        "long", "native", "new", "null",
        "package", "private", "protected", "public",
        "return",
        "short", "static", "strictfp", "super", "switch", "synchronized",
        "this", "throw", "throws", "transient", "true", "try",
        "void", "volatile",
        "while"
    };

    public static String []     CS_KEYWORDS = {
        "abstract", "as", "base", "bool", "break", "byte", "case",
        "catch", "char", "checked", "class", "const", "continue", "decimal",
        "default", "delegate", "do", "double", "else", "enum", "event",
        "explicit", "extern", "false", "finally", "fixed", "float", "for",
        "foreach", "goto", "if", "implicit", "in", "int", "interface",
        "internal", "is", "lock", "long", "namespace", "new", "null",
        "object", "operator", "out", "override", "params", "private", "protected",
        "public", "readonly", "ref", "return", "sbyte", "sealed", "short",
        "sizeof", "stackalloc", "static", "string", "struct", "switch", "this",
        "throw", "true", "try", "typeof", "uint", "ulong", "unchecked",
        "unsafe", "ushort", "using", "virtual", "void", "volatile", "while"
    };

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static void      setStringBuilder (StringBuilder sb, CharSequence value) {
        sb.setLength (0);
        sb.append (value);
    }

    public static String [] splitAtWhitespace (String s) {
        int     delim = 0;
        int     argLength = s.length ();

        while (delim < argLength && !Character.isWhitespace (s.charAt (delim)))
            delim++;

        return (
            new String [] {
                s.substring (0, delim),
                s.substring (delim).trim ()
            }
        );
    }

    public static String join(String separator, String ... items) {
        StringBuilder    sb = new StringBuilder ();
        for (String item : items) {
            if (sb.length() > 0)
                sb.append (separator);

            sb.append(item);
        }
        return sb.toString();
    }

    public static String join(String separator, String [] items, int offset, int length) {
        StringBuilder    sb = new StringBuilder ();
        for (int ii = 0; ii < length; ii++) {
            if (ii > 0)
                sb.append (separator);

            sb.append (items [offset + ii]);
        }
        return sb.toString();
    }

    /**
     * Capitalizes first letter of each word in the buffer, and lowercases
     * each letter after the first of each word.
     */
    public static String	niceCaps (String s) {
        StringBuffer    sb = new StringBuffer (s);
        niceCaps (sb);
        return (sb.toString ());
    }

    /**
     * Capitalizes first letter of each word in the buffer, and lowercases
     * each letter after the first of each word.
     */
    public static void      niceCaps (StringBuffer buf) {
        boolean capIt = true;
        for (int i = 0; i < buf.length(); i++) {
            char    curChar = buf.charAt(i);
            if (curChar == ' ')
                capIt = true;
            else if (capIt) {
                buf.setCharAt (i, Character.toUpperCase(curChar));
                capIt = false;
            }
            else
                buf.setCharAt (i, Character.toLowerCase(curChar));
        }
    }

    /** "camelCaseText With Spaces" =&gt; "Camel case text with spaces" */
    public static String deCamelize (String text) {
        StringBuilder result = new StringBuilder(text.length() + 16);
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (i > 0 && Character.isUpperCase(ch)) {
                result.append(' ');
                ch = Character.toLowerCase(ch);
            } else
            if (i == 0 && Character.isLowerCase(ch)) {
                ch = Character.toUpperCase(ch);
            }
            result.append(ch);
        }
        return result.toString();
    }

    public static String	replace (
        String					where,
        String []				what,
        String []				with
    )
    {
        StringBuffer			sb = new StringBuffer (where.length ());
        if (replace (where, what, with, sb, true))
            return (sb.toString ());
        else
            return (where);
    }

    public static void		replace (
        String					where,
        String []				what,
        String []				with,
        StringBuffer			out
    )
    {
        replace (where, what, with, out, true);
    }

    @SuppressWarnings("SameParameterValue")
    private static boolean	replace (
        String					where,
        String []				what,
        String []				with,
        StringBuffer			out,
        boolean					failIfNotFound
    )
    {
        int						numPatterns = what.length;
        int						length = where.length ();

        int						pos = 0;

        for (;;) {
            int		next = -1;
            int		patternIdx = -1;
            //
            //	Find the closest match
            //
            for (int ii = 0; ii < numPatterns; ii++) {
                int	found = where.indexOf (what [ii], pos);

                if (found < pos)
                    continue;

                if (next == -1 || found < next) {
                    next = found;
                    patternIdx = ii;
                }
            }

            if (next == -1)
                break;

            while (pos < next)
                out.append (where.charAt (pos++));

            out.append (with [patternIdx]);

            pos += what [patternIdx].length ();
        }

        if (pos == 0 && failIfNotFound)
            return (false);

        while (pos < length)
            out.append (where.charAt (pos++));

        return (true);
    }

    public static int           parseDecimalDigit (CharSequence s, int idx) {
        int         d = s.charAt (idx) - '0';

        if (d < 0 || d > 9)
            throw new NumberFormatException (s.toString ());

        return (d);
    }

    public static int           parseUnsignedDecimalInt (CharSequence s, int idx, int len) {
        int         d = 0;

        for (int ii = 0; ii < len; ii++)
            d = d * 10 + parseDecimalDigit (s, idx + ii);

        return (d);
    }

    public static long      parseMemorySize (String text) {
        long        k = 1;
        int         n = text.length ();

        if (n == 0)
            throw new NumberFormatException ();

        char        c = text.charAt (n - 1);

        if (c == 'B') {
            n--;

            if (n == 0)
                throw new NumberFormatException (text);

            c = text.charAt (n - 1);
        }

        if (c == 'K') {
            k = 1L << 10;
            n--;
        }
        else if (c == 'M') {
            k = 1L << 20;
            n--;
        }
        else if (c == 'G') {
            k = 1L << 30;
            n--;
        }

        return ((long) (k * Double.parseDouble (text.substring (0, n))));
    }

    private static void         hex (int n, StringBuilder out) {
        n = n & 0xF;

        if (n < 10)
            out.append ('0' + n);
        else
            out.append ('A' + n);
    }

    public static String        escapeJavaString (CharSequence str) {
        StringBuilder   sb = new StringBuilder ();
        escapeJavaString (str, sb);
        return (sb.toString ());
    }
    
    public static void          escapeJavaString (CharSequence str, StringBuilder out) {
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt (i);

            switch (ch) {
                case '\b':  out.append ("\\b"); break;
                case '\n':  out.append ("\\n"); break;
                case '\t':  out.append ("\\t"); break;
                case '\f':  out.append ("\\f"); break;
                case '\r':  out.append ("\\r"); break;
                case '"':   out.append ("\\\""); break;
                case '\'':  out.append ("\\\'"); break;
                case '\\':  out.append ("\\\\"); break;
                default:
                    if (ch >= 32 && ch <= 0x7F)
                        out.append (ch);
                    else {
                        out.append("\\u");
                        hex (ch >>> 12, out);
                        hex (ch >>> 8, out);
                        hex (ch >>> 4, out);
                        hex (ch, out);
                    }
                    break;
            }
        }
    }

    public static String        escapeCString (CharSequence str) {
        StringBuilder   sb = new StringBuilder ();
        escapeJavaString (str, sb);
        return (sb.toString ());
    }
    
    public static String        escapeCStringLiteral (CharSequence str) {
        StringBuilder   sb = new StringBuilder ();
        sb.append ('\"');
        escapeJavaString (str, sb);
        sb.append ('\"');
        return (sb.toString ());
    }
    
    public static void          escapeCString (CharSequence str, StringBuilder out) {
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt (i);

            switch (ch) {
                case '\b':  out.append ("\\b"); break;
                case '\n':  out.append ("\\n"); break;
                case '\t':  out.append ("\\t"); break;
                case '\f':  out.append ("\\f"); break;
                case '\r':  out.append ("\\r"); break;
                case '"':   out.append ("\\\""); break;
                case '\'':  out.append ("\\\'"); break;
                case '\\':  out.append ("\\\\"); break;
                default:
                    if (ch >= 32 && ch <= 0x7F)
                        out.append (ch);
                    else {
                        out.append("\\x");
                        hex (ch >>> 12, out);
                        hex (ch >>> 8, out);
                        hex (ch >>> 4, out);
                        hex (ch, out);
                    }
                    break;
            }
        }
    }

    /**
     *
     */
    public static String        escapeJavaStringLiteral (CharSequence str) {
        StringBuilder       out = new StringBuilder ();

        out.append ('\"');
        escapeJavaString (str, out);
        out.append ('\"');
        return (out.toString ());
    }

    public static boolean isValidJavaIdOrKeyword (CharSequence s) {
        // an empty or null string cannot be a valid identifier
        if (s == null) 
            return false;        

        int             n = s.length ();
        
        if (n == 0) 
            return false;        

        if (!Character.isJavaIdentifierStart (s.charAt (0))) 
            return false;        

        for (int i = 1; i < n; i++) 
            if (!Character.isJavaIdentifierPart (s.charAt (i))) 
                return false;        

        return true;
    }
        
    /**
     * A Unicode character of classes Lu, Ll, Lt, Lm, Lo, or Nl 
     */
    public static boolean   isCSIdentifierStart (char c) {
        if (c == '_')
            return (true);
        
        switch (Character.getType (c)) {
            case Character.LETTER_NUMBER:
            case Character.UPPERCASE_LETTER:
            case Character.LOWERCASE_LETTER:
            case Character.TITLECASE_LETTER:
            case Character.MODIFIER_LETTER:
            case Character.OTHER_LETTER:
                return (true);
        }
        
        return (false);
    }
    
    /**
     * <pre>
     *  identifier-part-character:
     *  letter-character - A Unicode character of classes Lu, Ll, Lt, Lm, Lo, or Nl
     *  decimal-digit-character - A Unicode character of the class Nd
     *  connecting-character - A Unicode character of the class Pc
     *  combining-character - A Unicode character of classes Mn or Mc
     *  formatting-character - A Unicode character of the class Cf
     *  </pre>
     */
    public static boolean   isCSIdentifierPart (char c) {
        switch (Character.getType (c)) {
            case Character.LETTER_NUMBER:
            case Character.UPPERCASE_LETTER:
            case Character.LOWERCASE_LETTER:
            case Character.TITLECASE_LETTER:
            case Character.MODIFIER_LETTER:
            case Character.OTHER_LETTER:
            case Character.DECIMAL_DIGIT_NUMBER:
            case Character.CONNECTOR_PUNCTUATION:
            case Character.NON_SPACING_MARK:
            case Character.COMBINING_SPACING_MARK:
            case Character.FORMAT:
                return (true);
        }
        
        return (false);
    }
    
    public static boolean isValidCSIdOrKeyword (CharSequence s) {
        // an empty or null string cannot be a valid identifier        
        if (s == null) 
            return false;        

        int             n = s.length ();
        
        if (n == 0) 
            return false;        
                        
        if (!isCSIdentifierStart (s.charAt (0))) 
            return false;        

        for (int pos = 1; pos < n; pos++) 
            if (!isCSIdentifierPart (s.charAt (pos))) 
                return false;        

        return true;
    }

    public static boolean       isJavaReservedWord (CharSequence value){
        for (String keyword: JAVA_KEYWORDS)
            if (Util.equals (keyword, value))
                return true;                    

        return false;
    }

    public static boolean       isCSReservedWord (CharSequence value){
        for (String keyword: CS_KEYWORDS)
            if (Util.equals (keyword, value))
                return true;                    

        return false;
    }

    public static int          parseDecimalInt (byte [] bytes, int offset, int len) {
        skipSpace: while (len > 0)
            switch (bytes [offset]) {
                case ' ':
                case '\t':
                    len--;
                    offset++;
                    break;

                default:
                    break skipSpace;
            }

        skipSpace: while (len > 0)
            switch (bytes [offset + len - 1]) {
                case ' ':
                case '\t':
                    len--;
                    break;

                default:
                    break skipSpace;
            }

        int     s;

        switch (bytes [offset]) {
            case '+':
                offset++;
                len--;
                s = 1;
                break;

            case '-':
                offset++;
                len--;
                s = -1;
                break;

            default:
                s = 1;
                break;
        }

        int     v = 0;

        while (len > 0) {
            int     digit = bytes [offset] - '0';

            if (digit < 0 || digit > 9)
                throw new NumberFormatException (
                    "bytes [" + offset + "] = " + bytes [offset]
                );

            v = v * 10 + digit;

            len--;
            offset++;
        }

        return (s * v);
    }

    public static long         parseDecimalLong (byte [] bytes, int offset, int len) {
        skipSpace: while (len > 0)
            switch (bytes [offset]) {
                case ' ':
                case '\t':
                    len--;
                    offset++;
                    break;

                default:
                    break skipSpace;
            }

        skipSpace: while (len > 0)
            switch (bytes [offset + len - 1]) {
                case ' ':
                case '\t':
                    len--;
                    break;

                default:
                    break skipSpace;
            }

        int     s;

        switch (bytes [offset]) {
            case '+':
                offset++;
                len--;
                s = 1;
                break;

            case '-':
                offset++;
                len--;
                s = -1;
                break;

            default:
                s = 1;
                break;
        }

        long     v = 0;

        while (len > 0) {
            int     digit = bytes [offset] - '0';

            if (digit < 0 || digit > 9)
                throw new NumberFormatException (
                    "bytes [" + offset + "] = " + bytes [offset]
                );

            v = v * 10 + digit;
            len--;
            offset++;
        }

        return (s * v);
    }

    public static double          parseDecimalDouble (byte [] bytes, int offset, int len) {
        skipSpace: while (len > 0)
            switch (bytes [offset]) {
                case ' ':
                case '\t':
                    len--;
                    offset++;
                    break;

                default:
                    break skipSpace;
            }

        skipSpace: while (len > 0)
            switch (bytes [offset + len - 1]) {
                case ' ':
                case '\t':
                    len--;
                    break;

                default:
                    break skipSpace;
            }

        int     s;

        switch (bytes [offset]) {
            case '+':
                offset++;
                len--;
                s = 1;
                break;

            case '-':
                offset++;
                len--;
                s = -1;
                break;

            default:
                s = 1;
                break;
        }

        double      v = 0;
        boolean     frac = false;
        double      m = 0.1;

        while (len > 0) {
            byte    b = bytes [offset];

            len--;
            offset++;

            if (b == '.') {
                frac = true;
                break;
            }

            int     digit = b - '0';

            if (digit < 0 || digit > 9)
                throw new NumberFormatException (
                    "bytes [" + offset + "] = " + b
                );

            v = v * 10 + digit;
        }

        if (frac)
            while (len > 0) {
                byte    b = bytes [offset];

                len--;
                offset++;

                int     digit = b - '0';

                if (digit < 0 || digit > 9)
                    throw new NumberFormatException (
                        "bytes [" + offset + "] = " + b
                    );

                v = v + digit * m;
                m *= 0.1;
            }

        return (s * v);
    }

    /** @return string trimmed of spaces, or null if input is empty string or null */
    public static String trim (String str) {
        if (str != null) {
            str = str.trim ();
            if (str.length() == 0)
                str = null;
        }
        return str;
    }

    /** @return true if trimmed input is blank string or null */
    public static boolean isEmpty (CharSequence value) {
        if (value == null)
            return true;

        final int length = value.length();
        for (int i=0; i < length; i++)
            if (value.charAt(i) > ' ')
                return false;
        return true;
    }

    /** @return true if input is null or empty string contains only whitespaces */
    public static boolean isWhitespace (CharSequence value) {
        if (value == null)
            return true;

        final int length = value.length();
        for (int i=0; i < length; i++)
            if ( ! Character.isWhitespace(value.charAt(i)))
                return false;
        return true;
    }

    public static CharSequence trim (CharSequence cs) {
        if (cs == null)
            return null;
        int len = cs.length ();
        int st = 0;

        while ((st < len) && (cs.charAt (st) <= ' ')) {
            st++;
        }
        while ((st < len) && (cs.charAt (len - 1) <= ' ')) {
            len--;
        }
        return ((st > 0) || (len < cs.length ())) ? cs.subSequence (st,
                                                                    len) : cs;
    }

    public static String [] parseCommaSeparatedList (String str) {
        String []       cols = null;
        if (str != null) {
            StringTokenizer tok = new StringTokenizer(str, ", ");
            int             num = tok.countTokens ();
            cols = new String [num];
            int             count = 0;
            while (tok.hasMoreTokens ())
                cols [count++] = tok.nextToken ();
        }
        return (cols);
    }

    public static String    createCommaSepList (String [] strs) {
        if (strs == null)
            return (null);

        StringBuilder    buf = new StringBuilder();
        for (int i = 0; i < strs.length; i++) {
            if (i > 0)
                buf.append (", ");
            buf.append (strs [i]);
        }
        return (buf.toString());
    }

    // adapted from String.indexOf(char[] ...)
    @SuppressWarnings("StatementWithEmptyBody")
    public static int           indexOf (final CharSequence s1, final CharSequence s2) {
        final int sourceCount = s1.length();
        final int targetCount = s2.length();
        char first  = s2.charAt(0);
        int max = (sourceCount - targetCount);

        for (int i = 0; i <= max; i++) {
            /* Look for first character. */
            if (s1.charAt(i) != first) {
                while (++i <= max && s1.charAt(i) != first);
            }

            /* Found first character, now look at the rest of v2 */
            if (i <= max) {
                int j = i + 1;
                int end = j + targetCount - 1;
                for (int k = 1; j < end && s1.charAt(j) ==
                        s2.charAt(k); j++, k++);

                if (j == end) {
                    /* Found whole string. */
                    return i;
                }
            }
        }
        return -1;
    }

    public static int           indexOf (final CharSequence s, final char c) {
        final int length = s.length();

        for (int i = 0; i < length; i++) {
            if (s.charAt(i) == c)
                return i;
        }

        return -1;
    }

    public static boolean   endsWith (CharSequence a, CharSequence b) {
        int     alen = a.length ();
        int     blen = b.length ();
        int     offset = alen - blen;

        if (offset < 0)
            return (false);

        for (int ii = 0; ii < blen; ii++)
            if (a.charAt (offset + ii) != b.charAt (ii))
                return (false);

        return (true);
    }

    @SuppressWarnings("StringEquality")
    public static boolean equals (String one, String two) {
		if (one == null)
			return two == null;

		return one == two || one.equals(two);
	}

    public static void main (String [] args) {
        byte []     b = args [0].getBytes();

        System.out.println (parseDecimalLong (b, 0, b.length));
    }

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

    static {
        DECIMAL_FORMAT.setMaximumIntegerDigits(309);
        DECIMAL_FORMAT.setMaximumFractionDigits(10);
    }

    public static String toDecimalString(float value) {
        final float abs = Math.abs(value);
        if (10e-3 < abs && abs < 10e7)
            return Float.toString(value);
        else
            synchronized (DECIMAL_FORMAT) {
                return DECIMAL_FORMAT.format(value);
            }
    }

    public static String toDecimalString(double value) {
        final double abs = Math.abs(value);
        if (10e-3 < abs && abs < 10e7)
            return Double.toString(value);
        else
            synchronized (DECIMAL_FORMAT) {
                return DECIMAL_FORMAT.format(value);
            }
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public static String[] split(String message, String regex,
                                 boolean trimToNull, boolean discardNullOrEmpty) {
        String[] splitted = message.split(regex);
        if (discardNullOrEmpty) {
            List<String> result = new ArrayList<>(splitted.length);
            for (int i = 0; i < splitted.length; i++) {
                String value = trimToNull ? trim(splitted[i]) : splitted[i];
                if (value == null || value.length() <= 0)
                    continue;
                result.add(value);
            }
            return result.toArray(new String[0]);
        } else {
            if (trimToNull) {
                for (int i = 0; i < splitted.length; i++)
                    splitted[i] = trim(splitted[i]);
            }
            return splitted;
        }
    }

    private static String repeat(char symbol, int length) {
        char[] chars = new char[length];
        Arrays.fill(chars, symbol);
        return String.valueOf(chars);
    }

    public static String messageInFrame(String message, char frameSymbol, int maxFrameLength, int paddingLength) {
        String lineSeparator = Util.NATIVE_LINE_BREAK;
        String[] lines = message.split("\n");

        int maxLength = maxFrameLength;
        if (maxFrameLength <= 0) {
            maxLength = lines[0].length();
            for (int i = 1; i < lines.length; i++) {
                maxLength = Math.max(maxLength, lines[i].length());
            }
            maxLength = 2 * paddingLength + maxLength;
        }

        String topAndBottom = repeat(frameSymbol, maxLength + 2);
        String padding = repeat(' ', paddingLength);
        String emptyLine = repeat(' ', maxLength);

        StringBuilder builder = new StringBuilder();
        builder.append(lineSeparator).append(topAndBottom).append(lineSeparator).
                append(frameSymbol).append(emptyLine).append(frameSymbol).append(lineSeparator);
        for (String line : lines) {
            line = line.trim();
            int rightPaddingLenght = Math.max(1, maxLength - paddingLength - line.length());
            builder.append(frameSymbol).
                    append(padding).append(line).append(repeat(' ', rightPaddingLenght)).
                    append(frameSymbol).append(lineSeparator);
        }
        builder.append(frameSymbol).append(emptyLine).append(frameSymbol).append(lineSeparator).
                append(topAndBottom);

        return builder.toString();
    }

    public static String   quote(String value) {
        String result = "\"\"";
        if (value.length() > 0 && value.charAt(0) != '"')
            result = "\"" + value;
        if (value.length() > 0 && value.charAt(value.length() - 1) != '"')
            result += "\"";

        return result;
    }


    private static String replace(String value, Map<String, String> replacements) {
        String result = value;
        for (String key : replacements.keySet()) {
            result = result.replace(key, replacements.get(key));
        }
        return result;
    }

    // ND TODO: use regex for replacement holders matching
    public static void replace(Properties target, Properties replacements,
                               String placeholderPrefix, String placeholderSuffix) {
        Set<String> replacementKeys = replacements.stringPropertyNames();
        Map<String, String> replaceMap = new HashMap<>(replacementKeys.size());
        for (String key : replacementKeys)
            replaceMap.put(placeholderPrefix + key + placeholderSuffix, replacements.getProperty(key));

        Set<String> keys = target.stringPropertyNames();
        for (String key : keys) {
            String value = target.getProperty(key);
            // replace placeholders
            String newKey = replace(key, replaceMap);
            if (value == null)
                throw new NullPointerException("Value is NULL for " + newKey);
            String newValue = replace(value, replaceMap);
            // remove old entry and put the new one
            target.remove(key);
            target.put(newKey, newValue);
        }
    }


    public static String getPreciseFormat(long timestampInNanos, String preciseTemplate, String delimeter){

        long mantissa = Math.abs(timestampInNanos % (long)Math.pow(10,9));
        String postfix = "";
        if (preciseTemplate!=null && preciseTemplate.length()>0 && nanosMicrosPatternChars.indexOf(preciseTemplate.charAt(preciseTemplate.length()-1))!=-1)
        {
            if (preciseTemplate.length()>1)
                postfix = preciseTemplate.substring(0, preciseTemplate.length()-1);
            int millies = (int) (mantissa / Math.pow(10, 6));
            int micros = (int) (mantissa / Math.pow(10, 3)) - millies * (int)Math.pow(10, 3);
            postfix += addLeadingZeros(String.valueOf(millies), 3) + delimeter + addLeadingZeros(String.valueOf(micros), 3);

            if (preciseTemplate.substring(preciseTemplate.length()-1).equalsIgnoreCase("n")) {
               int nanos = (int) (mantissa - millies * (int)Math.pow(10, 6)- micros * (int) Math.pow(10, 3));
               postfix += delimeter + addLeadingZeros(String.valueOf(nanos), 3);
            }
        }

        return postfix;
    }

    public static String addLeadingZeros(String value, int count){
        if (value==null || value.length()>=count)
            return value;

        StringBuilder valueBuilder = new StringBuilder(value);
        while (valueBuilder.length()<count){
            valueBuilder.insert(0, "0");
        }
        return valueBuilder.toString();
    }

    public static String    trimTrailingWhitespace (String s) {
        int slen = s.length ();
        int n;

        for (n = slen; n > 0; n--)
            if (!Character.isWhitespace (s.charAt (n - 1)))
                break;

        return (n == slen ? s : s.substring (0, n));
    }

    /**
     * <p>Abbreviates a String using ellipses. Specifically:</p>
     *
     * <ul>
     * <li>If <code>value</code> is less than <code>maxWidth</code> characters long, return it.</li>
     * <li>Else abbreviate it to <code>(substring(value, 0, max-3) + "...")</code>.</li>
     * <li>If <code>maxWidth</code> is less than <code>4</code>, throw an <code>IllegalArgumentException</code>.</li>
     * </ul>
     *
     * <pre>
     * StringUtils.abbreviate(null, *) = null
     * StringUtils.abbreviate("", 4) = ""
     * StringUtils.abbreviate("abcdefg", 6) = "abc..."
     * StringUtils.abbreviate("abcdefg", 7) = "abcdefg"
     * StringUtils.abbreviate("abcdefg", 8) = "abcdefg"
     * StringUtils.abbreviate("abcdefg", 4) = "a..."
     * StringUtils.abbreviate("abcdefg", 3) = IllegalArgumentException
     * </pre>
     *
     * @param value the String to check, may be null
     * @param maxWidth maximum length of result String, must be at least 4
     * @return abbreviated String, <code>null</code> if null String input
     * @throws IllegalArgumentException if the width is too small
     */
    public static String abbreviate(String value, int maxWidth) {
        if (value == null)
            return null;
        if (maxWidth < 4)
            throw new IllegalArgumentException("Minimum abbreviation width is 4");
        if (value.length() <= maxWidth)
            return value;
        return value.substring(0, maxWidth - 3) + "...";
    }
    
  /**
     * Compares two strings using only letters and digits. For example:
     * <pre>
     * compareSignatures(",a_ b++ 3=c", "__a%b3c+") 
     * </pre>
     * returns true.
     * @return true if the signatures contains the same sequence of letters and digits
     */
    public static boolean matchSignatures(String signature1, String signature2) {
        
        int i1 = 0;
        int i2 = 0;

        while (true) {
            char c1 = 0;

            while (i1 < signature1.length()) {
                char c = signature1.charAt(i1++);

                if (Character.isLetterOrDigit(c)) {
                    c1 = c;
                    break;
                }
            }

            char c2 = 0;

            while (i2 < signature2.length()) {
                char c = signature2.charAt(i2++);

                if (Character.isLetterOrDigit(c)) {
                    c2 = c;
                    break;
                }
            }

            if (c1 + c2 == 0) {
                break;
            }

            if (c1 != c2) {
                return false;
            }
        }

        return true;
    }   

    public static String toSafeFileName (String s) {
        final StringBuilder     sb = new StringBuilder ();
        final int               fnameLength = s.length ();

        for (int ii = 0; ii < fnameLength; ii++) {
            char    c = s.charAt (ii);

            if (c == '_')
                sb.append ("__");
            else if (!Character.isLetterOrDigit (c) &&
                     c != '-' &&
                     c != '.') 
            {
                sb.append ("_");
                sb.append ((int) c);
                sb.append ("_");
            }
            else
                sb.append (c);
        }

        return (sb.toString ());
    }


    public static String formatSizeToKMGFormat (final long x) {
        if ((x & ((1L << 30) - 1)) == 0)
            return ((x >> 30) + "G");

        if ((x & ((1L << 20) - 1)) == 0)
            return ((x >> 20) + "M");

        if ((x & ((1L << 10) - 1)) == 0)
            return ((x >> 10) + "K");

        return (String.valueOf (x));
    }

    public static String toHex(byte[] bytes) {
        return toHex(bytes, 0, bytes.length);
    }
        
    public static String toHex(byte[] bytes, int offset, int len) {
        final char[] hexChars = new char[len * 2];
        for (int i = 0; i < len; i++) {
            final int v = bytes[i + offset] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }


    /**
     * Searches for a specified pattern in an expression.
     *
     * @param expression - any valid expression of character data type.
     * @param pattern - specific string of characters to search for in {expression},
     *                  and can include the following valid wildcard characters:
     *                  % - Any string of zero or more characters.
     *                  _ (underscore) - Any single character.
     * @return true, if {expression} is match to specified {pattern}, else false.
     */
    public static boolean wildcardMatchRegexp(String expression, String pattern, boolean caseSensitive) {
        if (expression == null || pattern == null)
            return false;

        if (!caseSensitive) {
            pattern = pattern.toLowerCase();
            expression = expression.toLowerCase();
        }

        pattern = escapeSpecialCharacters(pattern, ".+*$^?|()[]{}");
        pattern = pattern.replaceAll("(?<!\\\\)_", ".");
        pattern = pattern.replaceAll("(?<!\\\\)%", ".*");

        return expression.matches(pattern);
    }

    @SuppressWarnings("SameParameterValue")
    private static String escapeSpecialCharacters(String expression, String specialSymbols) {
        if (expression == null)
            return null;

        if (specialSymbols == null)
            return expression;

        int len = expression.length();
        if (len == 0)
            return "";

        StringBuilder sb = new StringBuilder(len * 2);
        for (int i = 0; i < len; ++i) {
            char c = expression.charAt(i);
            if (specialSymbols.indexOf(c) != -1)
                sb.append("\\");
            sb.append(c);
        }

        return sb.toString();
    }

    /**
     * Searches for a specified pattern in an expression.
     *
     * @param expression - any valid expression of character data type.
     * @param pattern - specific string of characters to search for in {expression},
     *                  and can include the following valid wildcard characters:
     *                  % - Any string of zero or more characters.
     *                  _ (underscore) - Any single character.
     * @return true, if {expression} is match to specified {pattern}, else false.
     */
    public static boolean wildcardMatch(CharSequence expression, CharSequence pattern, boolean caseSensitive) {
        if (expression == null || pattern == null)
            return false;

        int exprPos = 0, exprSubMatchPos = 0;
        int patPos = 0, patSubMatchPos = 0;

        while (!isAtEnd(expression, exprPos)) {
            boolean isQuoted = isWildcardQuoted(pattern, patPos);
            if (isQuoted)
                ++patPos;

            boolean patIsAtEnd = isAtEnd(pattern, patPos);

            if (!patIsAtEnd && !isQuoted && pattern.charAt(patPos) == '%') {
                ++patPos;
                if (isAtEnd(pattern, patPos))
                    return true;

                exprSubMatchPos = exprPos + 1;
                patSubMatchPos = patPos;
            } else if (!patIsAtEnd &&
                    ((!isQuoted && pattern.charAt(patPos) == '_') ||
                            isCharactersEqual(expression.charAt(exprPos), pattern.charAt(patPos), caseSensitive))) {
                ++exprPos;
                ++patPos;
            } else if (exprSubMatchPos == 0) {
                return false;
            } else {
                patPos = patSubMatchPos;
                exprPos = exprSubMatchPos++;
            }
        }

        while (!isAtEnd(pattern, patPos) && pattern.charAt(patPos) == '%')
            ++patPos;

        return isAtEnd(pattern, patPos);
    }

    private static boolean isAtEnd(CharSequence expression, int pos) {
        return pos >= expression.length();
    }

    private static boolean isCharactersEqual(char c1, char c2, boolean caseSensitive) {
        char cc1 = caseSensitive ? c1 : Character.toUpperCase(c1);
        char cc2 = caseSensitive ? c2 : Character.toUpperCase(c2);
        return cc1 == cc2;
    }

    private static boolean isWildcardQuoted(CharSequence expression, int pos) {
        return !isAtEnd(expression, pos) &&
                expression.charAt(pos) == '\\' &&
                pos < expression.length() - 1 &&
                ("_%".indexOf(expression.charAt(pos + 1)) != -1);
    }

}