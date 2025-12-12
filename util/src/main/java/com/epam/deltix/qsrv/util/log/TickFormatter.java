package com.epam.deltix.qsrv.util.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

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
            if (Level.SEVERE.equals(record.getLevel())){
                buffer.append (getStackTrace(throwable));
            }else{
                buffer.append(throwable.toString());
            }
            buffer.append (lineSeparator);
        }
        
        return buffer.toString ();
    }

    public static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

}
