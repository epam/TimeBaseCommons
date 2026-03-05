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
package com.epam.deltix.util.text;

import com.epam.deltix.util.lang.Util;

import java.math.BigDecimal;

/**
 * Fast thread-safe method of formatting floating point number with given precision.
 * Primary use: UHF trading adapter where we need to format price fields with fixed precision (usually 3 decimal places)
 * and sometimes limited to certain number of digits (e.g. CME requires price field to be less than 15 characters).
 *
 * For large numbers this method falls back to BigDecimal.toPlainString().
 *
 * Note: formatter always uses '.' dot as decimal separator (regardless of current locale).
 *
 * @author Andy
 */
public class DecimalFormatter {

    private static final char [] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    public static final int MAX_WIDTH = 21;
    public static final int MAX_PRECISION = 15;
    private static final long MAX = Long.MAX_VALUE / 10;

    private static final int DEFAULT_DECIMAL_FORMATTER_PRECISION = Util.getIntSystemProperty("UHF.defaultDecimalFormatPrecision", 2, 0, 100);
    public static final DecimalFormatter DEFAULT = new DecimalFormatter(DEFAULT_DECIMAL_FORMATTER_PRECISION);


    private final int maxLength;
    private final int precision;
    private final long factor;

    public DecimalFormatter (int precision) {
        this (precision, MAX_WIDTH);
    }

    /**
     * @param precision maximum number of digits after decimal point (e.g. 3). Truncated part will be rounded.
     * @param maxLength maximum length a whole string should take (e.g. 16).
     */
    public DecimalFormatter (int precision, int maxLength) {
        if (precision < 0 || precision > MAX_PRECISION)
            throw new IllegalArgumentException("Precision");
        if (maxLength < 0 || maxLength > MAX_WIDTH)
            throw new IllegalArgumentException("Length");
        this.precision = precision;
        this.maxLength = maxLength;
        this.factor = Math.round(Math.pow(10, precision));
    }

    public String format (double number) {
        return format(number, precision, maxLength, factor);
    }

    public static String format (double number, int precision) {
        return format(number, precision, MAX_WIDTH);
    }

    /**
     * @param precision maximum number of digits after decimal point (e.g. 3). Truncated part will be rounded.
     * @param maxLength maximum length a whole string should take (e.g. 16).
     */
    public static String format (double number, int precision, int maxLength) {
        long factor = 1;
        for (int i = 0; i < precision; i++)
            factor*= 10;
        return format(number, precision, maxLength, factor);
    }

    private static String format (double number, int precision, int maxLength, long factor) {
        if (precision < 0 || precision > MAX_PRECISION)
            throw new IllegalArgumentException("Precision");
        if (maxLength < 0 || maxLength > MAX_WIDTH)
            throw new IllegalArgumentException("Length");
        if (Double.isNaN(number)) // Infinity will be checked a bit later
            throw new IllegalArgumentException("NaN");

        boolean sign = false;
        double factoredNumber = number;
        if (number < 0) {
            sign = true;
            factoredNumber = Math.abs(number);
        }
        factoredNumber = factoredNumber*factor;
        if (Double.isInfinite(factoredNumber) || factoredNumber > MAX)
            return formatLargeNumber (number);


        //long numberAsDecimal = Math.round(factoredNumber);  // this call costs 8% of total time we spend in this method on avg.
        long numberAsDecimal;
        {
            factoredNumber = factoredNumber * 10;
            numberAsDecimal = (long) factoredNumber;
            int smallestDigit = (int) (numberAsDecimal % 10);
            numberAsDecimal = numberAsDecimal / 10;
            if (smallestDigit >=5 )
                numberAsDecimal ++; // round up;
        }
        if (numberAsDecimal == 0)
            return "0";

        char [] buffer = new char [MAX_WIDTH];
        int i = MAX_WIDTH;
        int fractional_part = precision;

        boolean needLeadingZero = (fractional_part > 0);
        while (numberAsDecimal > 0) {
            int digit = (int) (numberAsDecimal % 10);
            if (digit != 0 || i != MAX_WIDTH || fractional_part == 0) // skip trailing zeros
                buffer [--i] = DIGITS [digit];

            numberAsDecimal = numberAsDecimal / 10;
            if (fractional_part > 0) {
                fractional_part --;
                if (fractional_part == 0) {
                    if (i != MAX_WIDTH)
                        buffer [--i] = '.';
                    needLeadingZero = (numberAsDecimal == 0);
                }
            }
        }
        if (needLeadingZero) {
            if (fractional_part > 0) { // if number was less than zero
                while (fractional_part-- > 0)
                    buffer [--i] = '0';
                buffer [--i] = '.';
            }
            buffer [--i] = '0';
        }

        if (sign)
            buffer [--i] = '-';

        int len = Math.min(MAX_WIDTH - i, maxLength);
        return new String (buffer, i, len);
    }

    // Super Rarely used to represent prices
    private static String formatLargeNumber(double number) {
        if (Double.isNaN(number))
            return "NaN";

        if (Double.isInfinite(number))
            return (number > 0) ? "Infinity" : "-Infinity";

        return new BigDecimal(number).toPlainString();
    }

}