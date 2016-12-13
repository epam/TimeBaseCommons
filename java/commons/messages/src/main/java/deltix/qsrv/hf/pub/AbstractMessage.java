package deltix.qsrv.hf.pub;

import deltix.data.stream.TimeStampedMessage;
import deltix.util.lang.Util;
import deltix.util.memory.MemorySizeEstimator;
import deltix.util.time.GMT;

/**
 *
 */
public class AbstractMessage implements
    TimeStampedMessage, MemorySizeEstimator, AbstractMessageInterface
{
    /**
     * Time in this field is measured in milliseconds that passed since January 1, 1970 UTC.
     * For inbound messages special constant {TIMESTAMP_UNKNOWN} marks 'unknown' timestamp in which case TimeBase server stores message using current server time.
     * See also {@link #nanoTime}.
     */
    //@Title("Time")
    //@FieldType("TIMESTAMP")
    protected long timestamp = TIMESTAMP_UNKNOWN;

    /**
     * Nanoseconds component of timestamp. Generally speaking value must be in range from [ 0 to 999999 ],
     * but system handles any value (nanoTime simply added to timestamp converted to nanoseconds).
     * Use {@link TimeStamp#getNanoTime(long, int)} helper method calculate message timestamps with nanosecond resolution in the same manner as TimeBase.
     *
     * Note this field is ignored when {@link #timestamp} is set to {TIMESTAMP_UNKNOWN}.
     */
    //@Title ("Nanoseconds Component")
    protected int nanoTime = 0;

    /**
     * Time in this field is measured in milliseconds that passed since January 1, 1970 UTC.
     * For inbound messages special constant {link TIMESTAMP_UNKNOWN} marks 'unknown' timestamp in which case TimeBase server stores message using current server time.
     * @return Time
     */
    public long getTimeStampMs() {
      return timestamp;
    }

    /**
     * Time in this field is measured in milliseconds that passed since January 1, 1970 UTC.
     * For inbound messages special constant {link TIMESTAMP_UNKNOWN} marks 'unknown' timestamp in which case TimeBase server stores message using current server time.
     * @param value - Time
     */
    public void setTimeStampMs(long value) {
      this.timestamp = value;
    }

    /**
     * Time in this field is measured in milliseconds that passed since January 1, 1970 UTC.
     * For inbound messages special constant {link TIMESTAMP_UNKNOWN} marks 'unknown' timestamp in which case TimeBase server stores message using current server time.
     * @return true if Timeis not null
     */
    public boolean hasTimeStampMs() {
      return timestamp != TIMESTAMP_UNKNOWN;
    }

    /**
     * Time in this field is measured in milliseconds that passed since January 1, 1970 UTC.
     * For inbound messages special constant {link TIMESTAMP_UNKNOWN} marks 'unknown' timestamp in which case TimeBase server stores message using current server time.
     */
    public void nullifyTimeStampMs() {
      this.timestamp = TIMESTAMP_UNKNOWN;
    }

    /**
     * Returns message time measured in nanoseconds that passed since January 1, 1970 UTC.
     * @return Nanoseconds
     */
    @Override
    public long                 getNanoTime() {
        return TimeStamp.getNanoTime(timestamp, nanoTime);
    }

    /**
     * Nanoseconds component of timestamp. Generally speaking value must be in range from [ 0 to 999999 ],
     * but system handles any value (nanosComponent simply added to timestamp converted to nanoseconds).
     * @return true if Nanoseconds Componentis not null
     */
    public boolean hasNanoTime() {
      return nanoTime != 0;
    }

    /**
     * Nanoseconds component of timestamp. Generally speaking value must be in range from [ 0 to 999999 ],
     * but system handles any value (nanosComponent simply added to timestamp converted to nanoseconds).
     */
    public void nullifyNanoTime() {
        this.nanoTime = 0;
        this.timestamp = TIMESTAMP_UNKNOWN;
    }

    @Deprecated // use clone(), copyTo(), copyFrom ()
    public AbstractMessage      copy (boolean deep) {
        try {
            AbstractMessage     out = getClass ().newInstance ();
            out.copy (this, deep);
            return (out);
        } catch (Throwable x) {
            throw new RuntimeException (x);
        }
    }

    /**
     * Method copies state of given template into this object
     * @param deep if true performs deep copy of mutable properties
     */
    @Deprecated  // use clone(), copyTo(), copyFrom ()
    public void                 copy (Object template, boolean deep) {
        if (template instanceof AbstractMessage) {
            AbstractMessage t = (AbstractMessage) template;
            timestamp = t.timestamp;
            nanoTime = t.nanoTime;
        }
    }

    public long                 getSizeInMemory () {
        return (OBJECT_OVERHEAD + SIZE_OF_LONG);
    }

    public void                 setNanoTime(long nanos) {
        if (nanos != TIMESTAMP_UNKNOWN) {
            nanoTime = (int) (nanos % TimeStamp.NANOS_PER_MS);
            timestamp = nanos / TimeStamp.NANOS_PER_MS;
        } else {
            timestamp = TIMESTAMP_UNKNOWN;
            nanoTime = 0;
        }
    }

    public int                  compareTime(AbstractMessage time) {
        return timestamp == time.timestamp ?
                Util.compare(nanoTime, time.nanoTime) : (timestamp > time.timestamp ? 1 : -1);
    }

    public String               getTimeString() {
        return GMT.formatNanos(timestamp, (int)nanoTime);
    }

    /**
     * Deep copies content from src instance to this.
     * @param src source for copy.
     */
    public RecordInterface copyFrom(RecordInfo src) {
        src.copyTo(this);
        return this;
    }

    /**
     * Reset all instance field to their default states.
     * @return this.
     */
    public RecordInterface reset() {
        timestamp = TIMESTAMP_UNKNOWN;
        nanoTime = 0;
        return this;
    }

    /**
     * Set null to all fields of this instance.
     * @return this.
     */
    public RecordInterface nullify() {
        nullifyNanoTime();
        nullifyTimeStampMs();
        return this;
    }

    /**
     * Deep copies content of this instance to destination instance.
     * @param dst destination for copy.
     */
    public void copyTo(RecordInterface dst) {
        AbstractMessageInterface dstCasted = (AbstractMessageInterface)dst;
        dstCasted.setNanoTime(nanoTime);
        dstCasted.setTimeStampMs(timestamp);
    }

    /**
     * Creates copy of this instance.
     * @return copy.
     */
    public RecordInterface clone() {
        AbstractMessage c = new AbstractMessage();
        copyTo(c);
        return c;
    }
}
