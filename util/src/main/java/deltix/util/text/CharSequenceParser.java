package deltix.util.text;

/**
 *
 */
public abstract class CharSequenceParser {
    private static final int    INT_MAX_VALUE_DIV_10 =      Integer.MAX_VALUE / 10;
    private static final long   LONG_MAX_VALUE_DIV_10 =     Long.MAX_VALUE / 10;
    private static final long   LONG_SIGN_BIT =             0x8000000000000000L;
    private static final long   DOUBLE_BIAS_EXP =           1023;
    private static final long   DOUBLE_MANTISSA_WIDTH =     52;
    private static final long   DOUBLE_ASSUMED_BIT =        1L << DOUBLE_MANTISSA_WIDTH;
    private static final long   DOUBLE_MANTISSA_BITMASK =   DOUBLE_ASSUMED_BIT - 1;
    private static final long   DOUBLE_NORM_EXP =           DOUBLE_BIAS_EXP + DOUBLE_MANTISSA_WIDTH;
    private static final long   DOUBLE_OVERFLOW_BITMASK =   ~DOUBLE_MANTISSA_BITMASK - DOUBLE_ASSUMED_BIT;
    private static final int    INT_SIGN_BIT =              0x80000000;
    private static final int    FLOAT_BIAS_EXP =            127;
    private static final int    FLOAT_MANTISSA_WIDTH =      23;
    private static final int    FLOAT_ASSUMED_BIT =         1 << FLOAT_MANTISSA_WIDTH;
    private static final int    FLOAT_MANTISSA_BITMASK =    FLOAT_ASSUMED_BIT - 1;
    private static final int    FLOAT_NORM_EXP =            FLOAT_BIAS_EXP + FLOAT_MANTISSA_WIDTH;
    private static final int    FLOAT_OVERFLOW_BITMASK =    ~FLOAT_MANTISSA_BITMASK - FLOAT_ASSUMED_BIT;

    private static final double[] SMALL_POWERS_OF_10 = {
            1e0,  1e1,  1e2,  1e3,  1e4,  1e5,  1e6,  1e7,  1e8,  1e9,
            1e10, 1e11, 1e12, 1e13, 1e14, 1e15, 1e16, 1e17, 1e18, 1e19,
            1e20, 1e21, 1e22
    };

    private static final double[] BIG_POWERS_OF_10 = {
            1e16, 1e32, 1e64, 1e128, 1e256
    };

    public static boolean parseBoolean ( CharSequence sc ) {
		if ("true".contentEquals ( sc )) {
			return true;
		} else if ("false".contentEquals ( sc )) {
			return false;
		}
		throw new IllegalArgumentException ( String.valueOf ( sc ) );
	}

    public static byte  parseByte(CharSequence sc) {
        return (parseByte(sc, 0, sc.length ()));
    }
    
    public static byte  parseByte(CharSequence sc, final int startIncl, final int endExcl) {
        final int result = parseInt(sc, startIncl, endExcl);
        if (result < Byte.MIN_VALUE || result > Byte.MAX_VALUE)
            throw new NumberFormatException ("Value out of range: " + result);
        else
            return (byte) result;
    }

    public static short parseShort(CharSequence sc) {
        return (parseShort(sc, 0, sc.length ()));
    }

    public static short parseShort(CharSequence sc, final int startIncl, final int endExcl) {
        final int result = parseInt(sc, startIncl, endExcl);
        if (result < Short.MIN_VALUE || result > Short.MAX_VALUE)
            throw new NumberFormatException ("Value out of range: " + result);
        else
            return (short) result;
    }

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
            
            checkNotAtEnd (pos, endExcl, sc, startIncl);
            
