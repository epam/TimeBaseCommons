package com.epam.deltix.util.time;

import org.apache.commons.lang3.StringUtils;

import java.util.*;


public class DurationFormatUtilEx {

    public static final long MILLIS_PER_SECOND = 1000L;
    public static final long MILLIS_PER_MINUTE = 60000L;
    public static final long MILLIS_PER_HOUR   = 3600000L;
    public static final long MILLIS_PER_DAY    = 86400000L;

    public static final int  DAYS_IN_MONTH     = 30;
    public static final int  DAYS_IN_YEAR      = 365;

    public static final long MILLIS_PER_MONTH  = MILLIS_PER_DAY * DAYS_IN_MONTH;
    public static final long MILLIS_PER_YEAR   = MILLIS_PER_DAY * DAYS_IN_YEAR;

    static final Object      y                 = "y";
    static final Object      M                 = "M";
    static final Object      d                 = "d";
    static final Object      H                 = "H";
    static final Object      m                 = "m";
    static final Object      s                 = "s";
    static final Object      S                 = "S";

    /**
     * Parses a classic date format string into Tokens
     * 
     * @param format
     *            to parse
     * @return Token[] of tokens
     */
    static Token[] lexx (final String format) {
        final char[] array = format.toCharArray ();
        final ArrayList<Token> list = new ArrayList<Token> (array.length);

        boolean inLiteral = false;
        StringBuffer buffer = null;
        Token previous = null;
        final int sz = array.length;
        for (int i = 0; i < sz; i++) {
            final char ch = array[i];
            if (inLiteral && ch != '\'') {
                buffer.append (ch);
                continue;
            }
            Object value = null;
            switch (ch) {
                // TODO: Need to handle escaping of '
                case '\'':
                    if (inLiteral) {
                        buffer = null;
                        inLiteral = false;
                    } else {
                        buffer = new StringBuffer ();
                        list.add (new Token (buffer));
                        inLiteral = true;
                    }
                break;
                case 'y':
                    value = y;
                break;
                case 'M':
                    value = M;
                break;
                case 'd':
                    value = d;
                break;
                case 'H':
                    value = H;
                break;
                case 'm':
                    value = m;
                break;
                case 's':
                    value = s;
                break;
                case 'S':
                    value = S;
                break;
                default:
                    if (buffer == null) {
                        buffer = new StringBuffer ();
                        list.add (new Token (buffer));
                    }
                    buffer.append (ch);
            }

            if (value != null) {
                if (previous != null && previous.getValue () == value) {
                    previous.increment ();
                } else {
                    final Token token = new Token (value);
                    list.add (token);
                    previous = token;
                }
                buffer = null;
            }
        }
        return list.toArray (new Token[list.size ()]);
    }

    /**
     * Element that is parsed from the format pattern.
     */
    static class Token {

        /**
         * Helper method to determine if a set of tokens contain a value
         * 
         * @param tokens
         *            set to look in
         * @param value
         *            to look for
         * @return boolean <code>true</code> if contained
         */
        static boolean containsTokenWithValue (final Token[] tokens,
                                               final Object value) {
            final int sz = tokens.length;
            for (int i = 0; i < sz; i++) {
                if (tokens[i].getValue () == value) {
                    return true;
                }
            }
            return false;
        }

        private final Object value;
        private int          count;

        /**
         * Wraps a token around a value. A value would be something like a 'Y'.
         * 
         * @param value
         *            to wrap
         */
        Token (final Object value) {
            this.value = value;
            this.count = 1;
        }

        /**
         * Wraps a token around a repeated number of a value, for example it would
         * store 'yyyy' as a value for y and a count of 4.
         * 
         * @param value
         *            to wrap
         * @param count
         *            to wrap
         */
        Token (final Object value,
               final int count) {
            this.value = value;
            this.count = count;
        }

        /**
         * Adds another one of the value
         */
        void increment () {
            count++;
        }

        /**
         * Gets the current number of values represented
         * 
         * @return int number of values represented
         */
        int getCount () {
            return count;
        }

        /**
         * Gets the particular value this token represents.
         * 
         * @return Object value
         */
        Object getValue () {
            return value;
        }

