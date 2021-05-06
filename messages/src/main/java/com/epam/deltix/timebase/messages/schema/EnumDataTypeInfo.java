package com.epam.deltix.timebase.messages.schema;

import java.lang.Override;

/**
 * Schema definition of enum data type.
 */
public interface EnumDataTypeInfo extends DataTypeInfo {
  /**
   * Definition for enumeration class.
   * @return Type Descriptor
   */
  ClassDescriptorRefInfo getTypeDescriptor();

  /**
   * Definition for enumeration class.
   * @return true if Type Descriptor is not null
   */
  boolean hasTypeDescriptor();

  /**
   * Method copies state to a given instance
   */
  @Override
  EnumDataTypeInfo clone();
}
