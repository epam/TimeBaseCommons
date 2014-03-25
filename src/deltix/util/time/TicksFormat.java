package deltix.util.time;

import deltix.data.stream.TimeStampedMessage;
import deltix.qsrv.hf.pub.TimeStamp;
import org.apache.commons.lang.time.FastDateFormat;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TicksFormat {

    private final StringBuffer              sb = new StringBuffer();
    private final Calendar                  calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));

    private static final FastDateFormat     TICKS = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.", TimeZone.getTimeZone("GMT"));
    private static final FastDateFormat     MS = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS", TimeZone.getTimeZone("GMT"));
    private static final DecimalFormat      DF = new DecimalFormat("000000000");

    public synchronized String         format(long milliseconds, int nanosComponent) {
        sb.setLength(0);
        calendar.setTimeInMillis(milliseconds);

        if (nanosComponent == 0 || milliseconds == TimeStampedMessage.TIMESTAMP_UNKNOWN) {
            MS.format(calendar, sb);
        } else {
            TICKS.format(calendar, sb);

            long ms = milliseconds % 1000;
            DF.format(ms * TimeStamp.NANOS_PER_MS + nanosComponent, sb, new FieldPosition(0));
        }

        return sb.toString();
    }

}
