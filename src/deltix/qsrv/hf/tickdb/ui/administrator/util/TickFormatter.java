package deltix.qsrv.hf.tickdb.ui.administrator.util;

import java.text.*;
import java.util.*;
import java.util.logging.*;
import java.util.logging.Formatter;

public class TickFormatter extends Formatter{
    
    private              Date                    date          = new Date ();
    private static final String                  format        = "{0,date} {0,time}";
    private              java.text.MessageFormat formatter;
    private              String                  lineSeparator = "\r\n";
    private              Object                  args[]        = new Object[1];
    private              StringBuffer            buffer        = new StringBuffer ();
    private              StringBuffer            text          = new StringBuffer ();

    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
    @Override
    public String format(LogRecord record) {
        buffer.setLength (0);
        text.setLength (0);
        // Minimize memory allocations here.
        date.setTime(record.getMillis());
        args[0] = date;
        if (formatter == null) {
            formatter = new MessageFormat(format);
        }
        formatter.format(args, text, null);
        buffer.append (text);
        buffer.append (" ");
        
        String message = formatMessage(record);
        buffer.append (record.getLevel ().getLocalizedName ());
        buffer.append (": ");
        if (message != null)
            buffer.append (message);

        buffer.append (lineSeparator);

        final Throwable throwable = record.getThrown ();
        if (throwable != null) {
            buffer.append (throwable.toString ());
            buffer.append (lineSeparator);
        }
        
        return buffer.toString ();
    }

}
