package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.util.collections.generated.ObjectArrayList;
import java.lang.Override;

/**
 * Schema definition for enumeration class.
 */
public interface EnumClassDescriptorInterface extends EnumClassDescriptorInfo, ClassDescriptorInterface {
  /**
   * List of values of enumeration.
   * @param value - Values
   */
  void setValues(ObjectArrayList<EnumValueInfo> value);

  /**
   * List of values of enumeration.
   */
  void nullifyValues();

  /**
   * True, if enumeration represents a bitmask.
   * @param value - Is Bitmask
   */
  void setIsBitmask(boolean value);

  /**
   * True, if enumeration represents a bitmask.
   */
  void nullifyIsBitmask();

  /**
   * Method nullifies all instance properties
   */
  @Override
  EnumClassDescriptorInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  EnumClassDescriptorInterface reset();

  @Override
  EnumClassDescriptorInterface copyFrom(RecordInfo template);
}
