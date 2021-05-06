package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import java.lang.Override;

/**
 * Class which defines a transformation that is applied to the descriptor.
 */
public interface SchemaDescriptorTransformationInterface extends SchemaDescriptorTransformationInfo, RecordInterface {
  /**
   * Defines the transformation type that was applied to the descriptor.
   * @param value - Transformation Type
   */
  void setTransformationType(SchemaDescriptorTransformationType value);

  /**
   * Defines the transformation type that was applied to the descriptor.
   */
  void nullifyTransformationType();

  /**
   * Method nullifies all instance properties
   */
  @Override
  SchemaDescriptorTransformationInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  SchemaDescriptorTransformationInterface reset();

  @Override
  SchemaDescriptorTransformationInterface copyFrom(RecordInfo template);
}
