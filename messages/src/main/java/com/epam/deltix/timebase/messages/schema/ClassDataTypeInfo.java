package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.util.collections.generated.ObjectList;
import java.lang.Override;

/**
 * Schema definition of class data type.
 */
public interface ClassDataTypeInfo extends DataTypeInfo {
  /**
   * Definitions for nested schema classes.
   * @return Type Descriptors
   */
  ObjectList<ClassDescriptorRefInfo> getTypeDescriptors();

  /**
   * Definitions for nested schema classes.
   * @return true if Type Descriptors is not null
   */
  boolean hasTypeDescriptors();

  /**
   * Method copies state to a given instance
   */
  @Override
  ClassDataTypeInfo clone();
}
