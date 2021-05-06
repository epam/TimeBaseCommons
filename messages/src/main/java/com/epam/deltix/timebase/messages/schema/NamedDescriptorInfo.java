package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.CharSequence;
import java.lang.Override;

/**
 * This is a base class for schema nodes.
 */
public interface NamedDescriptorInfo extends RecordInfo {
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
   * Node title.
   * @return Title
   */
  CharSequence getTitle();

  /**
   * Node title.
   * @return true if Title is not null
   */
  boolean hasTitle();

  /**
   * Node description.
   * @return Description
   */
  CharSequence getDescription();

  /**
   * Node description.
   * @return true if Description is not null
   */
  boolean hasDescription();

  /**
   * Method copies state to a given instance
   */
  @Override
  NamedDescriptorInfo clone();
}
