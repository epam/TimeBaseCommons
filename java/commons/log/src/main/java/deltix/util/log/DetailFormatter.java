package deltix.util.log;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import deltix.util.text.SimpleMessageFormat;
import deltix.util.lang.Util;

/**
 * Description: deltix.util.log.DetailFormatter
 * Date: Jul 15, 2010
 *
 * @author Nickolay Dul
 */
public class DetailFormatter extends Formatter {
    public static final String PRINT_CONTEXT_PROPERTY = "QuantServer.logging.detailFormatter.printContext";

    private final boolean printContext;

    public DetailFormatter() {
        this(Boolean.getBoolean(PRINT_CONTEXT_PROPERTY));
    }

    public DetailFormatter(boolean printContext) {
        this.printContext = printContext;
    }

    @Override
    public String format(LogRecord record) {
        StringBuilder sbuf = new StringBuilder(256);
        // time
        sbuf.append(String.format("%1$tF %1$tT.%1$tL", record.getMillis())).append(' ');

        // context
        if (printContext) {
            if (record.getSourceClassName() != null) {
                sbuf.append(record.getSourceClassName());
            } else {
                sbuf.append(record.getLoggerName());
            }
            if (record.getSourceMethodName() != null) {
                sbuf.append(' ').append(record.getSourceMethodName());
            }
            sbuf.append(Util.NATIVE_LINE_BREAK);
        }

        // level
        sbuf.append(record.getLevel()).append(' ');
        // thread id
        sbuf.append('[').append(record.getThreadID()).append("] ");

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
}
