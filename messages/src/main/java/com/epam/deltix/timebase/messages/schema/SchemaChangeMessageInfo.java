package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.MessageInfo;
import com.epam.deltix.util.collections.generated.ObjectList;
import java.lang.Override;

/**
 * Message tha defines a change in stream schema.
 */
public interface SchemaChangeMessageInfo extends MessageInfo {
  /**
   * Previous schema state.
   * @return Previous State
   */
  ObjectList<ClassDescriptorInfo> getPreviousState();

  /**
   * Previous schema state.
   * @return true if Previous State is not null
   */
  boolean hasPreviousState();

  /**
   * New schema state.
   * @return New State
   */
  ObjectList<ClassDescriptorInfo> getNewState();

  /**
   * New schema state.
   * @return true if New State is not null
   */
  boolean hasNewState();

  /**
   * New schema state.
   * @return Descriptor Change Actions
   */
  ObjectList<SchemaDescriptorChangeActionInfo> getDescriptorChangeActions();

  /**
   * New schema state.
   * @return true if Descriptor Change Actions is not null
   */
  boolean hasDescriptorChangeActions();

  /**
   * Message version
   * @return Version
   */
  long getVersion();

  /**
   * Message version
   * @return true if Version is not null
   */
  boolean hasVersion();

  /**
   * Method copies state to a given instance
   */
  @Override
  SchemaChangeMessageInfo clone();
}
