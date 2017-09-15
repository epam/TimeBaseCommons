package deltix.qsrv.hf.pub;

import java.lang.CharSequence;

/**
 * Base class for all messages that could be written in Timebase.
 */
public interface InstrumentMessageInfo extends RecordInfo {
  /**
   * Time in this field is measured in milliseconds that passed since January 1, 1970 UTC.
   * For inbound messages special constant {link TIMESTAMP_UNKNOWN} marks 'unknown' timestamp in which case TimeBase server stores message using current server time.
   * @return Time
   */
  long getTimeStampMs();

  /**
   * Time in this field is measured in milliseconds that passed since January 1, 1970 UTC.
   * For inbound messages special constant {link TIMESTAMP_UNKNOWN} marks 'unknown' timestamp in which case TimeBase server stores message using current server time.
   * @return true if Timeis not null
   */
  boolean hasTimeStampMs();

  /**
   * Nanoseconds component of timestamp. Generally speaking value must be in range from [ 0 to 999999 ],
   * but system handles any value (nanoTime simply added to timestamp converted to nanoseconds).
   * @return Nanoseconds Component
   */
  long getNanoTime();

  /**
   * Nanoseconds component of timestamp. Generally speaking value must be in range from [ 0 to 999999 ],
   * but system handles any value (nanoTime simply added to timestamp converted to nanoseconds).
   * @return true if Nanoseconds Componentis not null
   */
  boolean hasNanoTime();

  /**
   * Instrument type.
   * @return Instrument Type
   */
  InstrumentType getInstrumentType();

  /**
   * Instrument type.
   * @return true if Instrument Typeis not null
   */
  boolean hasInstrumentType();

  /**
   * Instrument name.
   * @return Symbol
   */
  CharSequence getSymbol();

  /**
   * Instrument name.
   * @return true if Symbolis not null
   */
  boolean hasSymbol();

  /**
   * Creates copy of this instance.
   * @return copy.
   */
  @Override
  InstrumentMessageInfo clone();
}
