package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.util.collections.generated.ObjectList;
import java.lang.Override;

/**
 * Schema definition for enumeration class.
 */
public interface EnumClassDescriptorInfo extends ClassDescriptorInfo {
  /**
   * List of values of enumeration.
   * @return Values
   */
  ObjectList<EnumValueInfo> getValues();

  /**
   * List of values of enumeration.
   * @return true if Values is not null
   */
  boolean hasValues();

  /**
   * True, if enumeration represents a bitmask.
   * @return Is Bitmask
   */
  boolean isBitmask();

  /**
   * True, if enumeration represents a bitmask.
   * @return true if Is Bitmask is not null
   */
  boolean hasIsBitmask();

  /**
   * Method copies state to a given instance
   */
  @Override
  EnumClassDescriptorInfo clone();
}
