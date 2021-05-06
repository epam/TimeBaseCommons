package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.Override;

/**
 * Schema definition of time-of-day data type.
 */
public interface TimeOfDayDataTypeInterface extends TimeOfDayDataTypeInfo, DataTypeInterface {
  /**
   * Method nullifies all instance properties
   */
  @Override
  TimeOfDayDataTypeInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  TimeOfDayDataTypeInterface reset();

  @Override
  TimeOfDayDataTypeInterface copyFrom(RecordInfo template);
}
