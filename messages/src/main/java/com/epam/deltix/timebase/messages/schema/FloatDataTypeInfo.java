package com.epam.deltix.timebase.messages.schema;

import java.lang.Override;

/**
 * Schema definition of float data type.
 */
public interface FloatDataTypeInfo extends IntegralDataTypeInfo {
  /**
   * Scale.
   * @return Scale
   */
  short getScale();

  /**
   * Scale.
   * @return true if Scale is not null
   */
  boolean hasScale();

  /**
   * Method copies state to a given instance
   */
  @Override
  FloatDataTypeInfo clone();
}
