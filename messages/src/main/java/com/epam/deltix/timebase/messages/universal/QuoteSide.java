package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.timebase.messages.SchemaElement;

/**
 * Side of quote. Bid and Ask .
 */
public enum QuoteSide {
  /**
   * Bid for quote.
   */
  @SchemaElement(
      name = "BID"
  )
  BID(0),

  /**
   * Ask for quote.
   */
  @SchemaElement(
      name = "ASK"
  )
  ASK(1);

  private final int value;

  QuoteSide(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static QuoteSide valueOf(int number) {
    switch (number) {
      case 0: return BID;
      case 1: return ASK;
      default: return null;
    }
  }

  public static QuoteSide strictValueOf(int number) {
    final QuoteSide value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'QuoteSide' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
