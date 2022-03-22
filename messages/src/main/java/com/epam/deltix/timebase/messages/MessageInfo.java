package com.epam.deltix.timebase.messages;

/**
 * Base class for all messages that could be written in Timebase.
 */
public interface MessageInfo extends RecordInfo {
  /**
   *
   * Gets message time in milliseconds that passed since January 1, 1970, 00:00:00 GMT
   *
   * @return Time Stamp Ms
   */
  long getTimeStampMs();

  /**
   *
   * Gets message time in milliseconds that passed since January 1, 1970, 00:00:00 GMT
   *
   * @return true if Time Stamp Ms is not null
   */
  boolean hasTimeStampMs();

  /**
   *
   * Gets message time in nanoseconds that passed since January 1, 1970, 00:00:00 GMT
   *
   * @return Nano Time
   */
  long getNanoTime();

  /**
   *
   * Gets message time in nanoseconds that passed since January 1, 1970, 00:00:00 GMT
   *
   * @return true if Nano Time is not null
   */
  boolean hasNanoTime();

  /**
   * Instrument name.
   * @return Symbol
   */
  CharSequence getSymbol();

  /**
   * Instrument name.
   * @return true if Symbol is not null
   */
  boolean hasSymbol();

  /**
   * Method copies state to a given instance
   */
  @Override
  MessageInfo clone();
}
