package com.epam.deltix.timebase.messages.service;

import com.epam.deltix.timebase.messages.SchemaElement;

/**
 * Security feed status.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.service.FeedStatus",
    title = "Security Feed Status"
)
public enum FeedStatus {
  /**
   * Feed for this security is available again.
   */
  @SchemaElement(
      name = "AVAILABLE"
  )
  AVAILABLE(0),

  /**
   * Feed for this security and exchange code is no longer available.
   */
  @SchemaElement(
      name = "NOT_AVAILABLE"
  )
  NOT_AVAILABLE(1);

  private final int value;

  FeedStatus(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static FeedStatus valueOf(int number) {
    switch (number) {
      case 0: return AVAILABLE;
      case 1: return NOT_AVAILABLE;
      default: return null;
    }
  }

  public static FeedStatus strictValueOf(int number) {
    final FeedStatus value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'FeedStatus' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
