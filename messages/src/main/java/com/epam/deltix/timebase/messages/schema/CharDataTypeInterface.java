package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.Override;

/**
 * Schema definition of char data type.
 */
public interface CharDataTypeInterface extends CharDataTypeInfo, DataTypeInterface {
  /**
   * Method nullifies all instance properties
   */
  @Override
  CharDataTypeInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  CharDataTypeInterface reset();

  @Override
  CharDataTypeInterface copyFrom(RecordInfo template);
}
