package com.epam.deltix.util.text;

import java.io.IOException;

import com.epam.deltix.util.lang.Util;

/**
 * Faster pragmatic alternative to Java's standard class MessageFormat.
 * <ul>
 * <li>PROS: 10x faster, thread safe, consumes less memory,  uses StringBuilder instead of slower StringBuffer.</li>
 * <li>CONS: doesn't support formatting like {2,number,integer} but instead calls String.valueOf() for each argument. Doesn't care about locale (which is good and bad).</li>
 * </ul>
 *  This simplified variation of Java Message format. Arguments can be referred by index using '{index}' notation.
 *  For example:
 *  <pre>
 *      log (ErrorLevel.DEBUG, "Hello {0} {1}", firstName, lastName);
 *  </pre>
 * If you want to use left figure bracket in your output string wrap it in single quotes ('). For example:
 *  <pre>
 *      log (ErrorLevel.DEBUG, "Here goes left figure bracket '{' that needs quotes and right bracket } that doesn't need escaping", ...);
 *  </pre>
 * If your want to use single quote, specify it twice (''). Double quotes (") character does not need any escaping.
 *
 * @see java.text.MessageFormat
 */
public class SimpleMessageFormat {

    public static String safeFormat (String format, Object[] args) {
        try {
            if (args == null || args.length == 0)
                return format;
            return format(format, args);
        } catch (InvalidFormatException e) {
            return e.getMessage();
        }
    }

    public static String format (String format, Object []args) {
        final StringBuilder sb = new StringBuilder (256);
        format (sb, format, args);
        return sb.toString();
    }

    public static void format (Appendable sb, String format, Object []args) {
        try {
            final int len = format.length();

            for (int i=0; i < len; i++) {
                final char ch = format.charAt(i);
                if (ch == '\'') {
                    final int quoteEnd = format.indexOf ('\'', i+1);
                    if (quoteEnd < 0) {
                        sb.append('\''); // this is just a single quote
                    } else {
                        if (i + 1 == quoteEnd)
                             sb.append ('\'');  // '' represents a single quote
                        else
                             sb.append (format, i+1, quoteEnd);
                        i = quoteEnd;
                    }
                } else
                if (ch == '{') {
                    int bracesEnd = format.indexOf('}', i+1);
                    if (bracesEnd < 0)
                        throw new InvalidFormatException(format, i, "Missing right brace '}'");

                    if (i + 1 == bracesEnd)
                        throw new InvalidFormatException(format, i, "Missing argument number inside braces {}");

                    final int argIndex = digits(format, i+1, bracesEnd);
                    if (argIndex > args.length-1)
                        throw new InvalidFormatException(format, i, "Formatting string refers to non-existing argument #" + argIndex + " when only " + args.length + " arguments are passed");

                    sb.append (String.valueOf (args[argIndex]));
                    i = bracesEnd;
                } else {
                    sb.append (ch);
                }
            }
        } catch (IOException e) {
            throw Util.asRuntimeException(e); // you will never see
        }
    }

    private static int digits(String format, int start, int end) {
        int result = 0;
        while (start < end) {
            char ch = format.charAt(start);
            if (ch < '0' || ch > '9')
                throw new InvalidFormatException(format, start, "Argument index contains non-digit character: '" + ch + '\'');
            result = 10 * result + (ch - '0');
            start++;
        }
        return result;
    }


    public static final class InvalidFormatException extends IllegalArgumentException {
         public InvalidFormatException (String format, int pos, String error) {
             super ("Format error at position " + pos + ": " + error + ". Format string: \"" + format + '\"');
         }
    }
}
