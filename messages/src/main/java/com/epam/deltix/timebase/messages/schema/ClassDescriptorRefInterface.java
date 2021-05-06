package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import java.lang.CharSequence;
import java.lang.Override;

/**
 * Schema definition for a reference to class descriptor.
 */
public interface ClassDescriptorRefInterface extends ClassDescriptorRefInfo, RecordInterface {
  /**
   * Node name.
   * @param value - Name
   */
  void setName(CharSequence value);

  /**
   * Node name.
   */
  void nullifyName();

  /**
   * Method nullifies all instance properties
   */
  @Override
  ClassDescriptorRefInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  ClassDescriptorRefInterface reset();

  @Override
  ClassDescriptorRefInterface copyFrom(RecordInfo template);
}
