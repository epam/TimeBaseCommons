package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.Override;

/**
 * Class which defines a transformation that is applied to the descriptor.
 */
public interface SchemaDescriptorTransformationInfo extends RecordInfo {
  /**
   * Defines the transformation type that was applied to the descriptor.
   * @return Transformation Type
   */
  SchemaDescriptorTransformationType getTransformationType();

  /**
   * Defines the transformation type that was applied to the descriptor.
   * @return true if Transformation Type is not null
   */
  boolean hasTransformationType();

  /**
   * Method copies state to a given instance
   */
  @Override
  SchemaDescriptorTransformationInfo clone();
}
