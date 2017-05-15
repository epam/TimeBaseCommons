package deltix.qsrv.hf.pub;

import java.lang.CharSequence;

/**
 * Base class for all messages that could be written in Timebase.
 */
public interface InstrumentMessageInterface extends InstrumentIdentity, InstrumentMessageInfo, RecordInterface {
  /**
   * Time in this field is measured in milliseconds that passed since January 1, 1970 UTC.
   * For inbound messages special constant {link TIMESTAMP_UNKNOWN} marks 'unknown' timestamp in which case TimeBase server stores message using current server time.
   * @param value - Time
   */
  void setTimeStampMs(long value);

  /**
   * Time in this field is measured in milliseconds that passed since January 1, 1970 UTC.
   * For inbound messages special constant {link TIMESTAMP_UNKNOWN} marks 'unknown' timestamp in which case TimeBase server stores message using current server time.
   */
  void nullifyTimeStampMs();

  /**
   * Nanoseconds component of timestamp. Generally speaking value must be in range from [ 0 to 999999 ],
   * but system handles any value (nanoTime simply added to timestamp converted to nanoseconds).
   * @param value - Nanoseconds Component
   */
  void setNanoTime(long value);

  /**
   * Nanoseconds component of timestamp. Generally speaking value must be in range from [ 0 to 999999 ],
   * but system handles any value (nanoTime simply added to timestamp converted to nanoseconds).
   */
  void nullifyNanoTime();

  /**
   * Instrument type.
   * @param value - Instrument Type
   */
  void setInstrumentType(InstrumentType value);

  /**
   * Instrument type.
   */
  void nullifyInstrumentType();

  /**
   * Instrument name.
   * @param value - Symbol
   */
  void setSymbol(CharSequence value);

  /**
   * Instrument name.
   */
  void nullifySymbol();

  /**
   * Deep copies content from src instance to this.
   * @param src source for copy.
   */
  InstrumentMessageInterface copyFrom(RecordInfo src);

  /**
   * Reset all instance field to their default states.
   * @return this.
   */
  InstrumentMessageInterface reset();

  /**
   * Set null to all fields of this instance.
   * @return this.
   */
  InstrumentMessageInterface nullify();
}
