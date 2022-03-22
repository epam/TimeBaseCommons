package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.SchemaElement;

/**
 * Type of transformation applied to data field.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.SchemaFieldDataTransformationType",
    title = "SchemaFieldDataTransformationType"
)
public enum SchemaFieldDataTransformationType {
  /**
   * Convert data operation.
   */
  @SchemaElement(
      name = "CONVERT_DATA"
  )
  CONVERT_DATA(0),

  /**
   * Apply default value operation.
   */
  @SchemaElement(
      name = "SET_DEFAULT"
  )
  SET_DEFAULT(1),

  /**
   * Drop record operation.
   */
  @SchemaElement(
      name = "DROP_RECORD"
  )
  DROP_RECORD(2);

  private final int value;

  SchemaFieldDataTransformationType(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static SchemaFieldDataTransformationType valueOf(int number) {
    switch (number) {
      case 0: return CONVERT_DATA;
      case 1: return SET_DEFAULT;
      case 2: return DROP_RECORD;
      default: return null;
    }
  }

  public static SchemaFieldDataTransformationType strictValueOf(int number) {
    final SchemaFieldDataTransformationType value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'SchemaFieldDataTransformationType' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
