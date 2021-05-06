package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import com.epam.deltix.util.collections.generated.ObjectArrayList;
import java.lang.Override;

/**
 * Class which defines a change to schema descriptor.
 */
public interface SchemaDescriptorChangeActionInterface extends SchemaDescriptorChangeActionInfo, RecordInterface {
  /**
   * Previous descriptor state.
   * @param value - Previous State
   */
  void setPreviousState(ClassDescriptorInfo value);

  /**
   * Previous descriptor state.
   */
  void nullifyPreviousState();

  /**
   * New descriptor state.
   * @param value - New State
   */
  void setNewState(ClassDescriptorInfo value);

  /**
   * New descriptor state.
   */
  void nullifyNewState();

  /**
   * Bitmask that defines the changes that were applied to the descriptor.
   * @param value - Change Types
   */
  void setChangeTypes(SchemaDescriptorChangeType value);

  /**
   * Bitmask that defines the changes that were applied to the descriptor.
   */
  void nullifyChangeTypes();

  /**
   * Defines the data transformation that was applied to the descriptor.
   * @param value - Descriptor Transformation
   */
  void setDescriptorTransformation(SchemaDescriptorTransformationInfo value);

  /**
   * Defines the data transformation that was applied to the descriptor.
   */
  void nullifyDescriptorTransformation();

  /**
   * A list of change actions to data fields.
   * Populated only if ChangeTypes contains ALTER flag
   * @param value - Field Change Actions
   */
  void setFieldChangeActions(ObjectArrayList<SchemaFieldChangeActionInfo> value);

  /**
   * A list of change actions to data fields.
   * Populated only if ChangeTypes contains ALTER flag
   */
  void nullifyFieldChangeActions();

  /**
   * Method nullifies all instance properties
   */
  @Override
  SchemaDescriptorChangeActionInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  SchemaDescriptorChangeActionInterface reset();

  @Override
  SchemaDescriptorChangeActionInterface copyFrom(RecordInfo template);
}
