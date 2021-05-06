package com.epam.deltix.timebase.messages.schema;

import java.lang.Override;

/**
 * Schema definition of integer data type.
 */
public interface IntegerDataTypeInfo extends IntegralDataTypeInfo {
  /**
   * Method copies state to a given instance
   */
  @Override
  IntegerDataTypeInfo clone();
}
