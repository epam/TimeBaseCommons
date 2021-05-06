package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.CharSequence;
import java.lang.Override;

/**
 * Schema definition for a schema node.
 */
public interface ClassDescriptorInterface extends ClassDescriptorInfo, NamedDescriptorInterface {
  /**
   * Optional GUID for this node.
   * @param value - Guid
   */
  void setGuid(CharSequence value);

  /**
   * Optional GUID for this node.
   */
  void nullifyGuid();

  /**
   * Method nullifies all instance properties
   */
  @Override
  ClassDescriptorInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  ClassDescriptorInterface reset();

  @Override
  ClassDescriptorInterface copyFrom(RecordInfo template);
}
