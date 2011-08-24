package deltix.qsrv.hf.pub;

import deltix.data.stream.TimeStampedMessage;

public class TimeStamp implements TimeStampedMessage {

    /** Number of ticks per millisecond (10 pow 4) */
    public static final int     TICKS_PER_MS = 10000;

    /** Time is measured in milliseconds that passed since January 1, 1970 UTC */
    @Title ("Time")
    @DateTimeMs
    public long                 timestamp = TIMESTAMP_UNKNOWN;

    /** Number of ticks (100 ns resolution) in defined TimeStamp#timestamp */
    @Title ("Ticks Component")
    public short                ticksComponent = 0;


    @Override
    public long                 getTicks() {
        return timestamp == TIMESTAMP_UNKNOWN ? TIMESTAMP_UNKNOWN : (getTicks(timestamp) + ticksComponent);
    }

    public void                 setTicks(long ticks) {
       if (ticks != TIMESTAMP_UNKNOWN) {
            ticksComponent = (short) (ticks % TimeStamp.TICKS_PER_MS);
            timestamp = (ticks - ticksComponent)/ TimeStamp.TICKS_PER_MS;
       } else {
            timestamp = TIMESTAMP_UNKNOWN;
            ticksComponent = 0;
       }
    }

    @Override
    public long                 getTimeStampMs() {
        return getTime();
    }

    public void                 setTime(long milliseconds) {
        timestamp = milliseconds;
        ticksComponent = 0;
    }
    
    public long                 getTime() {
        return timestamp + (ticksComponent > 0 ? 1 : 0);
    }

    public boolean              isUndefined() {
        return timestamp == TIMESTAMP_UNKNOWN;
    }

    public void                 setUndefined() {
        setTime(TIMESTAMP_UNKNOWN);
    }

    public static long          getTicks (long milliseconds) {
        return milliseconds == TIMESTAMP_UNKNOWN ? TIMESTAMP_UNKNOWN : milliseconds * TICKS_PER_MS;
    }

     public static long          getTime(long ticks) {
        if (ticks % TICKS_PER_MS == 0)
            return ticks / TICKS_PER_MS;

        return (ticks / TICKS_PER_MS) + 1;
    }
}
