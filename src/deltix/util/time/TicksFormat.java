package deltix.util.time;

import deltix.data.stream.TimeStampedMessage;
import deltix.qsrv.hf.pub.TimeStamp;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TicksFormat {

    private static final SimpleDateFormat   TICKS = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss.");
    private static final SimpleDateFormat   MS = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss.S");
    private static final DecimalFormat      DF = new DecimalFormat("000000000");
    
    static {
        MS.setTimeZone(TimeZone.getTimeZone("GMT"));
        TICKS.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    
    public String         format(long milliseconds, int nanosComponent) {
        StringBuffer sb = new StringBuffer();
        if (nanosComponent == 0 || milliseconds == TimeStampedMessage.TIMESTAMP_UNKNOWN) {
            MS.format(new Date(milliseconds), sb, new FieldPosition(0));
        } else {
            long ms = milliseconds % 1000;
            TICKS.format(new Date(milliseconds), sb, new FieldPosition(0));
            DF.format(ms * TimeStamp.NANOS_PER_MS + nanosComponent, sb, new FieldPosition(0));
        }

        return sb.toString();
    }

}
