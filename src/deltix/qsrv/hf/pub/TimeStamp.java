package deltix.qsrv.hf.pub;

import deltix.data.stream.TimeStampedMessage;

public class TimeStamp implements TimeStampedMessage {

    /** Number of ticks per millisecond (10 pow 4) */
    //public static final int     TICKS_PER_MS = 10000;
    public static final int     NANOS_PER_MS = 1000000;

    /** Time is measured in milliseconds that passed since January 1, 1970 UTC */
    @Title ("Time")
    @DateTimeMs
    public long                 timestamp = TIMESTAMP_UNKNOWN;

    /** Number of ticks (100 ns resolution) in defined TimeStamp#timestamp */
    @Title ("Ticks Component")
    public int                  nanosComponent = 0;

//    public long                 getNanoTime() {
//        return timestamp == TIMESTAMP_UNKNOWN ? TIMESTAMP_UNKNOWN : (getNanoTime(timestamp) + nanosComponent);
//    }

    public void                 setNanoTime(long nanoSeconds) {
       if (nanoSeconds != TIMESTAMP_UNKNOWN) {
            nanosComponent = (int) (nanoSeconds % TimeStamp.NANOS_PER_MS);
            timestamp = (nanoSeconds - nanosComponent) / TimeStamp.NANOS_PER_MS;
       } else {
            timestamp = TIMESTAMP_UNKNOWN;
            nanosComponent = 0;
       }
    }

    @Override
    public long                 getNanoTime() {
        return timestamp == TIMESTAMP_UNKNOWN ? TIMESTAMP_UNKNOWN : (getNanoTime(timestamp) + nanosComponent);
    }

    @Override
    public long                 getTimeStampMs() {
        return getTime();
    }

    public void                 setTime(long milliseconds) {
        timestamp = milliseconds;
        nanosComponent = 0;
    }
    
    public long                 getTime() {
        return timestamp + (nanosComponent > 0 ? 1 : 0);
    }

    public boolean              isUndefined() {
        return timestamp == TIMESTAMP_UNKNOWN;
    }

    public void                 setUndefined() {
        setTime(TIMESTAMP_UNKNOWN);
    }

    public static long          getNanoTime (long milliseconds) {
        return milliseconds == TIMESTAMP_UNKNOWN ? TIMESTAMP_UNKNOWN : milliseconds * NANOS_PER_MS;
    }

    public static long          getNanoTime (long milliseconds, int nanosComponent) {
        return milliseconds == TIMESTAMP_UNKNOWN ? TIMESTAMP_UNKNOWN : milliseconds * NANOS_PER_MS;
    }

//    public static long          getTime(long nanos) {
//        if (nanos % NANOS_PER_MS == 0)
//            return NANOS_PER_MS / TICKS_PER_MS;
//
//        return (NANOS_PER_MS / TICKS_PER_MS) + 1;
//    }
}
