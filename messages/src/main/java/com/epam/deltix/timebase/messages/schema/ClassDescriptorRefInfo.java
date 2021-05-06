package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.CharSequence;
import java.lang.Override;

/**
 * Schema definition for a reference to class descriptor.
 */
public interface ClassDescriptorRefInfo extends RecordInfo {
  /**
   * Node name.
   * @return Name
   */
  CharSequence getName();

  /**
   * Node name.
   * @return true if Name is not null
   */
  boolean hasName();

  /**
   * Method copies state to a given instance
   */
  @Override
  ClassDescriptorRefInfo clone();
}
