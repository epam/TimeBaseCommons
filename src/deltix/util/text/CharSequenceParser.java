package deltix.util.text;

/**
 *
 */
public abstract class CharSequenceParser {
    public static int   parseInt (CharSequence sc) {
        return (parseInt (sc, 0, sc.length ()));
    }
    
    public static int   parseInt (final CharSequence sc, final int startIncl, final int endExcl) {
        if (startIncl > endExcl)
            throw new IllegalArgumentException ("Illegal range: " + startIncl + ".." + endExcl);
        
        if (startIncl == endExcl)
            throw new NumberFormatException ("Empty string");
        
        int                 pos = startIncl;
        int                 value = 0;
        boolean             negative = false;
        char                ch = sc.charAt (pos);
        
        if (ch == '+' || ch == '-') {
            negative = ch == '-';
            pos++;
            
            if (pos == endExcl)
                throw new NumberFormatException (sc.subSequence (startIncl, endExcl).toString ());
            
            ch = sc.charAt (pos);
        }
        
        for (;;) {
            int             digit = ch - '0';
            
            if (digit < 0 || digit > 9)
                throw new NumberFormatException (
                    "Illegal digit at position " + (pos + 1) + " in: " + sc.subSequence (startIncl, endExcl).toString ());
            
            value = value * 10 + digit;
            
            pos++;
            
            if (pos == endExcl)
                return (negative ? -value : value);
            
            ch = sc.charAt (pos);
        }                
    }
    
    public static long  parseLong (CharSequence sc) {
        return (parseLong (sc, 0, sc.length ()));
    }
    
    public static long  parseLong (final CharSequence sc, final int startIncl, final int endExcl) {
        if (startIncl > endExcl)
            throw new IllegalArgumentException ("Illegal range: " + startIncl + ".." + endExcl);
        
        if (startIncl == endExcl)
            throw new NumberFormatException ("Empty string");
        
        int                 pos = startIncl;
        long                value = 0;
        boolean             negative = false;
        char                ch = sc.charAt (pos);
        
        if (ch == '+' || ch == '-') {
            negative = ch == '-';
            pos++;
            
            if (pos == endExcl)
                throw new NumberFormatException (sc.subSequence (startIncl, endExcl).toString ());
            
            ch = sc.charAt (pos);
        }
        
        for (;;) {
            int             digit = ch - '0';
            
            if (digit < 0 || digit > 9)
                throw new NumberFormatException (
                    "Illegal digit at position " + (pos + 1) + " in: " + sc.subSequence (startIncl, endExcl).toString ());
            
            value = value * 10 + digit;
            
            pos++;
            
            if (pos == endExcl)
                return (negative ? -value : value);
            
            ch = sc.charAt (pos);
        }                
    }
    
    public static double  parseDecimal (CharSequence sc) {
        return (parseDecimal (sc, 0, sc.length ()));
    }
    
    private static final long    BIAS_EXP = 1023;
    private static final long    NORM_EXP = BIAS_EXP + 52;
    
    public static double  parseDecimal (final CharSequence sc, final int startIncl, final int endExcl) {
        if (startIncl > endExcl)
            throw new IllegalArgumentException ("Illegal range: " + startIncl + ".." + endExcl);
        
        if (startIncl == endExcl)
            throw new NumberFormatException ("Empty string");
        
        int                 pos = startIncl;
        long                numerator = 0;        
        long                denominator = 1;        
        long                sign = 0;
        boolean             dotSeen = false;
        char                ch = sc.charAt (pos);
        
        if (ch == '+' || ch == '-') {
            if (ch == '-')
                sign = 0x8000000000000000L;
            
            pos++;
            
            if (pos == endExcl)
                throw new NumberFormatException (sc.subSequence (startIncl, endExcl).toString ());
            
            ch = sc.charAt (pos);
        }
        
        for (;;) {
            if (!dotSeen && ch == '.') 
                dotSeen = true;
            else {            
                final int       digit = ch - '0';

                if (digit < 0 || digit > 9)
                    throw new NumberFormatException (
                        "Illegal digit at position " + (pos + 1) + " in: " + sc.subSequence (startIncl, endExcl).toString ());

                numerator = numerator * 10 + digit;

                if (dotSeen)
                    denominator *= 10;
            }
            
            pos++;
            
            if (pos == endExcl)
                break;
            
            ch = sc.charAt (pos);
        }   
        
        if (numerator == 0)
            return (0.0);                
        
        // Build the double first, ignoring the denominator
        long    exp = NORM_EXP;        
        
        if (numerator > 0x10000000000000L) 
            while ((numerator & 0xFFE0000000000000L) != 0) {
                exp++;
                numerator >>>= 1;
            }  
        else
            while ((numerator & 0x10000000000000L) == 0) {
                exp--;
                numerator <<= 1;
            }                  
        
        numerator &= 0xFFFFFFFFFFFFFL;
        
        final long    bits = sign | (exp << 52) | numerator;
        double  result = Double.longBitsToDouble (bits);
            
        if (denominator != 1)
            result /= denominator;
        
        return (result);
    }
        
    public static void main (String [] args) {
        System.out.println (parseDecimal (args [0]));
    }
}
