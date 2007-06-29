package deltix.util.lang;

import java.util.StringTokenizer;

public class StringUtils {
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
    
    public static void main (String [] args) {
        byte []     b = args [0].getBytes();

        System.out.println (parseDecimalLong (b, 0, b.length));
    }


}


