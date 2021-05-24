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
package com.epam.deltix.util.time;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;
import com.epam.deltix.util.text.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

/**
 * Represents an interval (also known as time span in some systems), which
 * can be added to, or subtracted from, a date. This class is immutable.
 */
@XmlJavaTypeAdapter (Interval.StringIntervalAdapter.class)
public abstract class Interval implements Serializable {
    public static class StringIntervalAdapter extends XmlAdapter<String, Interval> {
        public Interval unmarshal(String v) throws Exception {
            return Interval.valueOf(v);
        }

        public String marshal(Interval v) throws Exception {
            return v.toString();
        }
    }
    
    private static final Log LOGGER = LogFactory.getLog(Interval.class.getName());


    public static final Interval    ZERO = null;
    public static final Interval DAY = Interval.valueOf("1D");
    public static final Interval HOUR = Interval.valueOf("1H");
    public static final Interval MINUTE = Interval.valueOf("1I");
    public static final Interval SECOND = Interval.valueOf("1S");

    protected Interval () {
    }

    public static Interval          create (long num, TimeUnit unit) {
        if (unit.isFixedSize ())
            return (new FixedInterval (num, unit));
        else {
            int     intNum = (int) num;

            if (intNum != num)
                throw new IllegalArgumentException (num + ": too large");

            return (new MonthlyInterval (intNum, unit));
        }
    }

    /**
     * Parse a QQL string.
     * @param text  The QQL representation of the interval.
     */
    public static Interval          valueOf (String text) {
        return (valueOf ((CharSequence) text));
    }

    /**
     * Parse a QQL string.
     * @param text  The QQL representation of the interval.
     */
    public static Interval          valueOf (CharSequence text) {
        if (text == null)
            return (null);
        return valueOf(text, 0, text.length());
    }

    public static Interval          valueOf (CharSequence text, int start, int end) {
        if (text == null)
            return (null);

        if (end == 0)
            return null;

        if (end - start < 2)
            throw new IllegalArgumentException ("Interval must consist of at least two characters. For example: \"1D\" or \"5Y\", got: " + text.toString ());

        long        num = CharSequenceParser.parseLong (text, start, end - 1);
        TimeUnit    unit = TimeUnit.fromSuffix (text.charAt (end - 1));

        return (create (num, unit));
    }

    /**
     * Returns whether this interval is zero.
     */
    public final boolean            isZero () {
        return (getNumUnits () == 0);
    }
    
    /**
     * Returns whether this interval is positive.
     */
    public final boolean            isPositive () {
        return (getNumUnits () > 0);
    }
    
    /**
     * Returns whether this interval is negative.
     */
    public final boolean            isNegative () {
        return (getNumUnits () < 0);
    }
    
    /**
     * Returns the negated value of this Interval.
     */
    public abstract Interval        negate ();
    
    /**
     *  Returns the underlying time unit.
     */    
    public abstract TimeUnit        getUnit ();
    
    /**
     *  Returns the duration of this interval in in units
     *  returned by {@link #getUnit}. Result can be negative.
     * 
     *  @see #getUnit
     */
    public abstract long            getNumUnits ();

     /**
     *  Returns the duration of this interval in in given units.
     *  Result can be negative.       
     */
    public abstract long            getNumUnits (TimeUnit unit);

    /**
     *  Returns the short representation of this interval, such as
     *  <tt>-4Q</tt>
     */
    @Override
    public String                   toString () {
        StringBuilder   sb = new StringBuilder ();
        sb.append (getNumUnits ());
        sb.append (getUnit ().getSuffix ());
        return (sb.toString ());
    }

    public String toHumanString() {
        StringBuilder   sb = new StringBuilder ();
        sb.append (getNumUnits ());
        sb.append (' ');
        sb.append (getUnit ().name ());
        return (sb.toString ());
    }


    public long toMilliseconds() {
        return toMilliseconds(this);
    }

    public static long toMilliseconds(Interval interval) {
        if (interval.getUnit().isVariableSize()) {
            return interval.getNumUnits() * interval.getUnit().getSizeInMonths() *
                   com.epam.deltix.util.time.TimeUnit.DAY.getSizeInMilliseconds() * 30;
        }
        return interval.getNumUnits() * interval.getUnit().getSizeInMilliseconds();
    }

    // Used in .NET code (dotnet/talclient/Deltix/Data/RealTick\Cursor.cs) 
    public static FixedInterval     parse(long size) {

        FixedInterval result = new FixedInterval(size, TimeUnit.MILLISECOND);

        if (size % toMilliseconds(Interval.DAY) == 0)
            result = new FixedInterval(size / toMilliseconds(Interval.DAY), TimeUnit.DAY);
        if (size % toMilliseconds(Interval.HOUR) == 0)
            result = new FixedInterval(size / toMilliseconds(Interval.HOUR), TimeUnit.HOUR);
        if (size % toMilliseconds(Interval.MINUTE) == 0)
            result = new FixedInterval(size / toMilliseconds(Interval.MINUTE), TimeUnit.MINUTE);
        if (size % toMilliseconds(Interval.SECOND) == 0)
            result = new FixedInterval(size / toMilliseconds(Interval.SECOND), TimeUnit.SECOND);

        return result;
    }

    public static long getSystemProperty(String propName, long defaultValue, long minValue, long maxValue) {
        String intervalValue = System.getProperty (propName);
        if (intervalValue == null)
            return defaultValue;
        intervalValue = intervalValue.trim();
        if (intervalValue.isEmpty())
            return defaultValue;

        Interval interval = Interval.valueOf(intervalValue);
        long result = interval.toMilliseconds();
        if (result < minValue) {
            LOGGER.error().append("Property \"").append(propName).append("\" cannot be less than ").append(minValue).commit();
            result = minValue;
        }
        if (result > maxValue) {
            LOGGER.error().append("Property \"").append(propName).append("\" cannot be more than ").append(maxValue).commit();
            result = maxValue;
        }
        return result;
    }


}