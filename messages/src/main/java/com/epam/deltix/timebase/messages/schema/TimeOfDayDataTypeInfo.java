package com.epam.deltix.timebase.messages.schema;

import java.lang.Override;

/**
 * Schema definition of time-of-day data type.
 */
public interface TimeOfDayDataTypeInfo extends DataTypeInfo {
  /**
   * Method copies state to a given instance
   */
  @Override
  TimeOfDayDataTypeInfo clone();
}
