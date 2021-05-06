package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.Override;

/**
 * Schema definition of array data type.
 */
public interface ArrayDataTypeInterface extends ArrayDataTypeInfo, DataTypeInterface {
  /**
   * Array element data type.
   * @param value - Element Type
   */
  void setElementType(DataTypeInfo value);

  /**
   * Array element data type.
   */
  void nullifyElementType();

  /**
   * Method nullifies all instance properties
   */
  @Override
  ArrayDataTypeInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  ArrayDataTypeInterface reset();

  @Override
  ArrayDataTypeInterface copyFrom(RecordInfo template);
}
