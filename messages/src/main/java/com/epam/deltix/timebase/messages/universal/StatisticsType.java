package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.timebase.messages.SchemaElement;

/**
 * Represents type of event and its value for StatisticsEntry.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.universal.StatisticsType",
    title = "Statistics Type"
)
public enum StatisticsType {
  /**
   * Custom.
   */
  @SchemaElement(
      name = "CUSTOM"
  )
  CUSTOM(1),

  /**
   * Opening price.
   */
  @SchemaElement(
      name = "OPENING_PRICE"
  )
  OPENING_PRICE(2),

  /**
   * Closing price.
   */
  @SchemaElement(
      name = "CLOSING_PRICE"
  )
  CLOSING_PRICE(3),

  /**
   * Settlement price.
   */
  @SchemaElement(
      name = "SETTLEMENT_PRICE"
  )
  SETTLEMENT_PRICE(4),

  /**
   * Trading session high price.
   */
  @SchemaElement(
      name = "TRADING_SESSION_HIGH_PRICE"
  )
  TRADING_SESSION_HIGH_PRICE(5),

  /**
   * Trading session low price.
   */
  @SchemaElement(
      name = "TRADING_SESSION_LOW_PRICE"
  )
  TRADING_SESSION_LOW_PRICE(6),

  /**
   * Trading session vwap price.
   */
  @SchemaElement(
      name = "TRADING_SESSION_VWAP_PRICE"
  )
  TRADING_SESSION_VWAP_PRICE(7),

  /**
   * Trade volume.
   */
  @SchemaElement(
      name = "TRADE_VOLUME"
  )
  TRADE_VOLUME(8),

  /**
   * Open interest.
   */
  @SchemaElement(
      name = "OPEN_INTEREST"
  )
  OPEN_INTEREST(9),

  /**
   * Session high bid.
   */
  @SchemaElement(
      name = "SESSION_HIGH_BID"
  )
  SESSION_HIGH_BID(10),

  /**
   * Session low offer.
   */
  @SchemaElement(
      name = "SESSION_LOW_OFFER"
  )
  SESSION_LOW_OFFER(11),

  /**
   * Auction clearing price.
   */
  @SchemaElement(
      name = "AUCTION_CLEARING_PRICE"
  )
  AUCTION_CLEARING_PRICE(12),

  /**
   * Fixing priceq.
   */
  @SchemaElement(
      name = "FIXING_PRICE"
  )
  FIXING_PRICE(13);

  private final int value;

  StatisticsType(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static StatisticsType valueOf(int number) {
    switch (number) {
      case 1: return CUSTOM;
      case 2: return OPENING_PRICE;
      case 3: return CLOSING_PRICE;
      case 4: return SETTLEMENT_PRICE;
      case 5: return TRADING_SESSION_HIGH_PRICE;
      case 6: return TRADING_SESSION_LOW_PRICE;
      case 7: return TRADING_SESSION_VWAP_PRICE;
      case 8: return TRADE_VOLUME;
      case 9: return OPEN_INTEREST;
      case 10: return SESSION_HIGH_BID;
      case 11: return SESSION_LOW_OFFER;
      case 12: return AUCTION_CLEARING_PRICE;
      case 13: return FIXING_PRICE;
      default: return null;
    }
  }

  public static StatisticsType strictValueOf(int number) {
    final StatisticsType value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'StatisticsType' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
