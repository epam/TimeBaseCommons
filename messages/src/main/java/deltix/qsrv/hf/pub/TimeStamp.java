package deltix.qsrv.hf.pub;

import deltix.data.stream.TimeStampedMessage;

public class TimeStamp implements TimeStampedMessage {

    /** Number of nanos per millisecond (10^6) */
    public static final int     NANOS_PER_MS = 1000000;

    /** Time is measured in milliseconds that passed since January 1, 1970 UTC */
    //@Title ("Time")
    //@FieldType("TIMESTAMP")
    public long                 timestamp = TIMESTAMP_UNKNOWN;

    /** Nanoseconds part of the TimeStamp#timestamp */
    //@Title ("Nanoseconds Component")
    public int                  nanosComponent = 0;


    public static TimeStamp     fromMilliseconds(long timestamp) {
        TimeStamp ts = new TimeStamp();
        ts.setTime(timestamp);
        return ts;
    }

    public static TimeStamp     fromNanoseconds(long nanoseconds) {
        TimeStamp ts = new TimeStamp();
        ts.setNanoTime(nanoseconds);
        return ts;
    }

    public void                 setNanoTime(long nanoSeconds) {
        if (nanoSeconds != TIMESTAMP_UNKNOWN && nanoSeconds != Long.MAX_VALUE) {
            nanosComponent = (int) (nanoSeconds % TimeStamp.NANOS_PER_MS);
            timestamp = nanoSeconds / TimeStamp.NANOS_PER_MS;
        } else {
            nanosComponent = 0;
            timestamp = nanoSeconds;
        }
    }

    public static int                  getNanosComponent(long nstime) {
        return (int) (nstime % TimeStamp.NANOS_PER_MS);
    }

    /*
     *  Returns number of nanoseconds that passed since January 1, 1970 UTC
     */
    @Override
    public long                 getNanoTime() {
        return timestamp == TIMESTAMP_UNKNOWN || timestamp == Long.MAX_VALUE ? timestamp : (getNanoTime(timestamp) + nanosComponent);
    }

    /*
    *  Returns number of milliseconds that passed since January 1, 1970 UTC
    */
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
        return isUndefined(timestamp);
    }

    public static boolean       isUndefined(long timestamp) {
        return timestamp == TIMESTAMP_UNKNOWN;
    }

    public void                 setUndefined() {
        setTime(TIMESTAMP_UNKNOWN);
    }

    public static long          getMilliseconds (long nanoSeconds) {
        return nanoSeconds != TIMESTAMP_UNKNOWN && nanoSeconds != Long.MAX_VALUE ? nanoSeconds / TimeStamp.NANOS_PER_MS : nanoSeconds;
    }

    public static long          getNanoTime (long milliseconds) {
        if (milliseconds == TIMESTAMP_UNKNOWN || milliseconds == Long.MAX_VALUE)
            return milliseconds;

        return milliseconds * NANOS_PER_MS;
    }

    public static long          getNanoTime (long milliseconds, int nanosComponent) {
        return milliseconds == TIMESTAMP_UNKNOWN || milliseconds == Long.MAX_VALUE ? milliseconds : (getNanoTime(milliseconds) + nanosComponent);
    }
}
