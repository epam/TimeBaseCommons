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

import java.math.*;
import java.util.concurrent.atomic.*;

public class MathUtil {
    public static final double  TWO_PI = Math.PI * 2;

    /**
     *	Returns 1 if a &amp;&amp; !b, -1 if !a &amp;&amp; b, 0 if a == b.
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
     *	Returns 1, -1, or 0 depending on the difference between arguments.
     */
    public static int		compare (int x, int y) {
        return (x > y ? 1 : x < y ? -1 : 0);
    }

    /**
     *	Returns 1, -1, or 0 depending on the difference between arguments.
     */
    public static int		compare (float x, float y) {
        return (x > y ? 1 : x < y ? -1 : 0);
    }

    /**
     *	Returns 1, -1, or 0 depending on the difference between arguments.
     */
    public static int		compare (double x, double y) {
        return (x > y ? 1 : x < y ? -1 : 0);
    }

    /**
     *	Returns 1, -1, or 0 depending on the difference between arguments.
     */
    public static int		compare (long x, long y) {
        return (x > y ? 1 : x < y ? -1 : 0);
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
     *	the smallest integer k such that 2<sup>k</sup> &gt;= v.
     */
    public static int		log2 (int v) {
    	int             ret = 0;
    	int             cmp = 1;

    	while (v > cmp) {
            ret++;
            cmp <<= 1;
    	}

    	return (ret);
    }

    /**
     *	Returns the ceiling of log<sub>2</sub> of the argument, i.e.
     *	the smallest integer k such that 2<sup>k</sup> &gt;= v.
     */
    public static int		log2 (long v) {
    	int             ret = 0;
    	long            cmp = 1;

    	while (v > cmp) {
            ret++;
            cmp <<= 1;
    	}

    	return (ret);
    }

    /**
     *	Returns the number of bits set in the specified long.
     */
    public static int		countSetBits (long v) {
    	int             ret = 0;

    	while (v != 0) {
            if ((v & 1) != 0)
                ret++;

            v >>>= 1;
    	}

    	return (ret);
    }

    /**
     *	Returns the number of bits set in the specified int.
     */
    public static int		countSetBits (int v) {
    	int             ret = 0;
    	
    	while (v != 0) {
            if ((v & 1) != 0)
                ret++;
            
            v >>>= 1;
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

    private static final double inv_sqrt_twopi = 1.0 / Math.sqrt (2 * Math.PI);

    /**
     *  Cumulative Standard Normal Distribution function
     */
    public static double    cumulativeStdNormalDistribution (double X) {
        double          L = Math.abs (X);
        double          K = 1.0 / (1.0 + 0.2316419 * L);
        double          K2 = K * K;
        double          K3 = K2 * K;
        double          K4 = K2 * K2;
        double          K5 = K3 * K2;
        final double    poly =
            (0.31938153 * K
             - 0.356563782 * K2
             + 1.781477937 * K3
             - 1.821255978 * K4
             + 1.330274429 * K5);

        double  dCND = inv_sqrt_twopi * Math.exp (-L * L / 2.0) * poly;
        return (X >= 0) ? 1.0 - dCND : dCND;
    }

    public static Number    negate (Number n) {
        if (n instanceof Integer)
            return (new Integer (-n.intValue ()));
        
        if (n instanceof Long)
            return (new Long (-n.longValue ()));
        
        if (n instanceof Double)
            return (new Double (-n.doubleValue ()));
        
        if (n instanceof Float)
            return (new Float (-n.floatValue ()));
        
        if (n instanceof Byte)
            return (new Byte ((byte) -n.byteValue ()));
        
        if (n instanceof Short)
            return (new Short ((short) -n.shortValue ()));
        
        if (n instanceof BigDecimal)
            return (((BigDecimal) n).negate ());
        
        if (n instanceof BigInteger)
            return (((BigInteger) n).negate ());
        
        if (n instanceof AtomicInteger)
            return (new AtomicInteger (-n.intValue ()));
        
        if (n instanceof AtomicLong)
            return (new AtomicLong (-n.longValue ()));
        
        if (n == null)
            return (null);
        
        throw new UnsupportedOperationException (String.valueOf (n));                        
    }
    
    public static void main (String [] args) {
        for (double k = -5; k <= 5; k+= 0.5)
            System.out.println (cumulativeStdNormalDistribution (k));
    }
}