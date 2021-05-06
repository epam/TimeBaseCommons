package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.MessageInterface;
import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.util.collections.generated.ObjectArrayList;
import java.lang.Override;

/**
 * Message tha defines a change in stream schema.
 */
public interface SchemaChangeMessageInterface extends SchemaChangeMessageInfo, MessageInterface {
  /**
   * Previous schema state.
   * @param value - Previous State
   */
  void setPreviousState(ObjectArrayList<ClassDescriptorInfo> value);

  /**
   * Previous schema state.
   */
  void nullifyPreviousState();

  /**
   * New schema state.
   * @param value - New State
   */
  void setNewState(ObjectArrayList<ClassDescriptorInfo> value);

  /**
   * New schema state.
   */
  void nullifyNewState();

  /**
   * New schema state.
   * @param value - Descriptor Change Actions
   */
  void setDescriptorChangeActions(ObjectArrayList<SchemaDescriptorChangeActionInfo> value);

  /**
   * New schema state.
   */
  void nullifyDescriptorChangeActions();

  /**
   * Message version
   * @param value - Version
   */
  void setVersion(long value);

  /**
   * Message version
   */
  void nullifyVersion();

  /**
   * Method nullifies all instance properties
   */
  @Override
  SchemaChangeMessageInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  SchemaChangeMessageInterface reset();

  @Override
  SchemaChangeMessageInterface copyFrom(RecordInfo template);
}
