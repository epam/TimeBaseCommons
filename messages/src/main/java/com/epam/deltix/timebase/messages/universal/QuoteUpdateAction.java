package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.timebase.messages.SchemaElement;

/**
 * Directs how to update an Order Book for L3 format.
 */
public enum QuoteUpdateAction {
  /**
   * Delete quote (meaning trader cancel it).
   */
  @SchemaElement(
      name = "CANCEL"
  )
  CANCEL(0),

  /**
   * Modify quote content (meaning that quote safe its position in order).
   */
  @SchemaElement(
      name = "MODIFY"
  )
  MODIFY(1),

  /**
   * Modify quote content (meaning that quote lose its position in order).
   */
  @SchemaElement(
      name = "REPLACE"
  )
  REPLACE(2);

  private final int value;

  QuoteUpdateAction(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static QuoteUpdateAction valueOf(int number) {
    switch (number) {
      case 0: return CANCEL;
      case 1: return MODIFY;
      case 2: return REPLACE;
      default: return null;
    }
  }

  public static QuoteUpdateAction strictValueOf(int number) {
    final QuoteUpdateAction value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'QuoteUpdateAction' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
