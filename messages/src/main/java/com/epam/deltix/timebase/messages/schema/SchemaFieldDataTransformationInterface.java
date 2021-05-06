package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import java.lang.CharSequence;
import java.lang.Override;

/**
 * Class which defines a transformation that is applied to a data field.
 */
public interface SchemaFieldDataTransformationInterface extends SchemaFieldDataTransformationInfo, RecordInterface {
  /**
   * Defines the transformation that was applied to the field.
   * @param value - Transformation Type
   */
  void setTransformationType(SchemaFieldDataTransformationType value);

  /**
   * Defines the transformation that was applied to the field.
   */
  void nullifyTransformationType();

  /**
   * Defines the default value if TransformationType equals SET_DEFAULT.
   * @param value - Default Value
   */
  void setDefaultValue(CharSequence value);

  /**
   * Defines the default value if TransformationType equals SET_DEFAULT.
   */
  void nullifyDefaultValue();

  /**
   * Method nullifies all instance properties
   */
  @Override
  SchemaFieldDataTransformationInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  SchemaFieldDataTransformationInterface reset();

  @Override
  SchemaFieldDataTransformationInterface copyFrom(RecordInfo template);
}
