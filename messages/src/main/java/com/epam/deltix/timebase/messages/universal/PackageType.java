package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.timebase.messages.SchemaElement;

/**
 * Package type needs to distinguish between incremental changes and different types of snapshot.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.universal.PackageType",
    title = "Package Type"
)
public enum PackageType {
  /**
   * Vendor snapshot.
   */
  @SchemaElement(
      name = "VENDOR_SNAPSHOT"
  )
  VENDOR_SNAPSHOT(0),

  /**
   * Periodical snapshot.
   */
  @SchemaElement(
      name = "PERIODICAL_SNAPSHOT"
  )
  PERIODICAL_SNAPSHOT(1),

  /**
   * Incremental update.
   */
  @SchemaElement(
      name = "INCREMENTAL_UPDATE"
  )
  INCREMENTAL_UPDATE(2);

  private final int value;

  PackageType(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static PackageType valueOf(int number) {
    switch (number) {
      case 0: return VENDOR_SNAPSHOT;
      case 1: return PERIODICAL_SNAPSHOT;
      case 2: return INCREMENTAL_UPDATE;
      default: return null;
    }
  }

  public static PackageType strictValueOf(int number) {
    final PackageType value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'PackageType' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
