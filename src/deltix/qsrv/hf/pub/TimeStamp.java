package deltix.qsrv.hf.pub;

import deltix.data.stream.TimeStampedMessage;

public class TimeStamp implements TimeStampedMessage {

    /** Number of nanos per millisecond (10^6) */
    public static final int     NANOS_PER_MS = 1000000;

    /** Time is measured in milliseconds that passed since January 1, 1970 UTC */
    @Title ("Time")
    @FieldType("TIMESTAMP")
    public long                 timestamp = TIMESTAMP_UNKNOWN;

    /** Nanoseconds part of the TimeStamp#timestamp */
    @Title ("Nanoseconds Component")
    public int                  nanosComponent = 0;

    public void                 setNanoTime(long nanoSeconds) {
        if (nanoSeconds != TIMESTAMP_UNKNOWN) {
            nanosComponent = (int) (nanoSeconds % TimeStamp.NANOS_PER_MS);
            timestamp = nanoSeconds / TimeStamp.NANOS_PER_MS;
        } else {
            nanosComponent = 0;
            timestamp = TIMESTAMP_UNKNOWN;
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

    public static long          getMilliseconds (long nanoSeconds) {
        return nanoSeconds != TIMESTAMP_UNKNOWN ? nanoSeconds / TimeStamp.NANOS_PER_MS : TIMESTAMP_UNKNOWN;
    }

    public static long          getNanoTime (long milliseconds) {
        return milliseconds == TIMESTAMP_UNKNOWN ? TIMESTAMP_UNKNOWN : milliseconds * NANOS_PER_MS;
    }

    public static long          getNanoTime (long milliseconds, int nanosComponent) {
        return milliseconds == TIMESTAMP_UNKNOWN ?
                TIMESTAMP_UNKNOWN : getNanoTime(milliseconds) + nanosComponent;
    }
}