        /**
         * Supports equality of this Token to another Token.
         * 
         * @param obj2
         *            Object to consider equality of
         * @return boolean <code>true</code> if equal
         */
        @Override
        public boolean equals (final Object obj2) {
            if (obj2 instanceof Token) {
                final Token tok2 = (Token) obj2;
                if (this.value.getClass () != tok2.value.getClass ()) {
                    return false;
                }
                if (this.count != tok2.count) {
                    return false;
                }
                if (this.value instanceof StringBuffer) {
                    return this.value.toString ().equals (
                                                          tok2.value.toString ());
                } else if (this.value instanceof Number) {
                    return this.value.equals (tok2.value);
                } else {
                    return this.value == tok2.value;
                }
            }
            return false;
        }

        /**
         * Returns a hashcode for the token equal to the
         * hashcode for the token's value. Thus 'TT' and 'TTTT'
         * will have the same hashcode.
         * 
         * @return The hashcode for the token
         */
        @Override
        public int hashCode () {
            return this.value.hashCode ();
        }

        /**
         * Represents this token as a String.
         * 
         * @return String representation of the token
         */
        @Override
        public String toString () {
            return StringUtils.repeat (this.value.toString (),
                                       this.count);
        }
    }

    public static String formatDuration (final long durationMillis,
                                         final String format) {
        return formatDuration (durationMillis,
                               format,
                               true);
    }

    public static String formatDuration (long durationMillis,
                                         final String format,
                                         final boolean padWithZeros) {
        final Token[] tokens = lexx (format);

        int years = 0;
        int months = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        int milliseconds = 0;

        if (Token.containsTokenWithValue (tokens,
                                          y)) {
            years = (int) (durationMillis / MILLIS_PER_YEAR);
            durationMillis = durationMillis
                                  - (years * MILLIS_PER_YEAR);
        }
        if (Token.containsTokenWithValue (tokens,
                                          M)) {
            months = (int) (durationMillis / MILLIS_PER_MONTH);
            durationMillis = durationMillis
                                  - (months * MILLIS_PER_MONTH);
        }
        if (Token.containsTokenWithValue (tokens,
                                          d)) {
            days = (int) (durationMillis / MILLIS_PER_DAY);
            durationMillis = durationMillis
                                  - (days * MILLIS_PER_DAY);
        }
        if (Token.containsTokenWithValue (tokens,
                                          H)) {
            hours = (int) (durationMillis / MILLIS_PER_HOUR);
            durationMillis = durationMillis
                                  - (hours * MILLIS_PER_HOUR);
        }
        if (Token.containsTokenWithValue (tokens,
                                          m)) {
            minutes = (int) (durationMillis / MILLIS_PER_MINUTE);
            durationMillis = durationMillis
                                  - (minutes * MILLIS_PER_MINUTE);
        }
        if (Token.containsTokenWithValue (tokens,
                                          s)) {
            seconds = (int) (durationMillis / MILLIS_PER_SECOND);
            durationMillis = durationMillis
                                  - (seconds * MILLIS_PER_SECOND);
        }
        if (Token.containsTokenWithValue (tokens,
                                          S)) {
            milliseconds = (int) durationMillis;
        }
      

        return format (tokens,
                       years,
                       months,
                       days,
                       hours,
                       minutes,
                       seconds,
                       milliseconds,
                       padWithZeros);

    }

    static String format (final Token[] tokens,
                          final int years,
                          final int months,
                          final int days,
                          final int hours,
                          final int minutes,
                          final int seconds,
                          int milliseconds,
                          final boolean padWithZeros) {
        final StringBuffer buffer = new StringBuffer ();
        boolean lastOutputSeconds = false;
        final int sz = tokens.length;
        for (int i = 0; i < sz; i++) {
            final Token token = tokens[i];
            final Object value = token.getValue ();
            final int count = token.getCount ();
            if (value instanceof StringBuffer) {
                buffer.append (value.toString ());
            } else {
                if (value == y) {
                    buffer.append (padWithZeros ? StringUtils.leftPad (Integer.toString (years),
                                                                       count,
                                                                       '0') : Integer.toString (years));
                    lastOutputSeconds = false;
                } else if (value == M) {
                    buffer.append (padWithZeros ? StringUtils.leftPad (Integer.toString (months),
                                                                       count,
                                                                       '0') : Integer.toString (months));
                    lastOutputSeconds = false;
                } else if (value == d) {
                    buffer.append (padWithZeros ? StringUtils.leftPad (Integer.toString (days),
                                                                       count,
                                                                       '0') : Integer.toString (days));
                    lastOutputSeconds = false;
                } else if (value == H) {
                    buffer.append (padWithZeros ? StringUtils.leftPad (Integer.toString (hours),
                                                                       count,
                                                                       '0') : Integer.toString (hours));
                    lastOutputSeconds = false;
                } else if (value == m) {
                    buffer.append (padWithZeros ? StringUtils.leftPad (Integer.toString (minutes),
                                                                       count,
                                                                       '0') : Integer.toString (minutes));
                    lastOutputSeconds = false;
                } else if (value == s) {
                    buffer.append (padWithZeros ? StringUtils.leftPad (Integer.toString (seconds),
                                                                       count,
                                                                       '0') : Integer.toString (seconds));
                    lastOutputSeconds = true;
                } else if (value == S) {
                    if (lastOutputSeconds) {
                        milliseconds += 1000;
                        final String str = padWithZeros ? StringUtils.leftPad (Integer.toString (milliseconds),
                                                                               count,
                                                                               '0') : Integer.toString (milliseconds);
                        buffer.append (str.substring (1));
                    } else {
                        buffer.append (padWithZeros ? StringUtils.leftPad (Integer.toString (milliseconds),
                                                                           count,
                                                                           '0') : Integer.toString (milliseconds));
                    }
                    lastOutputSeconds = false;
                }
            }
        }
        return buffer.toString ();
    }

