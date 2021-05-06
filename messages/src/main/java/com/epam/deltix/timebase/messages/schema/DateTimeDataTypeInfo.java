package com.epam.deltix.timebase.messages.schema;

import java.lang.Override;

/**
 * Schema definition of date-time data type.
 */
public interface DateTimeDataTypeInfo extends DataTypeInfo {
  /**
   * Method copies state to a given instance
   */
  @Override
  DateTimeDataTypeInfo clone();
}
