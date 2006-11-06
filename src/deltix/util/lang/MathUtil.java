package deltix.util.lang;

public class MathUtil {
    public static final double  TWO_PI = Math.PI * 2;
    
    /** 
     *	Returns 1 if a && !b, -1 if !a && b, 0 if a == b.
     */
    public static int		sign (boolean a, boolean b) {
        return (a ? (b ? 0 : 1) : (b ? -1 : 0));
    }
    
    /** 
     *	Returns 1, -1, or 0 if the argument is 
     *	greater, less than, or equal to 0 respectively.
     */
    public static int		sign (double x) {
        return (x > 0 ? 1 : x < 0 ? -1 : 0);
    }
    
    /** 
     *	Returns 1, -1, or 0 if the argument is 
     *	greater, less than, or equal to 0 respectively.
     */
    public static int 		sign (float x) {
        return (x > 0 ? 1 : x < 0 ? -1 : 0);
    }
    
    /** 
     *	Returns 1, -1, or 0 if the argument is 
     *	greater, less than, or equal to 0 respectively.
     */
    public static int		sign (int x) {
        return (x > 0 ? 1 : x < 0 ? -1 : 0);
    }
    
    /** 
     *	Returns 1, -1, or 0 if the argument is 
     *	greater, less than, or equal to 0 respectively.
     */
    public static int		sign (long x) {
        return (x > 0 ? 1 : x < 0 ? -1 : 0);
    }
    
    /** 
     *	Returns the fractional part of the argument. Result is negative if 
     *	the argument is negative.
     */
    public static float		frac (float x) {
    	return (x - (int) x);
    }
    
    /** 
     *	Returns the fractional part of the argument. Result is negative if 
     *	the argument is negative.
     */
    public static double	frac (double x) {
    	return (x - (int) x);
    }
    
    /** 
     *	Returns the ceiling of log<sub>2</sub> of the argument, i.e.
     *	the smallest integer k such that 2<sup>k</sup> >= v.
     */
    public static int		log2 (int v) {
    	int		ret = 0;
    	int             cmp = 1;
        
    	while (v > cmp) {
            ret++;
            cmp <<= 1;
    	}
    	
    	return (ret);
    }
    
    /**
     *  Normalize the angle so that it is whithin the range of (-PI .. PI].
     */
    public static double    normalizeAnglePlusMinusPi (double a) {
        if (a <= -Math.PI)
            do {
                a += TWO_PI;
            } while (a <= -Math.PI);
        else
            while (a > TWO_PI)
                a -= TWO_PI;
        
        return (a);
    }
    
    /**
     *  Normalize the angle so that it is whithin the range of [0 .. 2*PI).
     */
    public static double    normalizeAngleZeroTwoPi (double a) {
        if (a < 0)
            do {
                a += TWO_PI;
            } while (a < 0);
        else
            while (a >= TWO_PI)
                a -= TWO_PI;
        
        return (a);
    }
}
