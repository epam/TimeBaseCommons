package deltix.qsrv.hf.pub;

import deltix.data.stream.TimeStampedMessage;
import deltix.util.lang.Util;

/**
 *
 */
public class InstrumentMessage
    implements TimeStampedMessage, InstrumentMessageInterface, InstrumentIdentityInterface, Comparable<InstrumentMessage> {
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

//    @Deprecated // use clone(), copyTo(), copyFrom ()
//    public InstrumentMessage      copy (boolean deep) {
//        try {
//            InstrumentMessage     out = getClass ().newInstance ();
//            out.copy (this, deep);
//            return (out);
//        } catch (Throwable x) {
//            throw new RuntimeException (x);
//        }
//    }

    public void                 setNanoTime(long nanos) {
        if (nanos != TIMESTAMP_UNKNOWN) {
            nanoTime = (int) (nanos % TimeStamp.NANOS_PER_MS);
            timestamp = nanos / TimeStamp.NANOS_PER_MS;
        } else {
            timestamp = TIMESTAMP_UNKNOWN;
            nanoTime = 0;
        }
    }

    public int                  compareTime(InstrumentMessage time) {
        return timestamp == time.timestamp ?
                Util.compare(nanoTime, time.nanoTime) : (timestamp > time.timestamp ? 1 : -1);
    }

    public String               getTimeString() {
        return formatNanos(timestamp, (int)nanoTime);
    }

    public static String                formatNanos (long milliseconds, int nanos) {
        if (milliseconds == Long.MIN_VALUE)
            return ("<null>");

        return TicksFormat.format(milliseconds, nanos);
    }

    /**
     * Deep copies content from src instance to this.
     * @param template source for copy.
     */
    @Override
    public InstrumentMessage copyFrom(RecordInfo template) {
        if (template instanceof InstrumentMessageInfo) {
            InstrumentMessageInfo t = (InstrumentMessageInfo) template;
            setTimeStampMs(t.getTimeStampMs());
            setNanoTime(t.getNanoTime());

            // set symbol should copy value
            if (symbol instanceof StringBuilder)
                ((StringBuilder)symbol).setLength(0);
            else
                setSymbol(new StringBuilder());
            ((StringBuilder)symbol).append(t.getSymbol());

            setInstrumentType(t.getInstrumentType());
        }
        return this;
    }

    /**
     * Creates new instance of this class.
     */
    protected InstrumentMessage createInstance() {
        return new InstrumentMessage();
    }

    /**
     * Creates copy of this instance.
     * @return copy.
     */
    @Override
    public InstrumentMessage clone() {
        InstrumentMessage c = createInstance();
        c.copyFrom(this);
        return c;
    }

    //4.3//@Title ("Type") @PrimaryKey
    public InstrumentType instrumentType = InstrumentType.CUSTOM;

    //4.3//@Title ("Symbol") @PrimaryKey
    public CharSequence         symbol = "";

    /**
     * Instrument name.
     * @return Symbol
     */
    public CharSequence         getSymbol () {
      return symbol;
    }

    /**
     * Instrument name.
     * @param value - Symbol
     */
    public void setSymbol(CharSequence value) {
      this.symbol = value;
    }

    /**
     * Instrument name.
     * @return true if Symbolis not null
     */
    public boolean hasSymbol() {
      return symbol != null;
    }

    /**
     * Instrument name.
     */
    public void nullifySymbol() {
      this.symbol = null;
    }
    /**
     * Instrument type.
     * @return Instrument Type
     */
    public InstrumentType getInstrumentType() {
      return instrumentType;
    }

    /**
     * Instrument type.
     * @param value - Instrument Type
     */
    public void setInstrumentType(InstrumentType value) {
      this.instrumentType = value;
    }

    /**
     * Instrument type.
     * @return true if Instrument Typeis not null
     */
    public boolean hasInstrumentType() {
        return instrumentType != null;
    }

    /**
     * Instrument type.
     */
    public void nullifyInstrumentType() {
      this.instrumentType = null;
    }

//    /**
//     * Method copies state of given template into this object
//     * @param deep if true performs deep copy of mutable properties
//     */
//    @Deprecated
//    public void                 copy (Object template, boolean deep) {
//
//        if (template instanceof InstrumentMessage) {
//            InstrumentMessage t = (InstrumentMessage) template;
//            timestamp = t.timestamp;
//            nanoTime = t.nanoTime;
//        }
//
//        if (template instanceof InstrumentIdentity) {
//            InstrumentIdentity   sm = (InstrumentIdentity) template;
//
//            instrumentType = sm.getInstrumentType ();
//            symbol = sm.getSymbol ();
//            if (deep && symbol != null)
//                symbol = symbol.toString ();
//        }
//    }

    public int                  compareTime(TimeStamp time) {
        // Do not change. JIT compile this implementation into highly optimized branch free code with 4 cmovnl instructions.
        // If nanos does not belong to 0..999999 range or timestamp does not belong to 0..2^53
        // then timestamp comparison result is Undefined
        long nanos1 = timestamp * TimeStamp.NANOS_PER_MS + nanoTime;
        long nanos2 = time.timestamp * TimeStamp.NANOS_PER_MS + time.nanosComponent;
        nanos1 -= nanos2;
        return (nanos1 > 0 ? 1 : 0) - (nanos1 < 0 ? 1 : 0);
    }
    
    @Override
    public int                  compareTo(InstrumentMessage o) {
        if (timestamp == o.timestamp) {
            if (nanoTime == o.nanoTime) {
                int r = Util.fastCompare(symbol, o.symbol);
                return r != 0 ? r : instrumentType.compareTo(o.instrumentType);
            }

            return nanoTime > o.nanoTime ? 1 : -1;
        }

        return timestamp > o.timestamp ? 1 : -1;
    }

    public String               toString () {
        return (
            String.format (
                "%s,%s,%s,%s",
                getClass ().getSimpleName (),
                instrumentType != null ? instrumentType.name () : "<null>",
                symbol != null ? symbol.toString() : "<null>",
                getTimeString()
            )
        );
    }

    /**
     * Reset all instance field to their default states.
     * @return this.
     */
    @Override
    public InstrumentMessage reset() {
        instrumentType = InstrumentType.CUSTOM;
        symbol = "";
        timestamp = TIMESTAMP_UNKNOWN;
        nanoTime = 0;
        return this;
    }

    /**
     * Set null to all fields of this instance.
     * @return this.
     */
    @Override
    public InstrumentMessage nullify() {
        nullifyInstrumentType();
        nullifySymbol();
        nullifyTimeStampMs();
        nullifyNanoTime();
        return this;
    }
}
