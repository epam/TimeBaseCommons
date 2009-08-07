package deltix.util.text;

/**
 * Faster pragmatic alternative to Java's standard class MessageFormat.
 *
 * PROS: 10x faster, thread safe, consumes less memory,  uses StringBuilder instead of slower StringBuffer.
 * CONS: doesn't support formatting like {2,number,integer} but instead calls String.valueOf() for each argument. Doesn't care about locale (which is good and bad).
 *
 *
 * TODO: If you format the same message over and over with different arguments, we can parse once and cache... 
 * @see java.text.MessageFormat
 */
public class SimpleMessageFormat {

    public static String format (String format, Object []args) {
        final StringBuilder sb = new StringBuilder (256);
        format (sb, format, args);
        return sb.toString();
    }

    public static void format (StringBuilder sb, String format, Object []args) {

           final int len = format.length();

           for (int i=0; i < len; i++) {
               final char ch = format.charAt(i);
               if (ch == '\'') {
                   final int quoteEnd = format.indexOf ('\'', i+1);
                   if (quoteEnd < 0)
                       throw new InvalidFormatException(format, i, "String contains single quote, which is a special escape character. Use '' to print single quote");

                   if (i + 1 == quoteEnd)
                        sb.append ('\'');  // '' represents a single quote
                   else
                        sb.append (format, i+1, quoteEnd);
                   i = quoteEnd;
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

       }

       private static int digits(String format, int start, int end) {
           int result = 0;
           while (start < end) {
               char ch = format.charAt(start);
               if (ch < '0' || ch > '9')
                   throw new InvalidFormatException(format, start, "Argument index contains non-digit character: '" + ch +'\'');
               result = 10*result + (ch - '0');
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
