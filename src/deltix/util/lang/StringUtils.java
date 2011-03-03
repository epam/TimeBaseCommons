package deltix.util.lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.text.DecimalFormat;

public class StringUtils {
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
    }/**
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

    private static void         hex (int n, StringBuilder out) {
        n = n & 0xF;

        if (n < 10)
            out.append ('0' + n);
        else
            out.append ('A' + n);
    }

    public static String        escapeJavaString (CharSequence str) {
        StringBuilder       out = new StringBuilder ();

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
        return (out.toString ());
    }

    /**
     *
     */
    public static String        escapeJavaStringLiteral (CharSequence str) {
        StringBuilder       out = new StringBuilder ();

        out.append ('\"');
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
        out.append ('\"');
        return (out.toString ());
    }

    public static boolean isValidJavaIdentifier(String s) {
        // an empty or null string cannot be a valid identifier
        if (s == null || s.length() == 0) {
            return false;
        }

        char[] c = s.toCharArray();
        if (!Character.isJavaIdentifierStart(c[0])) {
            return false;
        }

        for (int i = 1; i < c.length; i++) {
            if (!Character.isJavaIdentifierPart(c[i])) {
                return false;
            }
        }

        return true;
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

        while (len > 0 && !frac) {
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

    /** @return true if input is empty string or null */
    public static boolean isEmpty (String value) {
        return trim(value) == null;
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

        StringBuffer    buf = new StringBuffer();
        if (strs != null)
            for (int i = 0; i < strs.length; i++) {
                if (i > 0)
                    buf.append (", ");
                buf.append (strs [i]);
            }
        return (buf.toString());
    }

    // adapted from String.indexOf(char[] ...)
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

    public static boolean equals ( String one,
	                                     String two ) {
		if (one == null) {
			return two == null || two.equals ( one );
		}
		return one == two || one.equals ( two );
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

    public static String[] split(String message, String regex,
                                 boolean trimToNull, boolean discardNullOrEmpty) {
        String[] splitted = message.split(regex);
        if (discardNullOrEmpty) {
            List<String> result = new ArrayList<String>(splitted.length);
            for (int i = 0; i < splitted.length; i++) {
                String value = trimToNull ? trim(splitted[i]) : splitted[i];
                if (value == null || value.length() <= 0)
                    continue;
                result.add(value);
            }
            return result.toArray(new String[result.size()]);
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
            int rightPaddingLenght = Math.max(1, maxLength - paddingLength - line.length());
            builder.append(frameSymbol).
                    append(padding).append(line).append(repeat(' ', rightPaddingLenght)).
                    append(frameSymbol).append(lineSeparator);
        }
        builder.append(frameSymbol).append(emptyLine).append(frameSymbol).append(lineSeparator).
                append(topAndBottom).append(lineSeparator);

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
        Map<String, String> replaceMap = new HashMap<String, String>(replacementKeys.size());
        for (String key : replacementKeys)
            replaceMap.put(placeholderPrefix + key + placeholderSuffix, replacements.getProperty(key));

        Set<String> keys = target.stringPropertyNames();
        for (String key : keys) {
            String value = target.getProperty(key);
            // replace placeholders
            String newKey = replace(key, replaceMap);
            String newValue = value != null ? replace(value, replaceMap) : value;
            // remove old entry and put the new one
            target.remove(key);
            target.put(newKey, newValue);
        }
    }

}


