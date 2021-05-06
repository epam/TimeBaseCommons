package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.Override;

/**
 * Schema definition of date-time data type.
 */
public interface DateTimeDataTypeInterface extends DateTimeDataTypeInfo, DataTypeInterface {
  /**
   * Method nullifies all instance properties
   */
  @Override
  DateTimeDataTypeInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  DateTimeDataTypeInterface reset();

  @Override
  DateTimeDataTypeInterface copyFrom(RecordInfo template);
}