            ch = sc.charAt (pos);
        }
        
        for (;;) {
            if (ch != ',') {
                int             digit = ch - '0';

                if (digit < 0 || digit > 9)
                    throw new NumberFormatException (
                        "Illegal digit at position " + (pos + 1) + " in: " + sc.subSequence (startIncl, endExcl).toString ()
                    );

                if (value < -INT_MAX_VALUE_DIV_10)
                    throw new NumberFormatException ("Integer (4-byte) too large: " + sc);

                value = value * 10 - digit;

                if (value > 0)  // Overflow
                    throw new NumberFormatException ("Integer (4-byte) too large: " + sc);
            }

            pos++;
            
            if (pos == endExcl) {
                if (negative)
                    return (value);
                else if (value == Integer.MIN_VALUE)
                    throw new NumberFormatException ("Integer (4-byte) too large: " + sc);
                else
                    return (-value);
            }
            
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
            
            checkNotAtEnd (pos, endExcl, sc, startIncl);
            
            ch = sc.charAt (pos);
        }
        
        for (;;) {
            if (ch != ',') {
                int             digit = ch - '0';

                if (digit < 0 || digit > 9)
                    throw new NumberFormatException (
                        "Illegal digit at position " + (pos + 1) + " in: " + sc.subSequence (startIncl, endExcl).toString ());

                if (value < -LONG_MAX_VALUE_DIV_10)
                    throw new NumberFormatException ("Long integer (8-byte) too large: " + sc);

                value = value * 10 - digit;

                if (value > 0)  // Overflow
                    throw new NumberFormatException ("Long integer (8-byte) too large: " + sc);
            }
            
            pos++;
            
            if (pos == endExcl) {
                if (negative)
                    return (value);
                else if (value == Long.MIN_VALUE)
                    throw new NumberFormatException ("Long integer (8-byte) too large: " + sc);
                else
                    return (-value);
            }
            
            ch = sc.charAt (pos);
        }                
    }
    
    public static double  parseDouble (CharSequence sc) {
        return (parseDouble (sc, 0, sc.length ()));
    }
    
    public static double  parseDouble (final CharSequence sc, final int startIncl, final int endExcl) {
        if (startIncl > endExcl)
            throw new IllegalArgumentException ("Illegal range: " + startIncl + ".." + endExcl);
        
        if (startIncl == endExcl)
            throw new NumberFormatException ("Empty string");
        
        int                 pos = startIncl;
        long                numerator = 0;        
        double              denominator = 1;
        long                sign = 0;
        boolean             dotSeen = false;
        boolean             overflow = false;
        char                ch = sc.charAt (pos);
        
        if (ch == '+' || ch == '-') {
            if (ch == '-')
                sign = LONG_SIGN_BIT;
            
            pos++;
            
            checkNotAtEnd (pos, endExcl, sc, startIncl);
            
            ch = sc.charAt (pos);
        }
        
        for (;;) {
            if (ch != ',') {
                if (!dotSeen && ch == '.')
                    dotSeen = true;
                else if (ch == 'e' || ch == 'E') {
                    pos++;
                    checkNotAtEnd (pos, endExcl, sc, startIncl);
                    ch = sc.charAt (pos);
                    boolean     negativeExp = false;
                    if (ch == '-') {
                        pos++;
                        negativeExp = true;
                    } else if (ch == '+') {
                        pos++;
                    }

                    checkNotAtEnd (pos, endExcl, sc, startIncl);
                    int     exp = parseInt (sc, pos, endExcl);

                    double powerOf10 = pow10(exp);
                    if (negativeExp) {
                        denominator *= powerOf10;
                    } else {
                        denominator /= powerOf10;
                    }
                    break;
                }
                else {
                    final int       digit = ch - '0';
                    if (digit < 0 || digit > 9) {
                        if (pos == startIncl || (pos == startIncl + 1 && (sc.charAt(startIncl) == '+' || sc.charAt(startIncl) == '-'))) {
                            if (matchesAt(sc, pos, endExcl, "Infinity")) {
                                return sign == 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
                            }
                            else if (matchesAt(sc, pos, endExcl, "Inf")) {
                                return sign == 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
                            }
                            else if (matchesAt(sc, pos, endExcl, "NaN")) {
                                return Double.NaN;
                            }
                        }
                        throw new NumberFormatException (
                                "Illegal digit at position " + (pos + 1) + " in: " + sc.subSequence (startIncl, endExcl));
                    }

                    if (overflow) {
                        //  Stop shifting the numerator
                        if (!dotSeen)
                            denominator *= 0.1;
                    }
                    else {
                        numerator = numerator * 10 + digit;

                        if (dotSeen)
                            denominator *= 10;

                        if (numerator >= DOUBLE_ASSUMED_BIT)
                            overflow = true;
                    }
                }
            }
            pos++;
            
            if (pos == endExcl)
                break;
            
            ch = sc.charAt (pos);
        }   
        
        if (numerator == 0){
            if (denominator != 1) {
                throw new NumberFormatException("Illegal number format: " + sc.subSequence (startIncl, endExcl));
            }
            return 0.0;
        }
        // Build the double first, ignoring the denominator
        long    exp = DOUBLE_NORM_EXP;        
        
        if (overflow) 
            while ((numerator & DOUBLE_OVERFLOW_BITMASK) != 0) {
                exp++;
                numerator >>>= 1;
            }  
        else
            while ((numerator & DOUBLE_ASSUMED_BIT) == 0) {
                exp--;
                numerator <<= 1;
            }                  
        
        numerator &= DOUBLE_MANTISSA_BITMASK;
        
        final long      bits = sign | (exp << DOUBLE_MANTISSA_WIDTH) | numerator;
        double          result = Double.longBitsToDouble (bits);

        if (denominator != 1)
            result /= denominator;

        return (result);
    }

    private static boolean matchesAt(CharSequence sc, int start, int end, String target) {
        if (end - start != target.length())
            return false;

        for (int i = 0; i < target.length(); i++) {
            if (sc.charAt(start + i) != target.charAt(i))
                return false;
        }
        return true;
    }

    private static void checkNotAtEnd (int pos, final int endExcl, final CharSequence sc, final int startIncl)
        throws NumberFormatException 
    {
        if (pos == endExcl)
            throw new NumberFormatException (
                "Unexpected end of text at position " + pos + ": " + 
                sc.subSequence (startIncl, endExcl).toString ()
            );        
    }

    public static float     parseFloat (CharSequence sc) {
        return (parseFloat (sc, 0, sc.length ()));
    }
    
    public static float     parseFloat (final CharSequence sc, final int startIncl, final int endExcl) {
        if (startIncl > endExcl)
            throw new IllegalArgumentException ("Illegal range: " + startIncl + ".." + endExcl);
        
        if (startIncl == endExcl)
            throw new NumberFormatException ("Empty string");
        
        int                 pos = startIncl;
        int                 numerator = 0;        
        double              denominator = 1;
        int                 sign = 0;
        boolean             dotSeen = false;
        boolean             overflow = false;
        char                ch = sc.charAt (pos);
        
        if (ch == '+' || ch == '-') {
            if (ch == '-')
                sign = INT_SIGN_BIT;
            
            pos++;
            
            checkNotAtEnd (pos, endExcl, sc, startIncl);
            
            ch = sc.charAt (pos);
        }
        
        for (;;) {
            if (ch != ',') {
                if (!dotSeen && ch == '.')
                    dotSeen = true;
                else if (ch == 'e' || ch == 'E') {
                    pos++;
                    checkNotAtEnd (pos, endExcl, sc, startIncl);
                    ch = sc.charAt (pos);
                    boolean     negativeExp = false;

                    if (ch == '-') {
                        pos++;
                        negativeExp = true;
                    }
                    else if (ch == '+')
                        pos++;

                    checkNotAtEnd (pos, endExcl, sc, startIncl);

                    int     exp = parseInt (sc, pos, endExcl);

                    double powerOf10 = pow10(exp);
                    if (negativeExp) {
                        denominator *= powerOf10;
                    } else {
                        denominator /= powerOf10;
                    }
                    break;
                }
                else {
                    final int       digit = ch - '0';

                    if (digit < 0 || digit > 9) {
                        if (pos == startIncl || (pos == startIncl + 1 && (sc.charAt(startIncl) == '+' || sc.charAt(startIncl) == '-'))) {
                            if (matchesAt(sc, pos, endExcl, "Infinity")) {
                                return sign == 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
                            }
                            else if (matchesAt(sc, pos, endExcl, "Inf")) {
                                return sign == 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
                            }
                            else if (matchesAt(sc, pos, endExcl, "NaN")) {
                                return Float.NaN;
                            }
                        }

                        throw new NumberFormatException (
                            "Illegal digit at position " + (pos + 1) + " in: " + 
                            sc.subSequence (startIncl, endExcl).toString ()
                        );
                    }

                    if (overflow) {
                        //  Stop shifting the numerator
                        if (!dotSeen)
                            denominator *= 0.1;
                    }
                    else {
                        numerator = numerator * 10 + digit;

                        if (dotSeen)
                            denominator *= 10;

                        if (numerator >= FLOAT_ASSUMED_BIT)
                            overflow = true;
                    }
                }
            }

            pos++;
            
            if (pos == endExcl)
                break;
            
            ch = sc.charAt (pos);
        }   
        
        if (numerator == 0) {
            if (denominator != 1) {
                throw new NumberFormatException("Illegal number format: " + sc.subSequence (startIncl, endExcl));
            }
            return (0.0F);
        }

        // Build the double first, ignoring the denominator
        int    exp = FLOAT_NORM_EXP;        
        
        if (overflow) 
            while ((numerator & FLOAT_OVERFLOW_BITMASK) != 0) {
                exp++;
                numerator >>>= 1;
            }  
        else
            while ((numerator & FLOAT_ASSUMED_BIT) == 0) {
                exp--;
                numerator <<= 1;
            }                  
        
        numerator &= FLOAT_MANTISSA_BITMASK;
        
        final int       bits = sign | (exp << FLOAT_MANTISSA_WIDTH) | numerator;
        float           result = Float.intBitsToFloat (bits);
            
        if (denominator != 1)
            result /= denominator;
        
        return (result);
    }

    private static double pow10(int exp) {
        if (exp < 0) {
            return 1.0 / pow10(-exp);
        }
        if (exp < SMALL_POWERS_OF_10.length) {
            return SMALL_POWERS_OF_10[exp];
        }

        double result = 1.0;
        int bitMask = 1;
        for (int i = 0; i < BIG_POWERS_OF_10.length; i++) {
            if ((exp & (bitMask << (i + 4))) != 0) {
                result *= BIG_POWERS_OF_10[i];
            }
        }
        result *= SMALL_POWERS_OF_10[exp & 0xF];
        return result;
    }
    public static void main (String [] args) {
        System.out.println (parseDouble (args [0]));
    }
}
