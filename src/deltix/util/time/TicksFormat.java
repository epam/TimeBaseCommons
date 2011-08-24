package deltix.util.time;

import deltix.qsrv.hf.pub.TimeStamp;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TicksFormat {

    private static final SimpleDateFormat   TICKS = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss.");
    private static final SimpleDateFormat   MS = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss.S");
    private static final DecimalFormat      DF = new DecimalFormat("0000000");
    
    static {
        MS.setTimeZone(TimeZone.getTimeZone("GMT"));
        TICKS.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    
    public String         format(long milliseconds, short ticksComponent) {
        StringBuffer sb = new StringBuffer();
        if (ticksComponent == 0) {
            MS.format(new Date(milliseconds), sb, new FieldPosition(0));
        } else {
            long ms = milliseconds % 1000;
            TICKS.format(new Date(milliseconds), sb, new FieldPosition(0));
            DF.format(ms * TimeStamp.TICKS_PER_MS + ticksComponent, sb, new FieldPosition(0));
        }

        return sb.toString();
    }

}
