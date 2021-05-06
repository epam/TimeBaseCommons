package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.util.collections.generated.ObjectList;
import java.lang.Override;

/**
 * Class which defines a change to schema descriptor.
 */
public interface SchemaDescriptorChangeActionInfo extends RecordInfo {
  /**
   * Previous descriptor state.
   * @return Previous State
   */
  ClassDescriptorInfo getPreviousState();

  /**
   * Previous descriptor state.
   * @return true if Previous State is not null
   */
  boolean hasPreviousState();

  /**
   * New descriptor state.
   * @return New State
   */
  ClassDescriptorInfo getNewState();

  /**
   * New descriptor state.
   * @return true if New State is not null
   */
  boolean hasNewState();

  /**
   * Bitmask that defines the changes that were applied to the descriptor.
   * @return Change Types
   */
  SchemaDescriptorChangeType getChangeTypes();

  /**
   * Bitmask that defines the changes that were applied to the descriptor.
   * @return true if Change Types is not null
   */
  boolean hasChangeTypes();

  /**
   * Defines the data transformation that was applied to the descriptor.
   * @return Descriptor Transformation
   */
  SchemaDescriptorTransformationInfo getDescriptorTransformation();

  /**
   * Defines the data transformation that was applied to the descriptor.
   * @return true if Descriptor Transformation is not null
   */
  boolean hasDescriptorTransformation();

  /**
   * A list of change actions to data fields.
   * Populated only if ChangeTypes contains ALTER flag
   * @return Field Change Actions
   */
  ObjectList<SchemaFieldChangeActionInfo> getFieldChangeActions();

  /**
   * A list of change actions to data fields.
   * Populated only if ChangeTypes contains ALTER flag
   * @return true if Field Change Actions is not null
   */
  boolean hasFieldChangeActions();

  /**
   * Method copies state to a given instance
   */
  @Override
  SchemaDescriptorChangeActionInfo clone();
}
