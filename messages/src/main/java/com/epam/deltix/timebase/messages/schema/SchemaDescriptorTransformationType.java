package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.SchemaElement;

/**
 * Type of transformation applied to the descriptor.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.SchemaDescriptorTransformationType",
    title = "SchemaDescriptorTransformationType"
)
public enum SchemaDescriptorTransformationType {
  /**
   * Drop record operation.
   */
  @SchemaElement(
      name = "DROP_RECORD"
  )
  DROP_RECORD(0);

  private final int value;

  SchemaDescriptorTransformationType(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static SchemaDescriptorTransformationType valueOf(int number) {
    switch (number) {
      case 0: return DROP_RECORD;
      default: return null;
    }
  }

  public static SchemaDescriptorTransformationType strictValueOf(int number) {
    final SchemaDescriptorTransformationType value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'SchemaDescriptorTransformationType' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
