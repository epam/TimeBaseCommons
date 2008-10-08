package deltix.qsrv.hf.tickdb.ui.administrator.util;

import java.text.*;
import java.util.*;
import java.util.logging.*;
import java.util.logging.Formatter;

public class TickFormatter extends Formatter{
    
    Date dat = new Date();
    private final static String format = "{0,date} {0,time}";
    private java.text.MessageFormat formatter;
    
    // Line separator string.  This is the value of the line.separator
    // property at the moment that the SimpleFormatter was created.
    private String lineSeparator = "\r\n";
    
    private Object args[] = new Object[1];

    @Override
    public String format(LogRecord record) {
        StringBuffer sb = new StringBuffer();
        // Minimize memory allocations here.
        dat.setTime(record.getMillis());
        args[0] = dat;
        StringBuffer text = new StringBuffer();
        if (formatter == null) {
            formatter = new MessageFormat(format);
        }
        formatter.format(args, text, null);
        sb.append(text);
        sb.append(" ");
        
        String message = formatMessage(record);
        sb.append(record.getLevel().getLocalizedName());
        sb.append(": ");
        sb.append(message);
        
        sb.append(lineSeparator);
         
        if (record.getThrown() != null) {
            String          msg = record.getThrown().getLocalizedMessage ();
            sb.append("Reason: ").append(msg);
            sb.append(lineSeparator);
        }
        
        return sb.toString();
    }

}
