package com.epam.deltix.timebase.messages;

import com.epam.deltix.util.lang.Util;
import java.lang.reflect.InvocationTargetException;

/**
 * Base class for all messages that could be written in Timebase.
 */
public class InstrumentMessage
    implements TimeStampedMessage, MessageInterface, IdentityKey, IdentityKeyInterface, Comparable<InstrumentMessage> {

    /**
     * Time in this field is measured in milliseconds that passed since January 1, 1970 UTC.\
     * For inbound messages special constant {TIMESTAMP_UNKNOWN} marks 'unknown' timestamp in which case TimeBase server stores message using current server time.
     * See also {@link #nanoTime}.
     */
    protected long timestamp = TIMESTAMP_UNKNOWN;

    /**
     * Nanoseconds component of timestamp. Generally speaking value must be in range from [ 0 to 999999 ],
     * but system handles any value (nanoTime simply added to timestamp converted to nanoseconds).
     * Use {@link TimeStamp#getNanoTime(long, int)} helper method calculate message timestamps with nanosecond resolution in the same manner as TimeBase.
     *
     * Note this field is ignored when {@link #timestamp} is set to {TIMESTAMP_UNKNOWN}.
     */
    protected int nanoTime = 0;

    /**
     * Gets message time in milliseconds that passed since January 1, 1970, 00:00:00 GMT
     * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this message.
     */
    public long getTimeStampMs() {
        return timestamp;
    }

    /**
     * Sets this message to represent a point in time that is
     * <code>value</code> milliseconds after January 1, 1970 00:00:00 GMT.
     *
     * For inbound messages special constant {link TIMESTAMP_UNKNOWN} marks 'unknown' timestamp
     * in which case TimeBase server stores message using current server time.
     *
     * @param   value   the number of milliseconds.
     */

    public void setTimeStampMs(long value) {
        this.timestamp = value;
    }

    /**
     * Indicated that this message has defined point in time.
     */
    public boolean hasTimeStampMs() {
        return timestamp != TIMESTAMP_UNKNOWN;
    }

    /**
     * Sets this message time as 'undefined' {link TIMESTAMP_UNKNOWN}
     */
    public void nullifyTimeStampMs() {
        this.timestamp = TIMESTAMP_UNKNOWN;
    }

    /**
     * Gets message time in nanoseconds that passed since January 1, 1970, 00:00:00 GMT
     *
     * @return the number of nanoseconds since January 1, 1970, 00:00:00 GMT represented by this message.
     */
    @Override
    public long                 getNanoTime() {
        return TimeStamp.getNanoTime(timestamp, nanoTime);
    }

    /**
     * Indicated that this message has defined nanoseconds component of the time.
     */
    public boolean hasNanoTime() {
        return nanoTime != 0;
    }

    /**
     * Sets this message time as 'undefined' {link TIMESTAMP_UNKNOWN}
     */
    public void nullifyNanoTime() {
        this.nanoTime = 0;
        this.timestamp = TIMESTAMP_UNKNOWN;
    }

    /**
     * Sets this message to represent a point in time that is
     * <code>value</code> nanoseconds after January 1, 1970 00:00:00 GMT.
     *
     * For inbound messages special constant {link TIMESTAMP_UNKNOWN} marks 'unknown' timestamp
     * in which case TimeBase server stores message using current server time.
     *
     * @param   value   the number of nanoseconds.
     */
    public void                 setNanoTime(long value) {
        if (value != TIMESTAMP_UNKNOWN) {
            nanoTime = (int) (value % TimeStamp.NANOS_PER_MS);
            timestamp = value / TimeStamp.NANOS_PER_MS;
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
        if (template instanceof MessageInfo) {
            MessageInfo t = (MessageInfo) template;
            setTimeStampMs(t.getTimeStampMs());
            setNanoTime(t.getNanoTime());

            // set symbol should copy value
            if (symbol instanceof StringBuilder)
                ((StringBuilder)symbol).setLength(0);
            else
                setSymbol(new StringBuilder());
            ((StringBuilder)symbol).append(t.getSymbol());
        }
        return this;
    }

    /**
     * Creates new instance of this class.
     */
    protected InstrumentMessage createInstance() {
        try {
            return Util.newInstance(getClass());
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Cannot create instance for the " + getClass());
        }
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

    protected CharSequence         symbol = "";

    /**
     * Instrument name.
     * @return String representation of identity name.
     */
    public final CharSequence         getSymbol () {
        return symbol;
    }

    /**
     * Sets Instrument name.
     * @param value - Symbol
     */
    public final void setSymbol(CharSequence value) {
        this.symbol = value;
    }

    /**
     * Returns true, if instrument name is defined.
     * @return true if name is not null
     */
    public final boolean hasSymbol() {
      return symbol != null;
    }

    /**
     * Set instrument name to 'undefined' value
     */
    public final void nullifySymbol() {
      this.symbol = null;
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
            if (nanoTime == o.nanoTime)
                return Util.fastCompare(symbol, o.symbol);
            return nanoTime > o.nanoTime ? 1 : -1;
        }

        return timestamp > o.timestamp ? 1 : -1;
    }

    public String               toString () {
        StringBuilder sb = new StringBuilder();
        return toString(sb).toString();
    }

    public StringBuilder        toString(StringBuilder str) {
        str.append("{ \"$type\": \"InstrumentMessage\"");

        if (hasSymbol())
            str.append(", \"symbol\": \"").append(symbol).append("\"");

        if (hasTimeStampMs())
            str.append(", \"timestamp\": \"").append(getTimeString()).append("\"");

        str.append("}");

        return str;
    }


    /**
     * Indicates whether some other object is "equal to" this one.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MessageInfo))
            return false;
        MessageInfo other = (MessageInfo)obj;
        if (getNanoTime() != other.getNanoTime())
            return false;
        if ((symbol == null) != (other.getSymbol() == null))
            return false;
        if (symbol != null && !CharSequenceUtils.equals(symbol, other.getSymbol()))
            return false;

        return true;
    }


    /**
     * Reset all instance field to their default states.
     * @return this.
     */
    @Override
    public InstrumentMessage reset() {
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
        nullifySymbol();
        nullifyTimeStampMs();
        nullifyNanoTime();
        return this;
    }
}
