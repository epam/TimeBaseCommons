package deltix.util.time;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 *  Thread-safe and fast time formatter.
 */
public final class TimeFormatter {

    private final static long SECONDS_IN_DAY = 24*60*60;

    /** Offset of local time zone from GMT (in milliseconds) */
    private final static long MILLIS_GMT_OFFSET;
    static {
        Calendar c = Calendar.getInstance();
        MILLIS_GMT_OFFSET = c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET);
    }

    private TimeFormatter () {}


    /**
     * Fast and thread-safe method for printing *local* time of day from GMT time of day. Useful for logging absolute time.
     *
     * @param gmtTimeInMilliseconds number of milliseconds in GMT time zone. Note: input will be truncated to single day!
     * @return time formatted like "00:00:00"
     *
     * @see #formatTimeOfDay(long)
     * @see #formatTimeOfDayFromSeconds(long)
     */
    public static String formatTimeOfDayGMT (long gmtTimeInMilliseconds) {
    	return formatTimeOfDay (gmtTimeInMilliseconds + MILLIS_GMT_OFFSET);
    }


    /**
     * Fast and thread-safe method for printing given time of day. Useful for logging elapsed time.
     *
     * @param timeInMilliseconds number of milliseconds to format. Note: input will be truncated to single day!
     * @return time formatted like "00:00:00"
     */
    public static String formatTimeOfDay (long timeInMilliseconds) {
    	return formatTimeOfDayFromSeconds (timeInMilliseconds / 1000);
    }

    /**
     * Fast and thread-safe method for printing current time of day in local time zone.
     *
     * @param seconds number of *seconds* to format. Note: input will be truncated to single day!
     * @return duration formatted like "12:34:45"
     *
     * @see #formatTimeOfDay(long)
     * @see #formatTimeOfDayGMT(long)
     */
    public static String formatTimeOfDayFromSeconds (long seconds) {
    	char [] timebuf = new char [] { '0', '0', ':', '0', '0', ':', '0', '0' };
        int secondsInDay = (int) (seconds % SECONDS_IN_DAY);

        int     s = secondsInDay % 60;
        int     m = (secondsInDay / 60) % 60;
        int     h = secondsInDay / 3600;

        // H low
        int foo = h % 10;
        if (foo > 0)
            timebuf [1] += foo;

        // H high
        foo = h / 10;
        if (foo > 0)
            timebuf [0] += foo;

        // M low
        foo = m % 10;
        if (foo > 0)
            timebuf [4] += foo;

        // M high
        foo = m / 10;
        if (foo > 0)
            timebuf [3] += foo;

        // S low
        foo = s % 10;
        if (foo > 0)
            timebuf [7] += foo;

        // S high
        foo = s  / 10;
        if (foo > 0)
            timebuf [6] += foo;

        return new String (timebuf);
    }



    /**
     * <p>Thread-Safe method to parse duration.
     *    <u>No trailing or leading spaces are allowed.</u>
     *    Digit groups may defined using one or two digits.</p>
     *
     * <p>Duration string format: <code>M+ | M+:SS | H+:M[M]:S[S]</code>, or:
     *              <ul>
     *                  <li>HH:MM:SS (HH can exceed 60)</li>
     *                  <li>MM:SS (MM can exceed 60)</li>
     *                  <li>MM (MM can exceed 60)</li>
     *              </ul></p>
     *
     * @param text input string (e.g. "0:12" or "00:12:00")
     * @return number of seconds in duration
     *
     * @see #parseTimeOfDay(CharSequence)
     */
    public static int parseDurationInSeconds (String text)
        throws DurationParseException
    {
        /// See test.td.util.time.Test_SimpleDurationFormat for JUnit test of this method

        final int len = (text != null) ? text.length() : 0;

        if (len == 0)
            throw new DurationParseException ("Cannot parse empty string as duration", text, 0);

        int pos = 0;
        int result = 0;
        int group = 0;

        // BEGIN(Good old C mode)
        final int TERMINAL_STATE_MASK                 = 0x100;
        final int STATE_EXPECT_FIRST_DIGITS_GROUP      = 0x001; // non terminal
        final int STATE_INSIDE_FIRST_DIGITS_GROUP      = 0x102;
        final int STATE_EXPECT_SECOND_DIGITS_GROUP     = 0x003; // non terminal
        final int STATE_INSIDE_SECOND_DIGITS_GROUP     = 0x104;
        final int STATE_PARSED_SECOND_DIGITS_GROUP     = 0x105;
        final int STATE_EXPECT_THIRD_DIGITS_GROUP      = 0x006;
        final int STATE_INSIDE_THIRD_DIGITS_GROUP      = 0x107;
        final int STATE_PARSED_THIRD_DIGITS_GROUP      = 0x108;

        int state = STATE_EXPECT_FIRST_DIGITS_GROUP;

        while (pos < len) {
            final char ch = text.charAt (pos);

            switch (state) {
                // initial state
                case STATE_EXPECT_FIRST_DIGITS_GROUP:
                    if (ch >= '0' && ch <= '9') {
                        group = (ch - '0');
                        state = STATE_INSIDE_FIRST_DIGITS_GROUP;
                    } else {
                        throw new DurationParseException ("Expecting digit", text, pos);
                    }
                    break;

                // we parsed first digit (can be hours or minutes)
                case STATE_INSIDE_FIRST_DIGITS_GROUP:
                    if (ch >= '0' && ch <= '9') {
                        group = group * 10 + (ch - '0');
                        state = STATE_INSIDE_FIRST_DIGITS_GROUP;
                    } else if (ch == ':') {
                        state = STATE_EXPECT_SECOND_DIGITS_GROUP;
                    } else {
                        throw new DurationParseException ("Unexpected digits separator '" + ch + "'", text, pos);
                    }
                    break;

                // we parsed first ':' symbol
                case STATE_EXPECT_SECOND_DIGITS_GROUP:
                    result = group*60;
                    if (ch >= '0' && ch <= '9') {
                        group = (ch - '0');
                        state = STATE_INSIDE_SECOND_DIGITS_GROUP;
                    } else {
                        throw new DurationParseException ("Expecting digit", text, pos);
                    }
                    break;

                // we parsed first digit of second group
                case STATE_INSIDE_SECOND_DIGITS_GROUP:
                    if (ch >= '0' && ch <= '9') {
                        group = group * 10 + (ch - '0');
                        if (group > 59)
                            throw new DurationParseException ("Minutes or seconds group exceed 59", text, pos);
                        state = STATE_PARSED_SECOND_DIGITS_GROUP;
                    } else if (ch == ':') {
                        state = STATE_EXPECT_THIRD_DIGITS_GROUP;
                    } else {
                        throw new DurationParseException ("Unexpected minutes separator '" + ch + "'", text, pos);
                    }
                    break;

                // we parsed two digits of minutes value
                case STATE_PARSED_SECOND_DIGITS_GROUP:
                    if (ch == ':') {
                        state = STATE_EXPECT_THIRD_DIGITS_GROUP;
                    } else {
                        throw new DurationParseException ("Unexpected minutes separator '" + ch + "'", text, pos);
                    }
                    break;

                // we parsed second ':' symbol
                case STATE_EXPECT_THIRD_DIGITS_GROUP:
                    result = (result + group)*60;
                    if (ch >= '0' && ch <= '9') {
                        group = (ch - '0');
                        state = STATE_INSIDE_THIRD_DIGITS_GROUP;
                    } else {
                        throw new DurationParseException ("Expecting first seconds digit", text, pos);
                    }
                    break;

                // we parsed first digit of seconds value
                case STATE_INSIDE_THIRD_DIGITS_GROUP:
                    if (ch >= '0' && ch <= '9') {
                        group = group * 10 + (ch - '0');
                        if (group > 59)
                            throw new DurationParseException ("Seconds value exceed 59", text, pos);
                        state = STATE_PARSED_THIRD_DIGITS_GROUP;
                    } else {
                        throw new DurationParseException ("Unexpected character after seconds group '" + ch + "'", text, pos);
                    }
                    break;

                case STATE_PARSED_THIRD_DIGITS_GROUP:
                    throw new DurationParseException ("Unexpected tail symbol '" + ch + "'", text, pos);

                default:
                    throw new IllegalStateException ("Unexpected state: " + state);

            }
            pos++;
        }

        if ((state & TERMINAL_STATE_MASK) == 0)
            throw new DurationParseException ("Unexpected end of string (state:" + state +')', text, pos);

        switch (state) {
            case STATE_INSIDE_FIRST_DIGITS_GROUP:
                result = group*60; break; // group contains minutes MM

            case STATE_INSIDE_SECOND_DIGITS_GROUP:
            case STATE_PARSED_SECOND_DIGITS_GROUP:
            case STATE_INSIDE_THIRD_DIGITS_GROUP:
            case STATE_PARSED_THIRD_DIGITS_GROUP:
                result += group; break;  // group contains seconds MM:SS
            default:
                throw new IllegalStateException ("Unexpected final state: " + state);
        }

//        if (result >= DurationFormat.MAX_DURATION_IN_SEC)
//            throw new DurationParseException ("Duration " + result + " exceed maximum of " + DurationFormat.MAX_DURATION_IN_SEC, text, pos);

        // END(Good old C mode)
        return result;
    }


    /**
     * <p>Thread-Safe method to parse time of day.
     *    <u>No trailing or leading spaces are allowed.</u>
     *    Digit groups may defined using one or two digits.</p>
     *
     * <p>Time of day string format: <code> H[H][:M[M][:S[S]]] [am|pm|AM|PM] </code>, for example:
     *  <ul>
     *     <li>HH:MM:SS (24-hour)</li>
     *     <li>HH:MM (24-hour)</li>
     *     <li>HH:MM:SS am/pm</li>
     *     <li>HH:MM am/pm</li>
     *     <li>HH am/pm</li>
     *     <li>HH</li>
     *   </ul>
     * </p>
     *
     * @param text input string (e.g. "0:12" or "11:12:13 pm")
     * @return number of seconds since midnight represented by given time of day string
     * @see #parseDurationInSeconds(String)
     *
     */
    public static int parseTimeOfDay (CharSequence text)
        throws TimeOfDayParseException
    {
        /// See test.td.util.time.Test_SimpleTimeOfDayFormat for JUnit test of this method

        final int len = (text != null) ? text.length() : 0;

        if (len == 0)
            throw new TimeOfDayParseException ("Cannot parse empty string as time of day", text, 0);

        int pos = 0;
        int hours = 0, minutes = 0, seconds = 0;

        // BEGIN(Good old C mode)
        final int TERMINAL_STATE_MASK                 = 0x100;
        final int STATE_EXPECT_FIRST_HOUR_DIGIT       = 0x001; // non terminal
        final int STATE_PARSED_FIRST_HOUR_DIGIT       = 0x102;
        final int STATE_PARSED_BOTH_HOUR_DIGITS       = 0x103;
        final int STATE_EXPECT_FIRST_MINUTE_DIGIT     = 0x004; // non terminal
        final int STATE_PARSED_FIRST_MINUTE_DIGIT     = 0x105;
        final int STATE_PARSED_BOTH_MINUTE_DIGITS     = 0x106;
        final int STATE_EXPECT_FIRST_SECOND_DIGIT     = 0x007; // non terminal
        final int STATE_PARSED_FIRST_SECONDS_DIGIT    = 0x108;
        final int STATE_PARSED_BOTH_SECOND_DIGITS     = 0x109;
        final int STATE_PARSED_SPACE                  = 0x00A; // non terminal
        final int STATE_EXPECT_M_SYMBOL               = 0x00B; // non terminal
        final int STATE_PARSED_M_SYMBOL               = 0x10C;

        int state = STATE_EXPECT_FIRST_HOUR_DIGIT;

        while (pos < len) {
            final char ch = text.charAt (pos);

            switch (state) {
                // initial state
                case STATE_EXPECT_FIRST_HOUR_DIGIT:
                    if (ch >= '0' && ch <= '9') {
                        hours = (ch - '0');
                        state = STATE_PARSED_FIRST_HOUR_DIGIT;
                    } else {
                        throw new TimeOfDayParseException ("Expecting first hour digit", text, pos);
                    }
                    break;

                // we parsed first digit of hours value
                case STATE_PARSED_FIRST_HOUR_DIGIT:
                    if (ch >= '0' && ch <= '9') {
                        hours = hours * 10 + (ch - '0');
                        if (hours > 23)
                            throw new TimeOfDayParseException ("Hours value exceed 23", text, pos);
                        state = STATE_PARSED_BOTH_HOUR_DIGITS;
                    } else if (ch == ':') {
                        state = STATE_EXPECT_FIRST_MINUTE_DIGIT;
                    } else if (ch == ' ' || ch == '\t') {
                        state = STATE_PARSED_SPACE;
                    } else {
                        throw new TimeOfDayParseException ("Unexpected hours separator '" + ch + "'", text, pos);
                    }
                    break;

                // we parsed two digits of hours value
                case STATE_PARSED_BOTH_HOUR_DIGITS:
                    if (ch == ':') {
                        state = STATE_EXPECT_FIRST_MINUTE_DIGIT;
                    } else if (ch == ' ' || ch == '\t') {
                        state = STATE_PARSED_SPACE;
                    } else {
                        throw new TimeOfDayParseException ("Unexpected hours separator '" + ch + "'", text, pos);
                    }
                    break;

                // we parsed first ':' symbol
                case STATE_EXPECT_FIRST_MINUTE_DIGIT:
                    if (ch >= '0' && ch <= '9') {
                        minutes = (ch - '0');
                        state = STATE_PARSED_FIRST_MINUTE_DIGIT;
                    } else {
                        throw new TimeOfDayParseException ("Expecting first minutes digit", text, pos);
                    }
                    break;

                // we parsed first digit of minutes value
                case STATE_PARSED_FIRST_MINUTE_DIGIT:
                    if (ch >= '0' && ch <= '9') {
                        minutes = minutes * 10 + (ch - '0');
                        if (minutes > 59)
                            throw new TimeOfDayParseException ("Minutes value exceed 59", text, pos);
                        state = STATE_PARSED_BOTH_MINUTE_DIGITS;
                    } else if (ch == ':') {
                        state = STATE_EXPECT_FIRST_SECOND_DIGIT;
                    } else if (ch == ' ' || ch == '\t') {
                        state = STATE_PARSED_SPACE;
                    } else {
                        throw new TimeOfDayParseException ("Unexpected minutes separator '" + ch + "'", text, pos);
                    }
                    break;

                // we parsed two digits of minutes value
                case STATE_PARSED_BOTH_MINUTE_DIGITS:
                    if (ch == ':') {
                        state = STATE_EXPECT_FIRST_SECOND_DIGIT;
                    } else if (ch == ' ' || ch == '\t') {
                        state = STATE_PARSED_SPACE;
                    } else {
                        throw new TimeOfDayParseException ("Unexpected minutes separator '" + ch + "'", text, pos);
                    }
                    break;

                // we parsed second ':' symbol
                case STATE_EXPECT_FIRST_SECOND_DIGIT:
                    if (ch >= '0' && ch <= '9') {
                        seconds = (ch - '0');
                        state = STATE_PARSED_FIRST_SECONDS_DIGIT;
                    } else {
                        throw new TimeOfDayParseException ("Expecting first seconds digit", text, pos);
                    }
                    break;

                // we parsed first digit of seconds value
                case STATE_PARSED_FIRST_SECONDS_DIGIT:
                    if (ch >= '0' && ch <= '9') {
                        seconds = seconds * 10 + (ch - '0');
                        if (seconds > 59)
                            throw new TimeOfDayParseException ("Seconds value exceed 59", text, pos);
                        state = STATE_PARSED_BOTH_SECOND_DIGITS;
                    } else if (ch == ' ' || ch == '\t') {
                        state = STATE_PARSED_SPACE;
                    } else {
                        throw new TimeOfDayParseException ("Unexpected character after seconds group '" + ch + "'", text, pos);
                    }
                    break;

                // we parsed two digits of seconds value
                case STATE_PARSED_BOTH_SECOND_DIGITS:
                    if (ch == ' ' || ch == '\t') {
                        state = STATE_PARSED_SPACE;
                    } else {
                        throw new TimeOfDayParseException ("Expecting space separator '" + ch + "'", text, pos);
                    }
                    break;

                // we got space
                case STATE_PARSED_SPACE:
                    if (ch == 'a' || ch == 'A') {
                        if (hours < 1 || hours > 12)
                            throw new TimeOfDayParseException ("Hour in AM/PM format must be [1..12]", text, pos);

                        if (hours == 12)
                            hours = 0;
                        state = STATE_EXPECT_M_SYMBOL;
                    } else if (ch == 'p' || ch == 'P') {
                        if (hours < 1 || hours > 12)
                            throw new TimeOfDayParseException ("Hour in AM/PM format must be [1..12]", text, pos);

                        if (hours != 12)
                            hours += 12;

                        state = STATE_EXPECT_M_SYMBOL;
                    } else if (ch == ' ' || ch == '\t') {
                        state = STATE_PARSED_SPACE;
                    } else {
                        throw new TimeOfDayParseException ("Expecting space or am/pm literal", text, pos);
                    }
                    break;

                case STATE_EXPECT_M_SYMBOL:
                    if (ch == 'm' || ch == 'M') {
                        state = STATE_PARSED_M_SYMBOL;
                        if (++pos < len) // (*)
                            throw new TimeOfDayParseException ("Unexpected tail symbols", text, pos);

                        break;

                    } else {
                        throw new TimeOfDayParseException ("Expecting 'm' symbol", text, pos);
                    }
                case STATE_PARSED_M_SYMBOL: // Redunant with (*)
                    throw new TimeOfDayParseException ("Expected symbol after am/pm: '" + ch + "'", text, pos);

                default:
                    throw new IllegalStateException ("Unexpected state: " + state);

            }
            pos++;
        }

        if ((state & TERMINAL_STATE_MASK) == 0)
            throw new TimeOfDayParseException ("Unexpected end of string (state:" + state +')', text, pos);

        // END(Good old C mode)
        return hours*3600 + minutes*60 + seconds;
    }

    @SuppressWarnings("serial")
	public static final class TimeOfDayParseException extends NumberFormatException {
        public TimeOfDayParseException (String message, CharSequence text, int position) {
            super (message + " (" + text + " at position " + position +')');
        }
    }

    @SuppressWarnings("serial")
	public static final class DurationParseException extends NumberFormatException {
        public DurationParseException (String message, String text, int position) {
            super (message + " (" + text + " at position " + position +')');
        }
    }


}
