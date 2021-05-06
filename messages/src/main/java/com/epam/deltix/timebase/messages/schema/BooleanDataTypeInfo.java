package com.epam.deltix.timebase.messages.schema;

import java.lang.Override;

/**
 * Schema definition of boolean data type.
 */
public interface BooleanDataTypeInfo extends DataTypeInfo {
  /**
   * Method copies state to a given instance
   */
  @Override
  BooleanDataTypeInfo clone();
}
