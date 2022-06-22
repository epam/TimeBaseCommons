package deltix.util.log;

import deltix.util.lang.Util;
import deltix.util.text.SimpleMessageFormat;
import deltix.util.time.TimeFormatter;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Simple formatter that displays only a log message
 *
 * THIS CLASS IS INSTALLED AS JAVA EXTENSION - PLEASE AVOID ADDING ANY DEPENDENCIES.
 *
 */
public class TerseFormatter extends Formatter {
    private static CurrentMonthDate currentMonthDate = CurrentMonthDate.getInstance();

    /**
     * Format the given log record and return the formatted string.
     */
    public String format (LogRecord record) {
        StringBuilder sbuf = new StringBuilder(256);

        // time
        long time = record.getMillis();
        if (time != 0) {
            sbuf.append (currentMonthDate.getDayMonth(time)).append (' ');
            sbuf.append (formatTimestamp(time)).append(' ');
        }

        // level
        sbuf.append(record.getLevel()).append(' ');

        // message
        String message = record.getMessage ();
        Object[] params = record.getParameters();
        if (params != null && params.length > 0) {
            SimpleMessageFormat.format(sbuf, message, params);
        } else {
            sbuf.append(message);
        }

        if (record.getThrown() != null) {
            sbuf.append(Util.NATIVE_LINE_BREAK);
            sbuf.append(Util.printStackTrace(record.getThrown()));
        }

        sbuf.append(Util.NATIVE_LINE_BREAK);
        return sbuf.toString();
    }

    protected String formatTimestamp(long time) {
        return TimeFormatter.formatTimeOfDayGMT(time);
    }
}