    public static String formatDurationWords (final long durationMillis,
                                              final boolean suppressLeadingZeroElements,
                                              final boolean suppressTrailingZeroElements) {
        String duration = formatDuration (durationMillis,
                                          "y' years 'M' months 'd' days 'H' hours 'm' minutes 's' seconds'");
        if (suppressLeadingZeroElements) {
            duration = " " + duration;
            String tmp = StringUtils.replaceOnce (duration,
                                                  " 0 years",
                                                  "");
            if (tmp.length () != duration.length ()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce (duration,
                                               " 0 months",
                                               "");
                if (tmp.length () != duration.length ()) {
                    duration = tmp;
                    tmp = StringUtils.replaceOnce (duration,
                                                          " 0 days",
                                                          "");
                    if (tmp.length () != duration.length ()) {
                        duration = tmp;
                        tmp = StringUtils.replaceOnce (duration,
                                                       " 0 hours",
                                                       "");
                        if (tmp.length () != duration.length ()) {
                            duration = tmp;
                            tmp = StringUtils.replaceOnce (duration,
                                                           " 0 minutes",
                                                           "");
                            duration = tmp;
                            if (tmp.length () != duration.length ())
                                duration = StringUtils.replaceOnce (tmp,
                                                                    " 0 seconds",
                                                                    "");
                        }
                    }
                }
            }

            if (duration.length () != 0)
                duration = duration.substring (1);
        }
        if (suppressTrailingZeroElements) {
            String tmp = StringUtils.replaceOnce (duration,
                                                  " 0 seconds",
                                                  "");
            if (tmp.length () != duration.length ()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce (duration,
                                               " 0 minutes",
                                               "");
                if (tmp.length () != duration.length ()) {
                    duration = tmp;
                    tmp = StringUtils.replaceOnce (duration,
                                                   " 0 hours",
                                                   "");
                    if (tmp.length () != duration.length ()) {
                        duration = tmp;
                        tmp = StringUtils.replaceOnce (tmp,
                                                       " 0 days",
                                                       "");
                        if (tmp.length () != duration.length ()) {
                            duration = tmp;
                            tmp = StringUtils.replaceOnce (tmp,
                                                           " 0 months",
                                                           "");
                            if (tmp.length () != duration.length ()) {
                                duration = tmp;
                                tmp = StringUtils.replaceOnce (tmp,
                                                               " 0 years",
                                                               "");
                            }
                        }
                    }
                }
            }
        }
        duration = " " + duration;
        duration = StringUtils.replaceOnce (duration,
                                            " 1 seconds",
                                            " 1 second");
        duration = StringUtils.replaceOnce (duration,
                                            " 1 minutes",
                                            " 1 minute");
        duration = StringUtils.replaceOnce (duration,
                                            " 1 hours",
                                            " 1 hour");
        duration = StringUtils.replaceOnce (duration,
                                            " 1 days",
                                            " 1 day");
        duration = StringUtils.replaceOnce (duration,
                                            " 1 months",
                                            " 1 month");
        duration = StringUtils.replaceOnce (duration,
                                            " 1 years",
                                            " 1 year");
        return duration.trim ();
    }

}
