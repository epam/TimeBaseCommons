package com.epam.deltix.timebase.messages.schema;

import java.lang.Override;

/**
 * Schema definition of char data type.
 */
public interface CharDataTypeInfo extends DataTypeInfo {
  /**
   * Method copies state to a given instance
   */
  @Override
  CharDataTypeInfo clone();
}
