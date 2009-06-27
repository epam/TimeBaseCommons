package deltix.util.log;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import deltix.util.time.TimeFormatter;

/**
 * Simple formatter that displays only a log message
 *
 * THIS CLASS IS INSTALLED AS JAVA EXTENSION - PLEASE AVOID ADDING ANY DEPENDENCIES.
 *
 */
public class TerseFormatter extends Formatter {

    /**
     * Format the given log record and return the formatted string.
     */
    public String format (LogRecord record) {

        // (!) THIS CLASS IS INSTALLED AS JAVA EXTENSION - PLEASE AVOID ADDING ANY DEPENDENCIES.

        StringBuilder sbuf = new StringBuilder();

        // time
        long time = record.getMillis();
        if (time != 0) {
            sbuf.append (formatTimestamp(time));
            sbuf.append (' ');
        }

        // level
        if (record.getLevel() == Level.SEVERE)
            sbuf.append("(!) ");

        // message
        String message = record.getMessage ();
        Object [] params = record.getParameters();
        if (params != null) {
            sbuf.append (MessageFormat.format(message, params));
        } else {
            sbuf.append (message);
        }

        if (record.getThrown() != null) {
            sbuf.append ('\n');
            sbuf.append (getStackTrace (record.getThrown ()));
        }

        sbuf.append('\n');
        return sbuf.toString();
    }

    protected String formatTimestamp(long time) {
        return TimeFormatter.formatTimeOfDayGMT(time);
    }

    /** Prints stack trace of given throwable, unwraps any ChainedException */
    private static String getStackTrace (Throwable t) {

        StringWriter sw = new StringWriter (1024);
        try {
            PrintWriter pwr = new PrintWriter (sw);

            printStackTrace (t, pwr);

            pwr.close ();
            sw.close ();

        } catch (IOException ignore) { ignore.printStackTrace();   }

        return sw.toString();
    }

    /** Prints stack trace of given throwable, unwraps any ChainedException */
    private static void printStackTrace (Throwable t, PrintWriter pwr) {

        if (t != null)
            t.printStackTrace(pwr);

        Throwable cause = null;
        if (t != null) {
            cause = t.getCause();
        }

        if (cause != null) {
            pwr.println ("\nCaused by:\n");
            printStackTrace (cause, pwr);
        }
    }


}
