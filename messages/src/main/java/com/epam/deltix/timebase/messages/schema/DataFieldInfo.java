package com.epam.deltix.timebase.messages.schema;

import java.lang.Override;

/**
 * This is a base class for schema data field.
 */
public interface DataFieldInfo extends NamedDescriptorInfo {
  /**
   * Data type of a field.
   * @return Data Type
   */
  DataTypeInfo getDataType();

  /**
   * Data type of a field.
   * @return true if Data Type is not null
   */
  boolean hasDataType();

  /**
   * Method copies state to a given instance
   */
  @Override
  DataFieldInfo clone();
}
