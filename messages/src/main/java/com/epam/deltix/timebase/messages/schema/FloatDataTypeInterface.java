package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.Override;

/**
 * Schema definition of float data type.
 */
public interface FloatDataTypeInterface extends FloatDataTypeInfo, IntegralDataTypeInterface {
  /**
   * Scale.
   * @param value - Scale
   */
  void setScale(short value);

  /**
   * Scale.
   */
  void nullifyScale();

  /**
   * Method nullifies all instance properties
   */
  @Override
  FloatDataTypeInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  FloatDataTypeInterface reset();

  @Override
  FloatDataTypeInterface copyFrom(RecordInfo template);
}
