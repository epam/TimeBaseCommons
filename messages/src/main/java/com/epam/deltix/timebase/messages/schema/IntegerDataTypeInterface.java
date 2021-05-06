package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.Override;

/**
 * Schema definition of integer data type.
 */
public interface IntegerDataTypeInterface extends IntegerDataTypeInfo, IntegralDataTypeInterface {
  /**
   * Method nullifies all instance properties
   */
  @Override
  IntegerDataTypeInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  IntegerDataTypeInterface reset();

  @Override
  IntegerDataTypeInterface copyFrom(RecordInfo template);
}
