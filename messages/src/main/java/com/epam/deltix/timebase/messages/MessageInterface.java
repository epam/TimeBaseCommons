package com.epam.deltix.timebase.messages;

import java.lang.CharSequence;
import java.lang.Override;

/**
 * Base class for all messages that could be written in Timebase.
 */
public interface MessageInterface extends MessageInfo, RecordInterface {
  /**
   *
   * Gets message time in milliseconds that passed since January 1, 1970, 00:00:00 GMT
   *
   * @param value - Time Stamp Ms
   */
  void setTimeStampMs(long value);

  /**
   *
   * Gets message time in milliseconds that passed since January 1, 1970, 00:00:00 GMT
   *
   */
  void nullifyTimeStampMs();

  /**
   *
   * Gets message time in nanoseconds that passed since January 1, 1970, 00:00:00 GMT
   *
   * @param value - Nano Time
   */
  void setNanoTime(long value);

  /**
   *
   * Gets message time in nanoseconds that passed since January 1, 1970, 00:00:00 GMT
   *
   */
  void nullifyNanoTime();

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
   * Method nullifies all instance properties
   */
  @Override
  MessageInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  MessageInterface reset();

  @Override
  MessageInterface copyFrom(RecordInfo template);
}
