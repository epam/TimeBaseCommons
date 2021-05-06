package com.epam.deltix.timebase.messages.schema;

import java.lang.Override;

/**
 * Schema definition of array data type.
 */
public interface ArrayDataTypeInfo extends DataTypeInfo {
  /**
   * Array element data type.
   * @return Element Type
   */
  DataTypeInfo getElementType();

  /**
   * Array element data type.
   * @return true if Element Type is not null
   */
  boolean hasElementType();

  /**
   * Method copies state to a given instance
   */
  @Override
  ArrayDataTypeInfo clone();
}
