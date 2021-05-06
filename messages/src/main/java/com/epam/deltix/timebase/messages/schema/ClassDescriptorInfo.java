package com.epam.deltix.timebase.messages.schema;

import java.lang.CharSequence;
import java.lang.Override;

/**
 * Schema definition for a schema node.
 */
public interface ClassDescriptorInfo extends NamedDescriptorInfo {
  /**
   * Optional GUID for this node.
   * @return Guid
   */
  CharSequence getGuid();

  /**
   * Optional GUID for this node.
   * @return true if Guid is not null
   */
  boolean hasGuid();

  /**
   * Method copies state to a given instance
   */
  @Override
  ClassDescriptorInfo clone();
}
