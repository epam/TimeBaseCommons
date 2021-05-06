package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.Override;

/**
 * Class which defines a change to schema field.
 */
public interface SchemaFieldChangeActionInfo extends RecordInfo {
  /**
   * Previous data field state.
   * @return Previous State
   */
  DataFieldInfo getPreviousState();

  /**
   * Previous data field state.
   * @return true if Previous State is not null
   */
  boolean hasPreviousState();

  /**
   * New descriptor state.
   * @return New State
   */
  DataFieldInfo getNewState();

  /**
   * New descriptor state.
   * @return true if New State is not null
   */
  boolean hasNewState();

  /**
   * Bitmask that defines the changes that were applied to the field.
   * @return Change Types
   */
  SchemaFieldChangeType getChangeTypes();

  /**
   * Bitmask that defines the changes that were applied to the field.
   * @return true if Change Types is not null
   */
  boolean hasChangeTypes();

  /**
   * Defines the data transformation that was applied to the field.
   * @return Data Transformation
   */
  SchemaFieldDataTransformationInfo getDataTransformation();

  /**
   * Defines the data transformation that was applied to the field.
   * @return true if Data Transformation is not null
   */
  boolean hasDataTransformation();

  /**
   * Method copies state to a given instance
   */
  @Override
  SchemaFieldChangeActionInfo clone();
}
