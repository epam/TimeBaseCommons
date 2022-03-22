package com.epam.deltix.timebase.messages;

/**
 * Most financial market-related messages subclass this abstract class.
 */
public interface MarketMessageInfo extends MessageInfo {
  /**
   * Exchange Time is measured in milliseconds that passed since January 1, 1970 UTC
   * @return Original Timestamp
   */
  long getOriginalTimestamp();

  /**
   * Exchange Time is measured in milliseconds that passed since January 1, 1970 UTC
   * @return true if Original Timestamp is not null
   */
  boolean hasOriginalTimestamp();

  /**
   * Identifies currency in which quantity is denominated (for currencies this must be base or term currency)
   * using 'alphanumeric' encoding
   * @return Currency
   */
  long getCurrency();

  /**
   * Identifies currency in which quantity is denominated (for currencies this must be base or term currency)
   * using 'alphanumeric' encoding
   * @return true if Currency is not null
   */
  boolean hasCurrency();

  /**
   * Market specific identifier of the given event in a sequence of market events.
   * @return Sequence Number
   */
  long getSequenceNumber();

  /**
   * Market specific identifier of the given event in a sequence of market events.
   * @return true if Sequence Number is not null
   */
  boolean hasSequenceNumber();

  /**
   * Identifies market data source. Different sessions of same connector
   * to a same data provider should have different id.
   * @return Source Id
   */
  long getSourceId();

  /**
   * Identifies market data source. Different sessions of same connector
   * to a same data provider should have different id.
   * @return true if Source Id is not null
   */
  boolean hasSourceId();

  /**
   * Method copies state to a given instance
   */
  @Override
  MarketMessageInfo clone();
}
