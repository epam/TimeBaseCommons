package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.CharSequence;
import java.lang.Override;

/**
 * Class which defines a transformation that is applied to a data field.
 */
public interface SchemaFieldDataTransformationInfo extends RecordInfo {
  /**
   * Defines the transformation that was applied to the field.
   * @return Transformation Type
   */
  SchemaFieldDataTransformationType getTransformationType();

  /**
   * Defines the transformation that was applied to the field.
   * @return true if Transformation Type is not null
   */
  boolean hasTransformationType();

  /**
   * Defines the default value if TransformationType equals SET_DEFAULT.
   * @return Default Value
   */
  CharSequence getDefaultValue();

  /**
   * Defines the default value if TransformationType equals SET_DEFAULT.
   * @return true if Default Value is not null
   */
  boolean hasDefaultValue();

  /**
   * Method copies state to a given instance
   */
  @Override
  SchemaFieldDataTransformationInfo clone();
}
