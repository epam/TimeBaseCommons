package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.Override;

/**
 * Schema definition of boolean data type.
 */
public interface BooleanDataTypeInterface extends BooleanDataTypeInfo, DataTypeInterface {
  /**
   * Method nullifies all instance properties
   */
  @Override
  BooleanDataTypeInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  BooleanDataTypeInterface reset();

  @Override
  BooleanDataTypeInterface copyFrom(RecordInfo template);
}
