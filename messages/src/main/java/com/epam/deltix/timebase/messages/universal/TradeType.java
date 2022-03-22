package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.timebase.messages.OldElementName;
import com.epam.deltix.timebase.messages.SchemaElement;

/**
 * Explains the meaning of the given price and/or size.
 */
public enum TradeType {
  /**
   * Regular trade.
   */
  @SchemaElement(
      name = "REGULAR_TRADE"
  )
  REGULAR_TRADE(0),

  /**
   * Auction price.
   */
  @SchemaElement(
      name = "AUCTION_CLEARING_PRICE"
  )
  AUCTION_CLEARING_PRICE(1),

  /**
   * Trade correction.
   */
  @SchemaElement(
      name = "CORRECTION"
  )
  CORRECTION(2),

  /**
   * Trade cancellation.
   */
  @OldElementName("CANCELATION")
  @SchemaElement(
      name = "CANCELLATION"
  )
  CANCELLATION(3),

  /**
   * Not regular trade of unknown type.
   */
  @SchemaElement(
      name = "UNKNOWN"
  )
  UNKNOWN(20);

  private final int value;

  TradeType(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static TradeType valueOf(int number) {
    switch (number) {
      case 0: return REGULAR_TRADE;
      case 1: return AUCTION_CLEARING_PRICE;
      case 2: return CORRECTION;
      case 3: return CANCELLATION;
      case 20: return UNKNOWN;
      default: return null;
    }
  }

  public static TradeType strictValueOf(int number) {
    final TradeType value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'TradeType' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
