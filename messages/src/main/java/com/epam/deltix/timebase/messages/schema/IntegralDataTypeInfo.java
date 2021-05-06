package com.epam.deltix.timebase.messages.schema;

import java.lang.CharSequence;
import java.lang.Override;

/**
 * This is a base class for numeric data types.
 */
public interface IntegralDataTypeInfo extends DataTypeInfo {
  /**
   * String definition for minimum constraint.
   * @return Min Value
   */
  CharSequence getMinValue();

  /**
   * String definition for minimum constraint.
   * @return true if Min Value is not null
   */
  boolean hasMinValue();

  /**
   * String definition for maximum constraint.
   * @return Max Value
   */
  CharSequence getMaxValue();

  /**
   * String definition for maximum constraint.
   * @return true if Max Value is not null
   */
  boolean hasMaxValue();

  /**
   * Method copies state to a given instance
   */
  @Override
  IntegralDataTypeInfo clone();
}
