package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.CharSequence;
import java.lang.Override;

/**
 * This is a base class for numeric data types.
 */
public interface IntegralDataTypeInterface extends IntegralDataTypeInfo, DataTypeInterface {
  /**
   * String definition for minimum constraint.
   * @param value - Min Value
   */
  void setMinValue(CharSequence value);

  /**
   * String definition for minimum constraint.
   */
  void nullifyMinValue();

  /**
   * String definition for maximum constraint.
   * @param value - Max Value
   */
  void setMaxValue(CharSequence value);

  /**
   * String definition for maximum constraint.
   */
  void nullifyMaxValue();

  /**
   * Method nullifies all instance properties
   */
  @Override
  IntegralDataTypeInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  IntegralDataTypeInterface reset();

  @Override
  IntegralDataTypeInterface copyFrom(RecordInfo template);
}
